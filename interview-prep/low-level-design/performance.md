# Low-Level Design — Performance

LLD performance = **data structures + lock scope + allocations**, not network (that’s SD).

## Hot spots

| Choice | Risk |
|--------|------|
| Coarse lock on entire lot/cache | Low concurrency |
| `List` scans for lookups | O(n) under growth |
| Chatty observers | Amplify work per event |
| Unbounded queues in actors | Memory blowups |
| Synchronized + blocking I/O | Latency under load (pinning if VT) |

## Rules

1. Index by key (`Map`) for hot lookups.  
2. Keep critical sections small; prefer immutability.  
3. State complexity before optimizing — correctness first.  
4. For caches: O(1) get/put with clear eviction.

## Measurement

- Unit microbench only after design settles.  
- Under load: lock contention profiles, allocation rate.

Related: [testability.md](../../low-level-design/concepts/testability.md), [../../interview-prep/collections](../collections/).
