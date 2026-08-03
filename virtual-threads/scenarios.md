# Realistic Scenarios (Virtual Threads)

Use these in design reviews and interviews. Each names the bottleneck and whether VT helps.

---

## 1) 10K Concurrent HTTP Requests

**Setup:** REST gateway; each request calls one internal service (50–200 ms).

| Model | Behavior |
|-------|----------|
| Platform pool 200 | Queue builds; 503/timeouts under burst |
| VT per request | 10K blocked VTs cheap; carriers ~CPU count |

**VT helps?** Yes — if downstream and memory can absorb 10K in-flight.  
**Still need:** max in-flight / load shed, timeouts, connection limits.

```java
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    // servlet/container often does this under the hood with VT enabled
}
```

---

## 2) Slow Downstream API

**Setup:** Vendor median 800 ms, p99 5 s; rate limit 200 RPS.

**VT helps?** Only concurrency *waiting* is cheaper. Vendor still caps RPS.  
**Without bulkhead:** 5K VTs × vendor stampede → 429 storm → worse than platform pool of 50.

**Fix:** `Semaphore(200)`, timeouts, circuit breaker, cache, queue.

---

## 3) Database-Heavy Service

**Setup:** Hikari 40; queries 20–40 ms; traffic spike.

**Before VT:** App threads = 200 waiting on pool — OS waste.  
**After VT:** Same 40 active queries; many cheap waiters.

**VT helps throughput?** Only if previously thread-limited *below* DB capacity.  
**Often:** efficiency win, not QPS win. Fix SQL/indexes for QPS.

---

## 4) High-Concurrency REST API

**Setup:** Fan-out: auth cache + DB + pricing HTTP.

**VT pattern:** one VT per request; sequential or structured fan-out with blocking calls.

**Risks:** each hop multiplies connection use; ThreadLocals; no admission control.

**PE checklist:** timeouts per hop, bulkheads per dependency, pool sizing × replicas.

---

## 5) Connection Pool Exhaustion

**Symptoms:** `Connection is not available, request timed out after 30000ms`; DB CPU low.

**Wrong narrative:** “Need more virtual threads.”  
**Right narrative:** pool/query/DB saturation.

**Mitigations:** fail-fast shorter `connectionTimeout`, query SLAs, scale DB or reduce connections via caching, shed load at edge.

---

## 6) Cases VT Does NOT Solve (embedded)

See [when-vt-do-not-help.md](./when-vt-do-not-help.md): CPU crypto, bad SQL, rate limits, lock-over-I/O, OOM from unbounded admission.

---

## Scenario Decision Card

```text
Measure: thread wait vs dependency saturation vs CPU
If thread-wait dominant AND dependency has headroom → adopt VT
Else → fix dependency / CPU / admission first
```

### Related

[experiments.md](./experiments.md) · [downstream-limitations.md](./downstream-limitations.md) · [principal-architecture-decisions.md](./principal-architecture-decisions.md)
