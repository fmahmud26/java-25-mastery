# Phaser

Flexible multi-phase barrier — dynamic party registration; more powerful (complex) than CyclicBarrier.

## Mental Model

```text
parties register/arrive
advance phases 0,1,2,…
reusable + variable party count
```

## Internal Mechanics

Tiered phasers possible for scalability. `arriveAndAwaitAdvance`, `arriveAndDeregister`.

## Code

```java
Phaser phaser = new Phaser(1); // self
for (Job job : jobs) {
    phaser.register();
    executor.execute(() -> {
        try { process(job); }
        finally { phaser.arriveAndDeregister(); }
    });
}
phaser.arriveAndAwaitAdvance(); // wait for all jobs phase
```

## Production Scenario — batch processing

Dynamic worker count per phase of ETL (extract/transform/load).

## Failure Scenario

Mismatched register/deregister → hang. Overkill for simple latch needs.

## Debugging Strategy

Prefer simpler synchronizer unless dynamic phases required.

## Performance / Trade-offs

Powerful vs complexity. Most apps: Latch/Barrier/CompletableFuture.

## Interview Questions

- Phaser vs CyclicBarrier?  
- When dynamic registration matters?

## Principal-Level Discussion

Reach for Phaser rarely in product services; common in frameworks/engines.

### Related

[cyclicbarrier.md](./cyclicbarrier.md) · [countdownlatch.md](./countdownlatch.md)
