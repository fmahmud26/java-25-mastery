# Retries

## Mental Model

Retry only when it helps **and** won’t duplicate side effects.

| Usually retry | Usually don’t |
|---------------|---------------|
| Connect failure / reset | `400` validation |
| `503` / `429` with backoff | `401`/`403` without new auth |
| Idempotent GET | POST create without idempotency key |
| Timed-out **read** of GET | Timed-out POST charge (unknown outcome) |

## Mechanism

```java
// sketch — production: jittered exponential backoff + max attempts + metrics
for (int attempt = 1; attempt <= max; attempt++) {
    try {
        var res = client.send(req, BodyHandlers.ofString());
        if (res.statusCode() != 503 && res.statusCode() != 429) return res;
    } catch (HttpConnectTimeoutException e) {
        if (attempt == max) throw e;
    }
    Thread.sleep(backoff(attempt)); // add jitter
}
```

Payments: `Idempotency-Key` header so safe retries don’t double-charge.

## Retry Storms

Blind retries × many pods × timeout = [retry-storms.md](./retry-storms.md).

### Related

[timeouts.md](./timeouts.md) · [partial-failure.md](./partial-failure.md) · [scenarios.md](./scenarios.md)
