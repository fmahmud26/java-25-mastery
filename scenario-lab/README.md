# Scenario Lab — Incident Investigations

Principal-track **incident drills**. Each file is a production-shaped mystery: symptoms first, root cause last.

## Do not spoil root cause early

Practice like an on-call interview:

1. Read **Incident → Symptoms → Environment → Metrics → Logs** only.
2. Form your own hypotheses **before** scrolling to Investigation / Root Cause.
3. Answer **Questions** out loud (or on paper).
4. Then work **Investigation** step-by-step with tools.
5. Only then open **Root Cause → Resolution → Prevention → Principal Engineer Discussion**.

Spoiling yourself trains recall, not judgment. Interviewers care how you **narrow**, not whether you guess the keyword first.

## Structure (every scenario)

| Section | Purpose |
|---------|---------|
| Incident | Short narrative |
| Symptoms | What operators see |
| Environment | JDK 25 + illustrative capacity (labeled) |
| Metrics | Numbers that constrain the story |
| Logs | Sample lines — partial evidence |
| Initial Hypotheses | 3–5 options; **none marked correct** |
| Questions | Interview-style probes |
| Investigation | Ordered tool path (`jcmd`, `jstack`, JFR, GC logs, dumps, metrics) |
| Root Cause | After evidence only |
| Resolution | Immediate + durable fix |
| Prevention | Alerts, tests, design |
| Principal Engineer Discussion | Trade-offs, redesign |

## How to practice

- **Timed pass (20–30 min):** hypotheses + first three investigation steps without peeking.
- **Tool pass:** name exact commands (`jcmd <pid> Thread.print`, JFR profile, heap histo) — see [performance-engineering/tools](../performance-engineering/tools/).
- **Counterfactual:** “What if traffic ×100?” “What if this is a container with a CPU quota?”
- **Cross-link:** when stuck, open the related chapter; do not jump to Root Cause.
- Pair with [principal-engineer/scenarios/](../principal-engineer/scenarios/) for shorter decision cards, then return here for full investigations.
- Prep formats: [../interview-prep/formats/](../interview-prep/formats/) · Answer spine: [../interview-prep/answer-framework.md](../interview-prep/answer-framework.md)

## Related material

| Resource | Why |
|----------|-----|
| [principal-engineer/scenarios/](../principal-engineer/scenarios/) | Compact Staff/Principal decision cards |
| [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) | Quality bar: reasoning > memorization |
| [performance-engineering/tools/](../performance-engineering/tools/) | `jcmd`, `jstack`, JFR, JMC, `jmap`, … |
| [performance-engineering/](../performance-engineering/) | CPU/memory/GC/thread profiling chapters |
| [concurrency/](../concurrency/), [virtual-threads/](../virtual-threads/), [garbage-collection/](../garbage-collection/), [jvm-internals/](../jvm-internals/) | Depth behind the incidents |

**Prod default diagnostic stack:** `jcmd` + **JFR** + **JMC** (+ async-profiler when needed).

## Scenario index

| # | File | Theme |
|---|------|--------|
| 01 | [01-high-cpu.md](./01-high-cpu.md) | Sustained CPU saturation |
| 02 | [02-memory-leak.md](./02-memory-leak.md) | Heap / retained set growth |
| 03 | [03-high-gc.md](./03-high-gc.md) | Allocation / GC pressure |
| 04 | [04-thread-starvation.md](./04-thread-starvation.md) | Pool starvation |
| 05 | [05-deadlock.md](./05-deadlock.md) | Lock / monitor deadlock |
| 06 | [06-database-pool-exhaustion.md](./06-database-pool-exhaustion.md) | JDBC pool exhausted |
| 07 | [07-high-latency.md](./07-high-latency.md) | Latency cliff |
| 08 | [08-duplicate-payment.md](./08-duplicate-payment.md) | Duplicate side effects |
| 09 | [09-race-condition.md](./09-race-condition.md) | Lost update / check-then-act |
| 10 | [10-kafka-consumer-lag.md](./10-kafka-consumer-lag.md) | Consumer lag |
| 11 | [11-virtual-thread-misuse.md](./11-virtual-thread-misuse.md) | Virtual thread misuse |
| 12 | [12-cache-stampede.md](./12-cache-stampede.md) | Cache stampede |
| 13 | [13-service-timeout.md](./13-service-timeout.md) | Cascading timeouts |
| 14 | [14-connection-leak.md](./14-connection-leak.md) | Connection / resource leak |
| 15 | [15-production-outage.md](./15-production-outage.md) | Multi-factor outage |

Tone: senior/staff interview exercise. Nuanced. Prefer evidence over slogans (no “virtual threads are always faster”).
