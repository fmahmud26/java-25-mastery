# Happens-Before

A JMM relation: if **A happens-before B**, then A’s writes are visible to B and ordered before B’s actions (for the relevant memory).

## Mental Model

```text
write x=1
unlock M   ──HB──►  lock M
                    read x   // must see 1 (if no other writes)
```

HB is transitive. Program order within a thread also establishes HB between that thread’s actions.

## Key Edges (memorize these)

| Edge | Example |
|------|---------|
| Monitor unlock → later lock (same monitor) | `synchronized` |
| Volatile write → later volatile read (same field) | `volatile` |
| Thread `start()` → first action in started thread | lifecycle |
| Last action in thread → `join()` return in joiner | lifecycle |
| Successful CAS / atomic sync writes | `Atomic*`, VarHandle volatile/CAS modes |
| Concurrent collection documented happens-before | e.g. CHM publish |

Full list is in the JLS; interviews expect the table above plus “ask for the edge.”

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

## Production Scenario — handoff queue

Producer `put` HB consumer `take` on a BlockingQueue — payload fields written before `put` are visible after `take` without extra volatiles on each field.

## Failure Scenario

Double-checked locking **without** volatile (or other safe publication) on the published reference → broken under JMM (partially constructed object).

## Debugging Strategy

Ask: “What HB edge publishes this write?” If none → bug. Draw writer actions → edge → reader actions in code review.

## Performance

HB edges cost (barriers/locks). Minimize critical section size; don’t invent edges with random volatiles.

## Trade-offs

Volatile flag vs synchronized block vs concurrent pub/sub structures vs VarHandle acquire/release ([varhandles.md](./varhandles.md)).

## Interview Questions

- List three HB edges.  
- Why was old DCL broken?  
- Does `synchronized` only give mutual exclusion?  
- Does HB mean “earlier in wall-clock time”? (No — it’s a JMM relation.)  

## Principal-Level Discussion

Every shared mutable publication path needs an intentional HB story. Code review question: “How does the reader synchronize with the writer?”

### Related

[java-memory-model.md](./java-memory-model.md) · [volatile.md](./volatile.md) · [synchronized.md](./synchronized.md) · [varhandles.md](./varhandles.md)
