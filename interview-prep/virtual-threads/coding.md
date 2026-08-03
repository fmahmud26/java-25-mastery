# Virtual Threads — Coding

Patterns that show up constantly:

| Problem | Pattern |
|---------|---------|
| Fan-out N HTTP/DB calls | VT per task executor |
| Thread-per-request server | VT-backed executor / container support |
| Mix I/O + CPU | VT orchestrates; platform pool for CPU |
| Bounded outbound concurrency | `Semaphore` even with VTs |
| Structured fan-in | `StructuredTaskScope` (preview) |
| Migrate from platform pool | Replace blocking pool with VT-per-task |

```java
// Fan-out / fan-in with virtual threads
List<Future<String>> futures = new ArrayList<>();
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    for (String url : urls) {
        futures.add(exec.submit(() -> httpGet(url)));
    }
}
List<String> bodies = new ArrayList<>();
for (var f : futures) bodies.add(f.get());
```

```java
// Bound concurrency — VTs are cheap; downstream may not be
Semaphore permits = new Semaphore(50);

String fetch(String id) throws InterruptedException {
    permits.acquire();
    try {
        return client.get(id);
    } finally {
        permits.release();
    }
}
```

```java
// Detect / branch on thread type (rare in app code; useful in libs)
if (Thread.currentThread().isVirtual()) {
    // assume blocking OK
} else {
    // maybe offload blocking to another executor
}
```

**Talk track:** platform pool bottleneck on blocking I/O → VT per task → watch pinning & downstream limits → optional structured scope for lifetimes.

Related: [../../virtual-threads](../../virtual-threads/), [../../concurrency](../../concurrency/).
