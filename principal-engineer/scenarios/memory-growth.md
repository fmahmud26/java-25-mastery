# Scenario: Retained Heap Growth (Leak or Unbounded Cache)

## Context

RSS and heap used climb over days. Full GC recovers little. Canary-only feature ships a new cache. OOM risk before weekend traffic.

## Constraints

- Cannot restart daily as a “fix”  
- Must distinguish **allocation pressure** vs **retention**  

## Options

| Option | Approach |
|--------|----------|
| **A. Raise `-Xmx`** | Buy time |
| **B. Dump → path-to-GC-roots** | Prove retainer |
| **C. Bound cache / TTL / size** | Product-safe limits |
| **D. SoftReference cache** | “GC will save us” |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Hours/days | Masks leak; bigger pause risk |
| B | Truth | Needs dump discipline |
| C | Stable RSS | Cache miss cost |
| D | Feels clever | Unpredictable clear → stampede |

## Decision

**B then C.** Chart heap **after GC**. Rising baseline ⇒ retention. Flat after GC + high alloc ⇒ allocation/GC story ([gc-pause-growth](./gc-pause-growth.md)). Prefer explicit bounds over SoftReference as primary control.

## Reasoning

A Java “leak” is unintended strong reachability. PE answer names root (static map, ThreadLocal, listener, unbounded cache) and a kill switch.

## Risks

- Dumping production without runbook  
- Fixing a symptom (Xmx) while retainer remains  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | After-GC heap chart + histo | — |
| 1 | Dump on canary; identify dominator | Wrong process |
| 2 | Bound + metric on size | Hit rate cliff |
| 3 | Remove retainer; soak test | Growth resumes |

## Success metrics

- After-GC heap stable under soak  
- Cache size capped; eviction metrics live  

Related: [../../scenario-lab/02-memory-leak.md](../../scenario-lab/02-memory-leak.md) · [../../memory-management/memory-leaks.md](../../memory-management/memory-leaks.md)
