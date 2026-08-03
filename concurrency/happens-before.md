# Happens-Before

A JMM relation: if **A happens-before B**, then A’s writes are visible to B and ordered before B’s actions.

## Mental Model

```text
write x=1
unlock M   ──HB──►  lock M
                    read x   // must see 1 (if no other writes)
```

HB is transitive. Program order within a thread also establishes HB.

## Internal Mechanics — key edges

| Edge | Example |
|------|---------|
| Monitor unlock → lock | `synchronized` |
| Volatile write → read | `volatile` field |
| Thread start | `start()` HB with run |
| Thread join | thread end HB with `join` return |
| CAS success / atomics | package rules |
| Concurrent collection ops | documented |

## Code

```java
synchronized (lock) { // unlock HB subsequent lock
    shared = build();
}
// other thread
synchronized (lock) {
    use(shared);
}
```

## Production Scenario — orders

State transition `PENDING → PAID` under same monitor ensures readers locking that monitor see fields written during payment.

## Failure Scenario

Double-checked locking **without** volatile on the published reference → broken under JMM (partially constructed object).

## Debugging Strategy

Ask: “What HB edge publishes this write?” If none → bug.

## Performance

HB edges cost (barriers/locks). Minimize critical section size.

## Trade-offs

Volatile flag vs synchronized block vs concurrent pub/sub structures.

## Interview Questions

- List three HB edges.  
- Why was old DCL broken?  
- Does `synchronized` only give mutual exclusion?

## Principal-Level Discussion

Every shared mutable publication path needs an intentional HB story. Code review question: “How does the reader synchronize with the writer?”

### Related

[java-memory-model.md](./java-memory-model.md) · [volatile.md](./volatile.md) · [synchronized.md](./synchronized.md)
