# Virtual Threads — Cheat Sheet

**Sources:** [../virtual-threads/README.md](../virtual-threads/README.md) · [thread-pinning](../virtual-threads/thread-pinning.md) · [synchronization-and-vt](../virtual-threads/synchronization-and-vt.md) · [database-connection-pools](../virtual-threads/database-connection-pools.md) · [when-vt-do-not-help](../virtual-threads/when-vt-do-not-help.md) · [java-interview-questions/virtual-threads](../java-interview-questions/virtual-threads/) · [experiments/virtual-vs-platform-blocking](../experiments/virtual-vs-platform-blocking/)

## Model

```text
Many virtual threads ↔ few carrier platform threads
Blocking wait → unmount (typically) → carrier free for others
```

PE rule from chapter: **VT amplify waiting concurrency; bottlenecks move to pools/CPU/deps.**

## When to use / not

| Use VT | Don’t expect wins |
|--------|-------------------|
| High concurrency **blocking** I/O | Pure **CPU** bound work |
| Thread-per-request style servers | “Unlimited” DB/HTTP concurrency |

Proof style: [virtual-vs-platform-blocking experiment](../experiments/virtual-vs-platform-blocking/) · [when-vt-do-not-help](../virtual-threads/when-vt-do-not-help.md)

## Pinning (version-aware)

| Era | `synchronized` + VT |
|-----|---------------------|
| Early Loom / JDK 21 teaching | Pinning was a major hazard |
| **Java 24+ / JEP 491** (incl. path to 25) | `synchronized` **no longer pins** the old way |

Still: **don’t hold locks across I/O**; residual pinning (JNI/FFM, local file I/O, class-init). Details: [thread-pinning.md](../virtual-threads/thread-pinning.md) · bank [Principal Q](../java-interview-questions/concurrency/q07-pinning-synchronized.md)

## Pools still scarce

| Resource | Rule |
|----------|------|
| JDBC / HTTP pools | Size from **dependency capacity**, not VT count |
| Admission | Bound accepts / shed load |

[database-connection-pools](../virtual-threads/database-connection-pools.md) · [q02 VT+JDBC](../java-interview-questions/virtual-threads/q02-vt-and-jdbc-pool.md)

## Context

ThreadLocal leaks amplify at VT scale → prefer Scoped Values (Java 25 final JEP 506) where appropriate — [VT ThreadLocal Q](../java-interview-questions/virtual-threads/q03-threadlocal-inheritance.md) · [JEP 506](../16-java-25-features/features/jep-506-scoped-values.md)

## Structured concurrency

**Preview** on Java 25 (JEP 505) — [java-25 cheat](./java-25.md) · [bank](../java-interview-questions/virtual-threads/q04-structured-concurrency-preview.md)
