# Process vs Thread

## Mental Model

```text
OS Process (JVM)
  address space, fds, heap
  ├── platform thread A  ─┐
  ├── platform thread B  ─┼─ share heap → need sync
  └── platform thread C  ─┘
```

**Process** = isolation boundary. **Thread** = scheduled unit of execution sharing the process.

## Internal Mechanics

The JVM process maps to OS processes/threads (HotSpot). Platform threads ≈ OS threads. Heap objects are visible to all threads → races without safe publication. Separate JVMs don’t share heap — communicate via network/DB.

## Code

```java
// Same process, two threads, shared mutable balance — UNSAFE without sync
class Wallet {
    long cents; // shared
}
```

## Production Scenario — payments

Payment API JVM: Tomcat/Netty threads + worker pool share in-memory idempotency map → must be CHM/DB, not unsynchronized HashMap. Separate fraud JVM = different process; no shared memory races, but distributed consistency instead.

## Failure Scenario

Assuming “each request is isolated” while mutating a static `HashMap` → lost updates / corruption under traffic.

## Debugging Strategy

Confirm one process (`jcmd`, container) vs multiple pods. Races only within shared heap. Cross-pod bugs → distributed design, not synchronized.

## Performance

Thread create/switch cheaper than new process; still not free. Prefer pools / VT for many tasks.

## Trade-offs

| Processes | Threads |
|-----------|---------|
| Strong isolation | Shared-memory speed |
| IPC cost | Sync complexity |

## Interview Questions

- Why can one bad thread kill the JVM process?  
- How do microservices change the concurrency problem?

## Principal-Level Discussion

Isolation boundary choice (thread vs process vs pod) is an architecture decision: blast radius vs latency. In-process concurrency is a performance optimization that demands JMM literacy.

### Related

[platform-threads.md](./platform-threads.md) · [thread-lifecycle.md](./thread-lifecycle.md) · [race-condition.md](./race-condition.md)
