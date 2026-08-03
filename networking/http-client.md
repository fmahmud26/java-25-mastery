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

## Async

```java
client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body);
```

On virtual threads, blocking `send` is often simpler for request-scoped work.

## Production

- **One** (or few) long-lived `HttpClient` instances per process — connection reuse  
- Per-request timeout always  
- Propagate `traceparent` / correlation headers  
- Bound body sizes; stream large payloads  

### Related

[timeouts.md](./timeouts.md) · [connection-pooling.md](./connection-pooling.md) · [java-net-http.md](./java-net-http.md)
