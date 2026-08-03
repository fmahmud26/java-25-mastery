# System Design — Principal Engineer Interview Prep

This section is **distributed product design under SLOs**: capacity, consistency, failure, and evolution — not buzzword diagrams.

Complementary: [low-level-design](../low-level-design/) (objects) · [coding-problems](../coding-problems/) (algorithms).

## How to use

1. Drill [fundamentals/](./fundamentals/) and [distributed-systems/](./distributed-systems/) until you can **teach** each lever.  
2. Design each system in [systems/](./systems/) under timebox using the template below.  
3. Rehearse with [interview.md](./interview.md) — clarify → numbers → architecture → hard part → failures → evolve.

## Required system template

Every system file contains:

```text
Requirements → Capacity estimation → Architecture → Components
→ Data flow → Data storage → Scaling → Failure modes
→ Observability → Security → Trade-offs → Evolution
```

Decisions use **choice → why → rejected alternative**.

## Foundations (must be fluent)

| Topic | Path |
|-------|------|
| Scalability | [fundamentals/scalability.md](./fundamentals/scalability.md) |
| Availability | [fundamentals/availability.md](./fundamentals/availability.md) |
| Reliability | [fundamentals/reliability.md](./fundamentals/reliability.md) |
| Latency | [fundamentals/latency.md](./fundamentals/latency.md) |
| Throughput | [fundamentals/throughput.md](./fundamentals/throughput.md) |
| Caching | [fundamentals/caching.md](./fundamentals/caching.md) |
| Load balancing | [fundamentals/load-balancing.md](./fundamentals/load-balancing.md) |
| Replication | [fundamentals/replication.md](./fundamentals/replication.md) |
| Partitioning | [fundamentals/partitioning.md](./fundamentals/partitioning.md) |
| Databases | [fundamentals/databases.md](./fundamentals/databases.md) |
| Consistency / CAP | Interview sketch: [fundamentals/consistency.md](./fundamentals/consistency.md), [cap-theorem.md](./fundamentals/cap-theorem.md) — **deep dive:** [distributed-systems/consistency.md](./distributed-systems/consistency.md), [cap.md](./distributed-systems/cap.md) |
| Observability | [fundamentals/observability.md](./fundamentals/observability.md) |
| Disaster recovery | [fundamentals/disaster-recovery.md](./fundamentals/disaster-recovery.md) |
| Queues | [distributed-systems/message-queues.md](./distributed-systems/message-queues.md) |
| Idempotency | [distributed-systems/idempotency.md](./distributed-systems/idempotency.md) |
| Rate limiting | [distributed-systems/rate-limiting.md](./distributed-systems/rate-limiting.md) |
| Retries / backoff | [distributed-systems/retry.md](./distributed-systems/retry.md), [backoff.md](./distributed-systems/backoff.md) |
| Circuit breakers | [distributed-systems/circuit-breakers.md](./distributed-systems/circuit-breakers.md) |

## Systems

| System | Core hardness |
|--------|----------------|
| [URL Shortener](./systems/url-shortener.md) | Extreme read skew, ID space, analytics fan-out |
| [Payment System](./systems/payment-system.md) | Idempotency, ledger, provider uncertainty |
| [Notification Platform](./systems/notification-platform.md) | Fan-out, retries, provider quotas |
| [Order System](./systems/order-system.md) | Inventory race, saga/outbox |
| [File Upload](./systems/file-upload.md) | Multipart, virus scan, object store |
| [Rate Limiter](./systems/rate-limiter.md) | Distributed counters, accuracy vs cost |
| [Distributed Cache](./systems/distributed-cache.md) | Consistency, hot keys, stampede |
| [Log Processing](./systems/log-processing.md) | Ingest volume, backpressure, exactly-once edges |

JVM notes: [java-focused.md](./java-focused.md).

## What “Principal” looks like

- Numbers before boxes; boxes before vendors.  
- Consistency chosen **per API**, not globally.  
- Failure modes with **detection + mitigation + customer impact**.  
- Explicit backpressure, idempotency, and DR (RPO/RTO).  
- Evolution path from MVP → 10× → multi-region without pretending MVP is final.

## Cross-links

| Need | Go to |
|------|--------|
| Answer spine | [../interview-prep/answer-framework.md](../interview-prep/answer-framework.md) · [../interview-prep/tracks/system-design.md](../interview-prep/tracks/system-design.md) |
| DS failure track | [../interview-prep/tracks/distributed-systems.md](../interview-prep/tracks/distributed-systems.md) |
| Timed mock | [../interview-prep/formats/mock-interviews/system-design-50.md](../interview-prep/formats/mock-interviews/system-design-50.md) |
| Incident lab | [../scenario-lab/](../scenario-lab/) |
| Cheat sheet | [../cheat-sheets/system-design.md](../cheat-sheets/system-design.md) |
| Objects (LLD) | [../low-level-design/](../low-level-design/) |
| PE decisions | [../principal-engineer/scenarios/](../principal-engineer/scenarios/) |
