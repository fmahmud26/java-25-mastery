# Interview — Networking for Java Backends

## Stack

DNS → TCP → TLS → HTTP. Java: `HttpClient`, sockets for custom protocols.

---

### Timeouts?

Connect vs request/read; nest under caller budget. Missing timeouts → cascade.

---

### Retries?

Only transient + safe. Idempotency keys for POST. Backoff+jitter. Avoid storms.

---

### Connection pooling?

Reuse TCP/TLS; share `HttpClient`. Exhaustion from unbounded clients/retries.

---

### Partial failure?

Timeout leaves unknown outcome — design reconcile/idempotent replay.

---

### HTTPS/TLS?

Encrypt + authenticate server; mTLS for service identity; handshake cost amortized by pooling.

---

### Scenario

“Payment p99 spike after deploy” → cold pools/TLS? DNS? downstream slow? Show how you’d measure connect vs total time.

**PE line:** budgets, caps, safe retries, explicit uncertainty.

### Related

[README.md](./README.md) · [scenarios.md](./scenarios.md) · [principal-decisions.md](./principal-decisions.md)
