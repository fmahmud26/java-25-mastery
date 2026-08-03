# Thread-per-Request Model

One thread handles one request end-to-end — the classic servlet mental model, revived by virtual threads.

## Mental Model

```text
HTTP accept → assign thread → controller → service → DB/HTTP → response → thread done
Platform era: thread = scarce → pools of 200
VT era:       thread = cheap  → one VT per request feasible
```

## Why It Returned

Reactive was partly a workaround for scarce platform threads. VT removes that pressure for I/O-bound apps, restoring readable stacks and try/catch flow.

## Code

```java
// Conceptual Spring/Java 25 style
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    // server uses VT executor for requests
    exec.execute(() -> {
        var body = readRequest();
        var result = orderService.checkout(body); // blocks freely
        writeResponse(result);
    });
}
```

## Production Scenario — high-concurrency REST API

Checkout: validate → reserve inventory → capture payment → write order. Straight-line blocking code on a VT; timeouts at each remote call.

## Failure Scenario

Thread-per-request without **admission control** → unlimited VTs stampede DB. Need load shedding, pool bounds, queue limits at the edge.

## Comparison Snapshot

| Model | Threading |
|-------|-----------|
| Platform pool | Limited concurrent requests ≈ pool size |
| VT per request | Concurrent requests ≈ admitted load; wait on pools |
| Reactive | Event-loop + callbacks/futures |

## Interview / PE

Is thread-per-request “back”? What must still be bounded?

### Related

[virtual-threads.md](./virtual-threads.md) · [downstream-limitations.md](./downstream-limitations.md) · [principal-architecture-decisions.md](./principal-architecture-decisions.md)
