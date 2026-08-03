# Networking ↔ Java Backend Systems

How concepts show up in real services.

| Concept | Java backend manifestation |
|---------|----------------------------|
| TCP | HttpClient / gRPC channel / Tomcat connector |
| DNS | K8s service names, `UnknownHostException` |
| TLS | HTTPS, mTLS sidecars, `SSLContext` |
| HTTP | REST JSON between services |
| Pooling | Shared `HttpClient`, Hikari (DB is analogous) |
| Timeouts | `connectTimeout` + request `timeout` + app deadlines |
| Retries | Resilience4j / custom; idempotency keys |
| Sockets | Rare raw; mostly framework-owned |

## Composition Root Checklist

1. Singleton `HttpClient`(s) per dependency class  
2. Explicit timeouts from SLO budget  
3. Metrics: latency, error, timeout, retry count per peer  
4. Bulkhead concurrency limits  
5. Retry policy reviewed for POSTs  

## VT Note

Virtual threads make blocking `send` scalable for waiting — they do **not** remove the need for pools/timeouts/retry discipline.

### Related

[http-client.md](./http-client.md) · [principal-decisions.md](./principal-decisions.md) · [scenarios.md](./scenarios.md)
