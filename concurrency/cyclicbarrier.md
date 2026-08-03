# CyclicBarrier

Reusable barrier: N parties wait until all arrive, then proceed (optional barrier action).

## Mental Model

```text
each party await() → when N arrived → all released → can cycle again
```

## Internal Mechanics

Generations; broken barrier on timeout/interrupt/exception → `BrokenBarrierException`.

## Code

```java
CyclicBarrier barrier = new CyclicBarrier(4, () -> mergePartialResults());
executor.execute(() -> {
    computeShard(0);
    barrier.await();
});
```

## Production Scenario — batch analytics

Four workers process order shards; merge when all done; repeat next batch.

## Failure Scenario

One worker dies → others stuck until timeout; barrier broken. Prefer latch for one-shot startup.

## Debugging Strategy

WAITING on barrier; check party count mismatches.

## Performance

Sync points limit scalability — minimize barrier frequency.

## Trade-offs

Barrier vs Phaser (more dynamic) vs stream/parallel.

## Interview Questions

- Barrier vs Latch?  
- What is a broken barrier?

## Principal-Level Discussion

Barriers couple workers tightly — often a smell for elastic cloud workers; prefer queue + reduce.

### Related

[countdownlatch.md](./countdownlatch.md) · [phaser.md](./phaser.md)
