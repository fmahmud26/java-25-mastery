# 02 — Concurrent Order Processor

**Tier:** Concurrency · **Demonstrates:** virtual threads, locks, atomics, CompletableFuture, concurrent maps

## Problem

Simulate checkout under concurrency: many orders reserve inventory and debit accounts without overselling or double-spending, while composing async payment + inventory steps.

## Requirements

**Functional**
- Submit N orders concurrently  
- Reserve inventory; debit account; mark SUCCESS/FAILED  
- Ledger transfer API under lock  

**Non-functional**
- No lost updates on inventory/balances  
- Virtual threads for order workers  
- Platform pool OK for CPU-ish reconciliation  
- Metrics: processed/failed counts  

## Architecture

```text
OrderProcessorApp
  → OrderProcessor (orchestration)
      → InventoryService (ConcurrentHashMap + atomic ops / compute)
      → AccountLedger (striped ReentrantLock per account)
      → PaymentGatewayPort (simulated delay; CF)
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| VT per order | Blocking simulated I/O | One platform thread per order at 10k |
| `CompletableFuture` for pay+reserve | Composition / timeout | Nested manual threads |
| Striped locks on accounts | Parallelism across accounts | Single global lock |
| `ConcurrentHashMap` inventory | Fine-grained | `synchronized` Hashtable |

## Design decisions

- **Inventory** uses atomic `compute` to avoid oversell races.  
- **Ledger** uses per-account locks ordered by account id to avoid deadlock on transfer.  
- **VT for workers**; don’t put CPU-heavy crypto on unlimited VT without measurement (lab in 05).  
- Failures are first-class statuses — not swallowed exceptions.

## Implementation plan

1. Domain: Order, OrderStatus  
2. InventoryService with CAS-style decrement  
3. AccountLedger with ordered locks  
4. Compose payment+inventory with CF or structured sequential VT calls  
5. Driver with `--orders` / `--workers`  
6. Stress test: same SKU contention  

## Failure scenarios

| Failure | Expected |
|---------|----------|
| Insufficient stock | Order FAILED; no debit |
| Insufficient balance | Release reservation (compensate) |
| Payment timeout | Idempotent retry or fail + release |
| Parallel same account | Serialized by lock; balances consistent |

## Testing strategy

- Concurrent test: 1000 orders for stock=500 → success≤500  
- Invariants: sum(balances) conserved on transfers  
- Deadlock smoke: many transfers random pairs under timeout  
- Deterministic failure injection on payment port  

## Performance considerations

- Hot SKU → lock/CAS contention; measure before sharding  
- VT stacking on one monitor still serializes  
- Avoid synchronized logging on hot path  

## Scaling strategy

- Next step: partition inventory by SKU; external DB with conditional updates  
- Payment becomes remote → add idempotency (project 07)  

## Interview discussion

“I show shared-state invariants under VT load: atomic inventory, deadlock-safe ledger locks, and clear compensation when payment fails. The point isn’t the domain — it’s race-free composition.”

**Follow-ups:** Why not `synchronized` on the whole service? How would you prove linearizability of inventory?

## Run

```bash
chmod +x run.sh && ./run.sh
./run.sh --orders 200 --workers 50
```
