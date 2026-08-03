# Distributed Locks

Mutual exclusion across nodes. **Prefer designs that don’t need them** (atomic compare-and-swap in DB, single-partition ownership, unique constraints).

## When they appear

- Cron single-flyer  
- Leader election adjacent  
- Coalescing expensive compute  

## Unsafe pattern (production SEV classic)

```text
SET lock NX EX 30
# do work for 45s (GC pause) — lock expired
# another node acquires lock
# both run — double pay / double ship
```

**Missing:** fencing token. Without epoch checked by the resource, expired holders corrupt state.

## Safer patterns

| Pattern | Why safer |
|---------|-----------|
| DB `UPDATE … WHERE version=` | Fencing via row version |
| Lease + epoch; storage rejects stale epoch | Classic Redlock critique answer |
| Kafka partition / single consumer | Ordering ownership |
| Idempotent job keyed by `date+name` | Duplicate run OK |

## Production scenario

See [scenarios/lock-split-brain.md](./scenarios/lock-split-brain.md).

## Trade-offs

| Buy | Sell |
|-----|------|
| Exclusion | Availability (lock holder dead); complexity; false security without fencing |

## Principal interview angles

- “What fences a lock holder after pause?”  
- “Why not Redis lock for payment capture?”  

Related: [leader-follower.md](./leader-follower.md), [idempotency.md](./idempotency.md), [distributed-transactions.md](./distributed-transactions.md).
