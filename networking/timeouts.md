# Timeouts

## Mental Model

```text
Connect timeout  — fail if TCP/TLS cannot establish
Request/read timeout — fail if response not finished in time
Overall budget — sum of hops < caller SLA
```

No timeout ⇒ threads/VTs pile up ⇒ cascading failure.

## Java HttpClient

```java
HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();

HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(3)) // response timeout
        .GET()
        .build();
```

Sockets: `connect(..., ms)`, `setSoTimeout(ms)`.

## Budgeting (service-to-service)

```text
Caller p99 SLO 300ms
  → inventory 80ms budget
  → payment 150ms budget
  → local work 50ms
```

Timeouts must be **shorter** than caller’s remaining budget or you cause retry storms upstream.

## Failures

[timeout-failures.md](./timeout-failures.md)

### Related

[retries.md](./retries.md) · [latency.md](./latency.md) · [http-client.md](./http-client.md)
