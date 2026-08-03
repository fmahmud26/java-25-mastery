# Virtual Threads — Implementation

Idiomatic Java 25 usage (not internals).

```java
// Start one virtual thread
Thread v = Thread.ofVirtual().name("req-", 0).start(() -> handle(request));
v.join();

Thread.startVirtualThread(() -> handle(request));

// Preferred server pattern — one VT per task, autoclose executor
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var id : ids) {
        exec.submit(() -> fetch(id));
    }
} // waits for tasks on close
```

## Builder / factory cheat sheet

| Need | API |
|------|-----|
| Named VT | `Thread.ofVirtual().name("w-", 0).start(…)` |
| Factory for libraries | `Thread.ofVirtual().factory()` |
| Per-task executor | `Executors.newVirtualThreadPerTaskExecutor()` |
| Platform thread (CPU) | `Thread.ofPlatform().start(…)` / fixed pool |
| Is this a VT? | `Thread.currentThread().isVirtual()` |

```java
// Split I/O (VT) from CPU (platform pool)
try (var io = Executors.newVirtualThreadPerTaskExecutor();
     var cpu = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
    io.submit(() -> {
        byte[] bytes = download();                 // block on VT
        return cpu.submit(() -> compress(bytes)).get();
    });
}
```

```java
// Structured concurrency — preview in Java 25 (enable preview)
try (var scope = StructuredTaskScope.open()) {
    var user = scope.fork(() -> findUser(id));
    var order = scope.fork(() -> findOrder(id));
    scope.join();                  // policy via Joiner / open() default
    return combine(user.get(), order.get());
}
```

## Do / don’t

| Do | Don’t |
|----|-------|
| Block on JDBC/HTTP freely on VTs | Pool virtual threads |
| Create millions of short VTs for I/O | Use VTs as the CPU parallelizer |
| Keep locks short | Hold `synchronized` across long I/O |

Related: [executors-new-virtual-thread-per-task-executor.md](../../virtual-threads/executors-new-virtual-thread-per-task-executor.md), [thread-per-request-model.md](../../virtual-threads/thread-per-request-model.md).
