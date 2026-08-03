# `newVirtualThreadPerTaskExecutor`

Preferred executor for VT-style concurrency on Java 25.

## Mental Model

```text
submit/execute → always start a new virtual thread
unlimited concurrent tasks (memory/OS permitting)
close() waits for completion (AutoCloseable)
```

## Code

```java
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<Result>> futures = ids.stream()
            .map(id -> exec.submit(() -> fetch(id)))
            .toList();
    for (Future<Result> f : futures) {
        results.add(f.get(2, TimeUnit.SECONDS));
    }
}
```

```java
// Structured concurrency is PREVIEW on Java 25 (JEP 505) — enable with --enable-preview
// Shape (conceptual): open scope → fork subtasks → join → close
// try (var scope = StructuredTaskScope.open()) {
//     var a = scope.fork(() -> fetch(id));
//     var b = scope.fork(() -> enrich(id));
//     scope.join();
// }
```

Be honest in interviews: **Structured Concurrency is preview** on Java 25; `newVirtualThreadPerTaskExecutor()` is the stable workhorse.

## Production Scenario — slow downstream API

Fan-out with per-call timeouts; cancel/abandon on budget exceed; semaphore if stampeding.

## Failure Scenario

Unbounded submit in a loop without timeout → memory growth / dependency melt.

## Interview / PE

Difference from `newCachedThreadPool`? Shutdown semantics?

### Related

[thread-pools-with-vt.md](./thread-pools-with-vt.md) · [scenarios.md](./scenarios.md)
