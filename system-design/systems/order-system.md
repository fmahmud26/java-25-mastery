# Order System

E-commerce order taking with inventory, payment, and fulfillment — classic saga/outbox territory.

## Requirements

**Functional:** create order, pay, cancel, query status; reserve inventory; fulfill.  
**Non-functional:** no oversell beyond policy; idempotent create; strong-ish inventory; payment uncertainty handled.  
**Non-goals:** full warehouse WMS; recommendations.

## Capacity estimation

```text
2M orders/day ≈ 23 QPS → peak ×20 flash sale ≈ 500 QPS orders
Read (status/history) 20× → ~10k QPS reads
Order doc ~5KB → storage grows fast with history
Flash sale: single SKU hot key dominates
```

## Architecture

```text
Client → API Gateway → Order Service
                      ├─ Order DB + outbox
                      ├─ Inventory service (reserve/commit/release)
                      ├─ Payment service
                      └─ Kafka: OrderPlaced, Paid, Cancelled…
Fulfillment / Notify / Analytics consume events
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Order service | Aggregate lifecycle | Source of truth for order |
| Inventory | Stock reservations | Separate scale & contention |
| Payment | Capture money | Existing platform |
| Orchestrator or choreography | Saga steps | Cross-service txn |
| Outbox publisher | Reliable events | Dual-write fix |

## Data flow (happy path)

1. `CreateOrder(idempotencyKey)` → persist ORDER_PENDING  
2. Reserve inventory (hold TTL)  
3. Charge payment  
4. Mark PAID; commit inventory; emit events  
5. Fulfillment async  

**Cancel/fail:** release hold; void/refund payment as needed (compensations).

## Data storage

| Data | Store | Notes |
|------|-------|-------|
| Orders | SQL by `orderId` / `userId` | Lines, amounts, state |
| Idempotency | Unique key | |
| Inventory | SQL/Redis+DB | `SKU` row version / qty |
| Outbox | Same DB as order | |
| Events | Kafka | |

**Hot SKU:** isolate inventory rows; optional queue per SKU for serialization under flash sales.

## Scaling

- Order service stateless; shard orders by `userId`  
- Inventory service scaled + cached reads; writes contended per SKU  
- Read models (CQRS) for order history via events  
- Checkout path shed nonessential deps  

## Failure modes

| Failure | Mitigation |
|---------|------------|
| Pay succeeds, order update fails | Reconcile from payment id; idempotent complete |
| Inventory reserved, pay fails | TTL release + explicit compensate |
| Duplicate create | Idempotency → same order |
| Kafka down | Outbox drains later; don’t dual-write blindly |
| Flash sale oversell | Transactional decrement; optional waitlist |

## Observability

- Checkout conversion funnel, reserve fail rate, payment pending age  
- Per-SKU contention metrics, saga step timeouts  
- Lag on fulfillment consumers  

## Security

- Authz on user orders; fraud scoring hooks  
- Amount integrity server-side (never trust client totals)  
- Audit state transitions  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Inventory hold + TTL | Payment latency | Sync pay before reserve — poor UX / different risk |
| Outbox | Reliable integration | Direct Kafka publish in request |
| Choreography or orchestrator | Explicit PE pick | Hidden dual writes |
| CQRS reads | Scale history | Join forever on OLTP primary |

## Evolution

1. Monolith modular + single DB txn inventory+order (MVP)  
2. Split payment; outbox events  
3. Split inventory; saga  
4. Flash-sale SKU queues; multi-warehouse  

Related: [event-driven-architecture](../distributed-systems/event-driven-architecture.md), [idempotency](../distributed-systems/idempotency.md), [consistency](../fundamentals/consistency.md).
