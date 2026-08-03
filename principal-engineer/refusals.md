# Hard Refusals — Principal Stances

Principals are defined by what they **will not** ship. These are signed technical refusals for this portfolio — not balance-sheet theater.

Each refusal: **stance → why → what we do instead → abort/exception**.

---

## 1. No microservices to “fix” a God table

**Refuse:** Splitting checkout into 8 services while `orders` remains one write hotspot.

**Why:** Network hops and dual-writes do not remove row contention; they distribute join pain and operational blast radius.

**Instead:** Find the bottleneck (dump/metrics). Scale the monolith read path; async non-critical work; shard/cell only when write forecast forces it.

**Exception:** Independent deploy/scale ownership already proven *and* data plane can split cleanly.

Evidence pattern: [scenarios/scale-100x.md](./scenarios/scale-100x.md) · [scenarios/microservices-not-required.md](./scenarios/microservices-not-required.md)

---

## 2. No “virtual threads = faster” migrations

**Refuse:** Enabling VT fleet-wide without proving I/O-bound work and sizing scarce pools.

**Why:** Local evidence: blocking batch VT wallMs=35 vs platform-16 wallMs=1266 ([experiments/virtual-vs-platform-blocking](../experiments/virtual-vs-platform-blocking/)); VT does not add cores. Unbounded VT + Hikari 10 → pool wait cliffs.

**Instead:** VT on blocking request paths; CPU on bounded platform pools; admit ≤ pool×utilization; JFR residual pinning only as secondary.

**Exception:** Measured thread-exhaustion on blocking I/O with headroom on DB/HTTP pools.

ADR: [portfolio/adr-001-virtual-threads-adoption.md](./portfolio/adr-001-virtual-threads-adoption.md)

---

## 3. No dual-write “for now” without a delete date

**Refuse:** Writing DB + publish Kafka in two steps as a permanent design.

**Why:** Partial failure creates silent inconsistency (order exists, fulfillment never runs).

**Instead:** Outbox/CDC in the same transaction; publisher with lease/retry/dead-letter; lag SLO.

**Exception:** None for money/inventory. Ephemeral UX events may stay in-process.

ADR: [portfolio/adr-003-outbox-not-dual-write.md](./portfolio/adr-003-outbox-not-dual-write.md) · project [08](../real-world-projects/08-notification-outbox/)

---

## 4. No inventing payment success on PSP timeout

**Refuse:** Mapping HTTP timeout → `CAPTURED` (or auto-retry charge) without reconcile.

**Why:** Provider may have captured; client retry without idempotency double-charges.

**Instead:** `PENDING` + idempotency key + webhook/reconcile; never charge twice for one key.

ADR: [portfolio/adr-002-payment-unknown-outcome.md](./portfolio/adr-002-payment-unknown-outcome.md) · project [07](../real-world-projects/07-payment-orchestrator/)

---

## 5. No collector shopping before alloc/live-set proof

**Refuse:** “p99 is bad → switch to ZGC today” as first move.

**Why:** Long pauses are a symptom. Churn vs retention need different fixes. Blind swaps hide leaks.

**Instead:** After-GC heap chart + alloc rate + JFR; fix churn/leak; then bakeoff.

**Exception:** Controlled bakeoff after evidence, one flag change, with abort metrics.

Card: [scenarios/gc-pause-growth.md](./scenarios/gc-pause-growth.md)

---

## 6. No SoftReference unbounded caches as the primary bound

**Refuse:** “GC will evict when memory is tight” as capacity planning.

**Why:** Eviction is unpredictable → stampede; Soft refs are not an SLO control.

**Instead:** Explicit max size + TTL + singleflight; metrics on size/hit/evict.

Card: [scenarios/memory-growth.md](./scenarios/memory-growth.md)

---

## 7. No active-active ledger without conflict policy

**Refuse:** Multi-region active-active for money without fencing/merge rules.

**Why:** You chose AP for writes and didn’t admit it; double-spend windows appear under partition.

**Instead:** Single-writer region + failover; or explicit CRDT/merge only for non-ledger data.

---

## 8. No preview APIs in production without a board exception

**Refuse:** Structured Concurrency (preview on Java 25) as a default production dependency.

**Why:** API can change; silent `--enable-preview` is an ops hazard.

**Instead:** Stable `newVirtualThreadPerTaskExecutor()` + disciplined Futures; preview in labs only.

---

## How to use in interviews

Pick **two refusals** and defend with numbers + failure mode + alternative. If you cannot name what you’d kill, you are not speaking as a Principal yet.
