# Virtual Threads

JVM-scheduled lightweight threads (Loom). Final and central on **Java 21 LTS → Java 25 LTS**.

## Mental Model

```text
Virtual thread = continuation-like task the JVM mounts on a carrier to run bytecode
Block on supported ops → unmount → carrier runs another VT
Continue → remount (possibly other carrier)
```

Same `Thread` API. `Thread.currentThread()` works. ThreadLocals exist but scale carefully (see memory).

## Why They Exist

Platform thread-per-request hit OS limits. Reactive scaled concurrency but raised complexity. VT: **simple blocking code + high concurrency**.

## Code (Java 25)

```java
Thread.startVirtualThread(() -> handle(request));

Thread v = Thread.ofVirtual().name("req-", 0).start(() -> handle(request));

try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    var f1 = exec.submit(() -> db.find(id));
    var f2 = exec.submit(() -> http.enrich(id));
    return combine(f1.get(), f2.get());
}
```

## Internal Mechanics (engineering view)

1. VT starts → mounted on carrier from scheduler pool.  
2. Hits blocking (socket, park, many locks) → **unmount** (if not pinned).  
3. Carrier free for other VTs.  
4. I/O completes → VT runnable again → remount.  

Heap stacks grow/shrink; not a full OS stack per VT.

## Production Scenario — 10K concurrent HTTP requests

Servlet/Netty/Helidon/Spring with VT executor: each request blocks on JDBC/HTTP freely. Throughput rises until **Hikari/DB** or **downstream API** saturates — then tune pools/timeouts, not “more threads.”

## Failure Scenario

Unbounded fan-out of VTs to a 20-connection DB → wait storms, timeout cascades. Or CPU work on VT → carrier starvation.

## Structured concurrency (preview — know the idea)

Java 25 ships **Structured Concurrency** as a **preview** (JEP 505): group related subtasks in a `StructuredTaskScope`, fork (often on VTs), `join`, cancel as a unit. Requires `--enable-preview`. Prefer for fan-out with clear lifetimes; don’t treat preview APIs as mandatory production defaults until final.

## Scalability

Scales **number of blocked tasks**. Does not scale CPU or remote capacity.

## Memory

Cheaper than platform threads, not free — see [memory.md](./memory.md).

## Interview Questions

- What problem do VTs solve?  
- Mount/unmount?  
- Should you pool VTs?

## Principal-Level Discussion

Adopt VT for blocking servers to simplify architecture. Keep hard limits on **downstream resources**. Observability: task/VT count is vanity; watch pool wait, pinning (where applicable), p99, error rates.

### Related

[carrier-threads.md](./carrier-threads.md) · [jvm-scheduling.md](./jvm-scheduling.md) · [thread-per-request-model.md](./thread-per-request-model.md) · [downstream-limitations.md](./downstream-limitations.md)
