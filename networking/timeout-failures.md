# Timeout Failures

## What Timed Out?

| Layer | Meaning |
|-------|---------|
| Connect | Never established TCP/TLS |
| Request/read | Established but no complete response |
| Caller deadline | Cancelled upstream — stop work |

## Cascades

Upstream timeout 1s, downstream timeout 5s → upstream gives up while downstream still works → duplicate work on retry.

**Rule:** downstream timeout < upstream remaining budget.

## Java Symptoms

`HttpTimeoutException`, `SocketTimeoutException`, thread dumps in `read`/`park` on client.

### Related

[timeouts.md](./timeouts.md) · [retry-storms.md](./retry-storms.md) · [partial-failure.md](./partial-failure.md)
