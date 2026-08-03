# Concurrency — Staff / Principal Guide (Java 25)

Shared-memory concurrency on the JVM: threads, the memory model, locks, atomics, synchronizers, and concurrent collections. This folder targets **Staff/Principal interviews** and production incident skills — not “start a Thread” tutorials.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Foundations: [process-vs-thread](./process-vs-thread.md) → [platform-threads](./platform-threads.md) → [thread-lifecycle](./thread-lifecycle.md) → [creating-threads](./creating-threads.md)  
2. Tasks: [runnable](./runnable.md) · [callable](./callable.md) · [future](./future.md) · [executor-service](./executor-service.md)  
3. JMM: [java-memory-model](./java-memory-model.md) → [happens-before](./happens-before.md) → [visibility](./visibility.md) · [atomicity](./atomicity.md) · [ordering](./ordering.md)  
4. Sync tools: [synchronization](./synchronization.md) · [synchronized](./synchronized.md) · [monitor](./monitor.md) · [volatile](./volatile.md) · [cas](./cas.md) · [atomic-variables](./atomic-variables.md)  
5. Locks: [reentrantlock](./reentrantlock.md) · [readwritelock](./readwritelock.md) · [stampedlock](./stampedlock.md)  
6. Coordinators: [countdownlatch](./countdownlatch.md) · [cyclicbarrier](./cyclicbarrier.md) · [semaphore](./semaphore.md) · [phaser](./phaser.md)  
7. Structures: [concurrenthashmap](./concurrenthashmap.md) · [blockingqueue](./blockingqueue.md) · [concurrent-queues](./concurrent-queues.md) · [copyonwritearraylist](./copyonwritearraylist.md)  
8. Hazards: [race-condition](./race-condition.md) · [deadlock](./deadlock.md) · [livelock](./livelock.md) · [starvation](./starvation.md) · [contention](./contention.md)  
9. Ops: [thread-dumps-and-debugging](./thread-dumps-and-debugging.md) · Drill: [interview.md](./interview.md)

## Scenario index

| Domain | Typical tools |
|--------|----------------|
| Payments | Idempotency + CHM, striped locks, careful retries |
| Inventory | Atomic stock / DB txn; avoid lost updates |
| Orders | Workers + BlockingQueue; state machines |
| Caches | CHM / COW; stamped optimistic reads |
| Job processing | ExecutorService, Semaphore limits |
| High-traffic APIs | Pool sizing, VT contrast, contention metrics |

## Principal stance

Correctness first (JMM + invariants). Then latency under contention. Prefer higher-level concurrency utilities over hand-rolled volatiles. Measure; thread-dump before guessing. Virtual threads (see VT folder) change *blocking* economics — they do **not** remove data races.
