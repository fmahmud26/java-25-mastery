# Principal Engineer Decisions (Networking)

## 1) Timeout budget is mandatory

Every outbound dependency has connect + request timeouts derived from SLOs. No infinite waits. Budgets nest: child timeout < parent remaining budget.

## 2) Shared clients, capped pools

One `HttpClient` (or pool) per downstream; max concurrency documented; never per-request client. Bulkhead noisy neighbors.

## 3) Retries are load amplifiers

Default: retry idempotent reads with jitter; POSTs need idempotency or no retry. Circuit break on sustained failure. Review retry policy like an API change.

## 4) Partial failure has a state model

Timeout ≠ definite failure. Design PENDING/reconcile for money and inventory. See [partial-failure.md](./partial-failure.md).

## 5) TLS/cert ops is uptime

Expiry monitoring, staging mTLS drills, private CA rotation runbooks.

## 6) Observe per dependency

RED metrics + timeout/retry counters + pool wait. Traces across hops. JVM overlay when p99 mysterious: [../performance-engineering/jvm-observability.md](../performance-engineering/jvm-observability.md).

## 7) VT does not remove networking physics

Virtual threads make blocking calls scalable in *threads*, not in *downstream capacity*. Cap in-flight work per dependency.

## Anti-decisions

- Retry all 5xx three times with no jitter  
- 30s timeouts everywhere “to be safe”  
- New HttpClient per call  
- Ignoring DNS TTL during failover  
- Unbounded VT fan-out to a 20-connection peer  

## Design-review questions

1. What is the timeout budget for each hop?  
2. What is retried, how often, with what jitter?  
3. What happens on unknown POST outcome?  
4. Which dashboard shows pool wait and retry amp?  

### Related

[retry-storms.md](./retry-storms.md) · [partial-failure.md](./partial-failure.md) · [interview.md](./interview.md) · [http-client.md](./http-client.md)
