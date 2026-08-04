# HTTP Client (`java.net.http`)

Standard client (Java 11+): HTTP/1.1 & HTTP/2, sync/async, used heavily in service-to-service Java backends.

## Mental Model

```text
Shared HttpClient (pool + config)
    → HttpRequest (per call: URI, method, timeout, headers, body)
    → send / sendAsync
    → HttpResponse
```

## Mechanism

```java
HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .executor(Executors.newVirtualThreadPerTaskExecutor()) // optional
        .build();

HttpRequest req = HttpRequest.newBuilder(URI.create("https://inventory/stock/SKU-1"))
        .timeout(Duration.ofSeconds(3))
        .header("Accept", "application/json")
        .GET()
        .build();

HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
if (res.statusCode() >= 500) {
    // decide retry policy — see retries.md
}
```

## Async vs Virtual Threads

```java
client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body);
```

On virtual threads, blocking `send` is often simpler for request-scoped work. Prefer one model per service — mixing CF chains and VT without need raises debug cost. NIO/async background: [../io-nio/asynchronous-io.md](../io-nio/asynchronous-io.md).

## Production

- **One** (or few) long-lived `HttpClient` instances per process — connection reuse  
- Per-request timeout always; connect timeout on the client  
- Propagate `traceparent` / correlation headers  
- Bound body sizes; stream large payloads (`BodyHandlers.ofInputStream`)  
- Separate clients per downstream for bulkheads when limits differ  

## Production Scenario — per-request client

Team constructs `HttpClient.newHttpClient()` inside a handler. TLS handshakes explode; ephemeral ports rise; p99 tanks.

**Fix:** singleton/shared client; see [connection-pooling.md](./connection-pooling.md).

## Failure Modes

| Symptom | Likely |
|---------|--------|
| `HttpTimeoutException` | Request timeout / slow peer |
| Connect timeout | Peer down, DNS, SYN drop, TLS stall |
| `IOException` mid-body | Reset / partial response — treat carefully |

## When Not to Use Raw HttpClient Alone

Need rich middleware (auth refresh, hedged requests) — wrap with a small client facade; or use a mature stack — still apply the same timeout/pool rules.

## Principal Perspective

HttpClient is fine for production Java 25 services when timeouts, pooling, and retries are **designed**, not left as defaults.

### Related

[timeouts.md](./timeouts.md) · [connection-pooling.md](./connection-pooling.md) · [java-net-http.md](./java-net-http.md) · [retries.md](./retries.md)
