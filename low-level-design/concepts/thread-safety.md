# Thread Safety — Shared State Discipline

LLD concurrency = **name the shared mutable state**, then pick the smallest lock/atomic that preserves invariants.

## Questions to answer out loud

1. What is shared across threads?  
2. What invariant must hold (no double-book seat)?  
3. What is the critical section — and does it include I/O?  
4. What happens under contention?

## Toolkit (Java)

| Tool | Why choose it |
|------|----------------|
| Immutability / records | No races if never mutated |
| `synchronized` / intrinsic lock | Simple critical sections |
| `ReentrantLock` | Try-lock, conditions, fairness |
| `ConcurrentHashMap` | Segmented map ops |
| Atomcs / CAS | Counters, flags |
| Single-writer thread + queue | Elevator cabin command queue |

## Rules of thumb

- **Never** hold a lock while calling payment/SMS/network.  
- Prefer lock **per resource** (seat, spot) over global lock.  
- Document **happens-before** for publish of booking confirmation.  
- For inventories: compare-and-set status `FREE → HELD` beats “check then set” without sync.

## Staff phrasing

“Seats are the shared inventory. Hold is a CAS on seat state under a per-show lock striping map; payment runs outside the lock with a TTL hold that expires via scheduler.”
