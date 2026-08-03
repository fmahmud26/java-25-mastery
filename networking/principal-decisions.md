# Principal Engineer Decisions (Networking)

## 1) Timeout budget is mandatory

Every outbound dependency has connect + request timeouts derived from SLOs. No infinite waits.

## 2) Shared clients, capped pools

One `HttpClient` (or pool) per downstream; max concurrency documented; never per-request client.

## 3) Retries are load amplifiers

Default: retry idempotent reads with jitter; POSTs need idempotency or no retry. Circuit break on sustained failure.

## 4) Partial failure has a state model

Timeout ≠ definite failure. Design PENDING/reconcile for money and inventory.

## 5) TLS/cert ops is uptime

Expiry monitoring, staging mTLS drills, private CA rotation runbooks.

## 6) Observe per dependency

RED metrics + timeout/retry counters. Traces across hops.

## Anti-decisions

- Retry all 5xx three times with no jitter  
- 30s timeouts everywhere “to be safe”  
- New HttpClient per call  
- Ignoring DNS TTL during failover  

### Related

[retry-storms.md](./retry-storms.md) · [partial-failure.md](./partial-failure.md) · [interview.md](./interview.md)
