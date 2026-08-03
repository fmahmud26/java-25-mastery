# System Design — Interview Questions (L1–L4)

Clarify → diagram → deep dive → failures → scale.

---

## Level 1 — Junior

### What is horizontal vs vertical scaling?

**Answer:** Vertical = bigger machine. Horizontal = more machines behind a load balancer. Horizontal needs stateless (or externalized state) app tiers and often partitioned data.

### What does a cache do in a web system?

**Answer:** Stores frequently read data closer/faster than the source of truth to cut latency and load. Must define TTL/invalidation and accept a staleness window.

### What is load balancing?

**Answer:** Distributes traffic across instances (round-robin, least-conn, consistent hash). Health checks remove bad nodes. Often L7 for HTTP routing/auth.

---

## Level 2 — Mid-level

### Explain CAP in practical interview terms.

**Answer:** Under network partition, a system cannot simultaneously provide perfect consistency and perfect availability. You choose which to degrade; most product systems mix models by use case (payments vs feeds).

### Cache-aside vs write-through?

**Answer:** Cache-aside: app manages reads/fills; simple, risk of stampede. Write-through: writes update cache+DB together; more consistent cache, higher write latency. Pick based on read/write ratio and consistency needs.

### How do message queues help?

**Answer:** Decouple producers/consumers, buffer spikes, enable async workflows and retries. Trade: eventual processing, need idempotency, lag monitoring.

---

## Level 3 — Senior

### How do you design idempotent payments APIs?

**Answer:** Client `Idempotency-Key` stored with request hash/result; duplicates return first result. Ledger entries unique on key; webhooks de-duped. At-least-once delivery assumed end-to-end.

### Hot key problem — mitigations?

**Answer:** Local/near caches, key splitting, request coalescing, read replicas for that key, rate limits, rethink partition key. Measure: one partition/CPU hot while others idle.

### When is “exactly-once” a red flag answer?

**Answer:** Across distributed systems you usually get at-least-once + idempotent consumers (effectively-once). Claiming broker exactly-once without end-to-end story is incomplete.

---

## Level 4 — Expert

### Production: p99 spiking, DB CPU high, Redis hit rate collapsed after deploy. How do you diagnose and stabilize?

**Answer (structured):**

1. **Symptoms** — p99, error rate, DB CPU, Redis hit %, deploy marker.  
2. **Tools** — metrics, traces, slow query log, Redis `INFO`/keyspace, recent config/TTL changes.  
3. **Hypotheses**  
   - Cache key schema change → mass miss / stampede  
   - TTL aligned → thundering herd  
   - New query path bypassing cache  
   - Connection pool exhaustion amplifying latency  
4. **Stabilize** — rollback/feature flag; raise cache; request coalescing; shed load; emergency TTL jitter.  
5. **Fix** — restore key strategy; stampede lock; warm cache; add SLOs on hit rate.  
6. **Validate** — canary, load test with expiry storms, dashboards for hit rate + DB QPS.

**Common Mistake at L4:** Jumping to “shard the DB” before proving the cache miss / stampede root cause.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Availability vs reliability? |
| 2 | Consistent hashing purpose? |
| 3 | Design URL shortener read path |
| 4 | Retry storm taking down dependency — diagnosis |
