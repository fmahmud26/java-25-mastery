# DNS

## Mental Model

```text
orders.prod.svc → A/AAAA record → IP(s)
Client caches answers (TTL)
Wrong/slow DNS ⇒ “network is broken”
```

## Mechanism

- Resolver (OS / JVM / container) queries recursive resolvers  
- TTL controls cache freshness  
- Load balancers often front multiple A records  
- Headless K8s services / CoreDNS quirks matter in clusters  

```java
InetAddress.getAllByName("payment.internal");
// HttpClient resolves on connect; failures surface as UnknownHostException
```

## Service-to-Service

Service A calls `https://payment.internal/charge` → DNS every cache miss → TCP/TLS to resolved IP. Stale DNS after failover → connection failures until TTL expires.

## Failures

| Failure | Effect |
|---------|--------|
| NXDOMAIN | Hard fail |
| Timeout / flaky resolver | Intermittent `UnknownHostException` |
| Stale cache | Traffic to dead IPs |
| Split-horizon | Works in staging, fails in prod |

## PE Practices

- Prefer platform service discovery (K8s DNS, mesh) with known TTLs  
- Alert on DNS error rates  
- Don’t invent your own ultra-long caches without invalidation  

### Related

[connection-failure.md](./connection-failure.md) · [latency.md](./latency.md) · [scenarios.md](./scenarios.md)
