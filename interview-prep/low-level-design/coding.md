# Low-Level Design — Coding

Classic prompts and the design spine.

| Problem | Spine |
|---------|-------|
| Parking lot | Spots, Ticket, Vehicle, Pricing strategy |
| Elevator | Elevator state, Scheduler strategy, requests |
| Rate limiter | Algorithm interface (token/sliding), Clock port |
| LRU cache | Capacity + eviction policy; thread-safety story |
| Movie booking | Seats, Show, locking/reservation expiry |
| Vending machine | State machine + inventory |

```java
public interface RateLimiter {
    boolean tryAcquire(String key);
}

public final class TokenBucketLimiter implements RateLimiter {
    // Clock injected for tests
}
```

## Whiteboard checklist

1. Requirements & non-goals.  
2. Types diagram (5–12 types, not 40).  
3. One happy-path sequence.  
4. Concurrency note.  
5. “Add X” extension (new pricing / new spot type).

Practice: [../../low-level-design/problems](../../low-level-design/problems/), [interview.md](../../low-level-design/interview.md).
