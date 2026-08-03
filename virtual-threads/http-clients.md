# HTTP Clients and Virtual Threads

Blocking HTTP on VTs is idiomatic — still bound by **connections, timeouts, and peer capacity**.

## Mental Model

```text
VT → HttpClient.send (block/unmount) → connection pool / HTTP/2 streams → dependency
```

## Code (Java 25)

```java
HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .executor(Executors.newVirtualThreadPerTaskExecutor()) // optional; client has own behavior
        .build();

HttpRequest req = HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(3))
        .GET()
        .build();

try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    return exec.submit(() -> client.send(req, BodyHandlers.ofString())).get();
}
```

Always set **request timeouts**. Prefer connection limits / HTTP/2 multiplexing awareness.

## Production Scenario — slow downstream API

10K VTs call a vendor with 100 RPS limit → self-inflicted outage. Need bulkhead (Semaphore), timeouts, circuit breaker, caching.

## Failure Scenario

No timeout → VT pile forever; memory up; peer overloaded. Shared JVM `HttpClient` without limits stampeding one host.

## Interview / PE

Blocking HttpClient + VT vs WebClient reactive? Where do timeouts live?

### Related

[blocking-io.md](./blocking-io.md) · [downstream-limitations.md](./downstream-limitations.md) · [scenarios.md](./scenarios.md)
