# Scenario: CPU Saturation — Hot Path, Few Cores

## Context

2–4 core service nodes peg at 95–100% CPU. Latency rises; GC calm; heap after GC flat. Suspect “need more threads” or “enable VT.”

## Constraints

- Horizontal scale limited this week  
- Must identify *which* frames burn CPU before buying hardware  

## Options

| Option | Approach |
|--------|----------|
| **A. Add threads / VT** | Raise concurrency |
| **B. Profile → fix hot code** | JFR / async-profiler; fix algorithm / alloc |
| **C. Cache / avoid work** | Memoize, precompute, cheaper codec |
| **D. Scale out** | More pods after proving work is necessary |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Illusion of progress | More context switch; same cores; often worse |
| B | Real capacity | Needs evidence |
| C | Cheap wins | Stale data risk |
| D | Headroom | Cost; multiplies waste if work is buggy |

## Decision

**B → C → D. Never A first for CPU-bound symptoms.** VT does not add cores ([virtual-threads-no-gain](./virtual-threads-no-gain.md)).

## Reasoning

RUNNABLE stacks recurring in the same frames + calm GC ⇒ application compute. Prove with repeated dumps + CPU profile before architecture talk.

## Risks

- Mis-attributing GC or lock wait as CPU  
- Optimizing a 5% frame while a library regex is 70%  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | Rule out GC / pool wait | — |
| 1 | Flame graph; fix top frames | No dominant frame → scale-out spike |
| 2 | Cache / batch | Correctness fail |
| 3 | Scale out | Cost gate |

## Success metrics

- CPU ↓ at same RPS or RPS ↑ at same CPU  
- p99 improves without error-budget burn  

Related: [../../scenario-lab/01-high-cpu.md](../../scenario-lab/01-high-cpu.md) · [../../performance-engineering/cpu-profiling.md](../../performance-engineering/cpu-profiling.md)
