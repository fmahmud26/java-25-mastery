# Virtual Threads — Java 25 Deep Guide

Project Loom’s **virtual threads** make the thread-per-request style scalable for **blocking waits**. They multiplex many lightweight threads onto few **carrier** platform threads. They do **not** invent CPU cores or expand DB/HTTP connection pools.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
Request → Virtual Thread (cheap)
              ↕ mount / unmount
         Carrier Platform Thread (scarce)
              ↓
         Blocking I/O / waits
              ↓
         Downstream pools (still scarce!)
```

## Study path

1. Model: [platform-threads](./platform-threads.md) → [virtual-threads](./virtual-threads.md) → [carrier-threads](./carrier-threads.md) → [jvm-scheduling](./jvm-scheduling.md)  
2. Style: [thread-per-request-model](./thread-per-request-model.md) → [blocking-io](./blocking-io.md) → [thread-pools-with-vt](./thread-pools-with-vt.md) → [executors-new-virtual-thread-per-task-executor](./executors-new-virtual-thread-per-task-executor.md)  
3. Hazards: [thread-pinning](./thread-pinning.md) · [synchronization-and-vt](./synchronization-and-vt.md) · [memory](./memory.md)  
4. Limits: [database-connection-pools](./database-connection-pools.md) · [http-clients](./http-clients.md) · [downstream-limitations](./downstream-limitations.md) · [when-vt-do-not-help](./when-vt-do-not-help.md)  
5. Compare: [vt-vs-platform-completablefuture-reactive](./vt-vs-platform-completablefuture-reactive.md) · [virtual-threads-vs-reactive-programming](./virtual-threads-vs-reactive-programming.md) · **canonical:** [../reactive-programming/vs-virtual-threads.md](../reactive-programming/vs-virtual-threads.md)  
6. Practice: [scenarios](./scenarios.md) · [experiments](./experiments.md) · [principal-architecture-decisions](./principal-architecture-decisions.md) · [interview](./interview.md)

## One-line PE rule

**VT amplify concurrency of waiting; bottlenecks move to pools, CPU, and dependencies — measure those.**
