# Scenario: Virtual Threads Adopted — No Gain (or Worse)

## Context

A blocking HTTP + JDBC service migrates request handling to `newVirtualThreadPerTaskExecutor()`. Expectation: “VT = faster.” After release, RPS flat or down; CPU up on JSON/crypto paths; DB pool wait low. Leadership asks whether to roll back Loom.

## Constraints

- JDK **25** (JEP 491: `synchronized` no longer pins the old way)  
- p99 SLO unchanged; cannot rewrite to reactive this quarter  
- Mix of I/O and CPU stages on the same request path  

## Options

| Option | Approach |
|--------|----------|
| **A. Rollback VT** | Restore sized platform pool |
| **B. VT only for I/O** | Keep VT on request/I/O; bound CPU stages on platform pool |
| **C. “Fix pinning”** | Hunt `synchronized` as if still on JDK 21 |
| **D. Bigger machines** | Buy cores for oversubscribed VT CPU work |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Known behavior | Loses cheap blocked concurrency if I/O was the real ceiling |
| B | Matches Loom economics | Requires clear stage boundaries |
| C | Comfortable JDK 21 lore | Wrong primary diagnosis on 25; wastes cycle |
| D | Short-term headroom | Cost; hides unbounded fan-out |

## Decision

**B.** Treat VT as an **I/O concurrency** tool. Prove I/O-bound fraction with JFR/CPU profiles. Cap CPU fan-out (semaphore ≈ cores). Do not sell VT as a throughput multiplier for CPU work.

On Java 25, still ban **lock-over-I/O** and watch **native/file/class-init** residual pinning — but do not lead with “synchronized pins.”

## Reasoning

VTs do not add cores. Unmount helps while blocked in cooperating JDK I/O; CPU-bound work keeps carriers busy by design. Pool exhaustion and dependency RPS remain hard limits.

## Risks

- Unbounded VT submit overwhelms DB after CPU fix  
- Residual pinning (JNI/FFM, local file I/O) misread as “VT broken”  
- Preview Structured Concurrency used in prod without policy  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | Profile: CPU vs block vs pool wait | — |
| 1 | Bound CPU stages; keep VT on I/O | p99 regresses |
| 2 | JFR residual pinning / native | Pin storms unexplained |
| 3 | Admission control on fan-out | Dep 5xx rise |

## Success metrics

- RPS ≥ baseline at same error budget  
- Carrier busy time explained by CPU stages, not mystery pins  
- DB pool wait within budget under peak admitted concurrency  

Related: [../../virtual-threads/when-vt-do-not-help.md](../../virtual-threads/when-vt-do-not-help.md) · [../../scenario-lab/11-virtual-thread-misuse.md](../../scenario-lab/11-virtual-thread-misuse.md) · [../../experiments/virtual-vs-platform-blocking/](../../experiments/virtual-vs-platform-blocking/)
