# Networking — Java Backend Deep Guide

How service-to-service calls actually fail: TCP, DNS, TLS, HTTP, pools, timeouts, and retries — mapped to `java.net` / `java.net.http`.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
Service A                Service B
   │                        │
 DNS resolve                │
 TCP connect + TLS          │
 HTTP request/response      │
 pools / timeouts / retries │
```

## Study path

1. Foundations: [tcp](./tcp.md) · [dns](./dns.md) · [sockets](./socket.md) · [http](./http.md) · [https](./https.md) · [tls](./tls.md)  
2. Java: [http-client](./http-client.md) · [java-net-http](./java-net-http.md) · [connection-pooling](./connection-pooling.md) · [timeouts](./timeouts.md) · [retries](./retries.md)  
3. Failures: [latency](./latency.md) · [connection-failure](./connection-failure.md) · [timeout-failures](./timeout-failures.md) · [retry-storms](./retry-storms.md) · [connection-exhaustion](./connection-exhaustion.md) · [partial-failure](./partial-failure.md)  
4. Practice: [scenarios](./scenarios.md) · [java-backend](./java-backend.md) · [principal-decisions](./principal-decisions.md) · [interview](./interview.md) · [practical/](./practical/)

## One-line PE rule

**Every outbound call needs a timeout budget, a pool limit, and a retry policy that cannot amplify an outage.**
