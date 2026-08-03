# Answer Framework — PCR-OTDR

Every substantive interview answer should walk this spine. Interviewers score **structure and judgment**, not encyclopedias.

```text
P  Problem      — what are we solving / diagnosing?
C  Context      — constraints, SLOs, scale, ownership
R  Reasoning    — how you think; mechanisms involved
O  Options      — at least two credible paths
T  Trade-offs   — what each option buys and sells
D  Decision     — what you pick now
R  Result       — how you’d know it worked / what you’d watch
```

Say the letters mentally; speak in full sentences.

---

## 1. Problem

Restate in one crisp sentence. If unclear, **ask** (coding: constraints; design: QPS; debug: when started).

| Weak | Strong |
|------|--------|
| “I’ll talk about HashMap.” | “We’re seeing high CPU in request threads correlating with map lookups under a bad key type.” |

---

## 2. Context

Pin the world: traffic, consistency needs, latency SLO, single service vs fleet, JDK version, what you can change.

| Weak | Strong |
|------|--------|
| “In general…” | “Java 25 service, p99 100ms, Postgres primary, VT enabled, pool size 20.” |

---

## 3. Reasoning

Name the **mechanism** (happens-before, resize, GC retention, dual-write). Show you know *why* symptoms appear.

| Weak | Strong |
|------|--------|
| “Use ConcurrentHashMap.” | “Check-then-act on CHM isn’t atomic; `computeIfAbsent` closes the race that double-sends email.” |

---

## 4. Options

Force at least two real options (including “do nothing / buy time”). Prevents tunnel vision.

---

## 5. Trade-offs

Currency: latency, correctness, cost, complexity, operability, time-to-ship.

| Option | Buys | Sells |
|--------|------|-------|
| A | … | … |
| B | … | … |

---

## 6. Decision

Pick one **for the stated context**. “It depends” without a pick is incomplete; “it depends, **so I’d pick X because…**” is fine.

---

## 7. Result

How you validate: metrics, test, canary, error budget, rollback. Interviews love closed loops.

---

## Timed variants

| Format | How to use the spine |
|--------|----------------------|
| Rapid-fire (60–90s) | Problem + Decision + one trade-off |
| Coding (15–20m) | Problem/constraints → brute → optimize → complexity → tests |
| Debug (20–30m) | Problem → Context → hypotheses (Options) → evidence → Decision/fix → Result |
| Design (40–50m) | Full spine; spend most time on Options/Trade-offs |
| Principal (45m) | Spine + **blast radius / migration / standards** |

---

## Worked mini-example (concurrency)

**Problem:** Checkout threads idle on CPU but p99 is 2s.  
**Context:** 200 platform threads; dependency p99 1.5s; no timeouts.  
**Reasoning:** Threads blocked on I/O → pool exhaustion → queueing.  
**Options:** (1) Raise pool (2) Timeouts+CB+bulkhead (3) VT without pool bounds.  
**Trade-offs:** (1) masks and amplifies DB (2) protects system (3) VT≠infinite DB.  
**Decision:** Timeouts + bulkhead + bounded retries; consider VT later with Hikari cap.  
**Result:** Pool active/wait metrics, dependency p99, error budget; load test hung-dep chaos.

---

## Practice drill

Take any question from [java-interview-questions](../java-interview-questions/). Force the seven headings on paper in 5 minutes. Then speak it in 3 minutes. Cut trivia that doesn’t serve Decision/Result.

## Same spine, different templates

| Interview type | Folder template | How PCR-OTDR maps |
|----------------|-----------------|-------------------|
| LLD | Requirements → … → Trade-offs | Problem/Context = requirements; Options = class/API choices; Decision = boundaries |
| System design | Requirements → Capacity → … → Evolution | Context includes numbers; Options = topology; Result = SLOs/metrics |
| Principal | Context → … → Success metrics | Same letters; Result = success metrics + abort signals |
| Debug / scenario-lab | Hypotheses → evidence → fix | Problem/Reasoning heavy; Options = next probes; Result = validation |

Do **not** learn three unrelated scripts — one judgment spine, domain-specific section labels.
