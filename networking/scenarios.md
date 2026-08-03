# Service-to-Service Scenarios

## 1) Checkout orchestration

```text
Order Service
  → GET  inventory /stock (timeout 80ms, retry GET ×2 with jitter)
  → POST payment /charge (timeout 200ms, idempotency key, retry only connect failures)
  → POST notify /email (async via outbox — don’t block checkout)
```

Partial failure: payment timeout → mark `PAYMENT_UNKNOWN` → reconcile job.

---

## 2) Inventory under load

Many order pods call inventory. Pool per host capped; 429 → backoff. No retry storm on `503` without breaker.

---

## 3) Payment latency spike

TLS + cold pool after deploy → p99 spike. Warm pool / keep client singleton; measure connect vs request time.

---

## 4) DNS failover

Payment VIP IP changes; clients cache old A record → connection failures until TTL. Detect via `UnknownHost` / connect errors spike; lower TTL or use platform discovery.

---

## 5) Fan-out amplification

One user request → 8 HTTP calls. Timeouts must nest; retries only on leaf idempotent reads.

### Related

[java-backend.md](./java-backend.md) · [retry-storms.md](./retry-storms.md) · [partial-failure.md](./partial-failure.md)
