# Blocking I/O with Virtual Threads

VT’s sweet spot: **write blocking code** that waits on **cooperating** JDK blocking points without holding a carrier (when unmount works).

## Mental Model

```text
VT calls jdbc.query() / http.send() / Thread.sleep() / queue.take()
→ blocks
→ unmount (normally for socket I/O, park, sleep, …)
→ carrier free
→ later resume
```

**Do not equate all “disk” with network unmount.** On Linux, **local file I/O can still pin** carriers (residual pinning). Prefer measuring file-heavy paths; see [thread-pinning.md](./thread-pinning.md).

## Why It Matters

Pre-Loom: blocking I/O forced huge platform pools or async APIs. With VT: keep JDBC and `HttpClient` synchronous style.

## Code

```java
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    return exec.submit(() -> {
        var order = orderRepo.find(id);          // JDBC block
        var tax = taxClient.quote(order);        // HTTP block
        return OrderResponse.from(order, tax);
    }).get();
}
```

## Production Scenario — database-heavy service

Each request VT blocks on connections from Hikari. Concurrency of **queries** ≤ pool size. VT lets you have 10K request threads waiting **for a connection** without 10K OS threads — but connection wait still hurts latency.

## Failure Scenario

Blocking inside a pin (legacy) or holding a lock across I/O → carrier stuck. Or no timeouts on blocking calls → VT pile-up.

## Best Practices

- Timeouts on JDBC/HTTP  
- Bounded connection pools  
- Don’t hold locks across I/O (design — still true after JEP 491)  
- Prefer Java APIs that cooperate with VT unmount; treat local file I/O as residual-pinning risk until measured 

## Interview / PE

Why is blocking “OK” on VT? What still isn’t free about blocking?

### Related

[thread-pinning.md](./thread-pinning.md) · [database-connection-pools.md](./database-connection-pools.md) · [http-clients.md](./http-clients.md)
