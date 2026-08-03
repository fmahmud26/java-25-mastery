# Principal Engineer Discussion (Memory)

Decisions and review standards — not flag folklore.

## Decision 1 — Leak vs Capacity

Before raising `-Xmx`, answer: **Is after-GC live set growing at constant traffic?**  
Yes → fix retention. No → capacity/live-set design (cache size, features).

## Decision 2 — Total Memory Budget

```text
Container limit ≥ heap + metaspace + code cache + stacks×threads + direct/native + headroom
```

Ship a spreadsheet or runbook. `-Xmx` alone is incomplete.

## Decision 3 — Caches Are Product Surface

Every cache needs: max size/weight, TTL/TTI policy, metrics (size, hit rate, evictions), owner, overload behavior. Soft references alone are not a policy.

## Decision 4 — Dump Discipline

- `HeapDumpOnOutOfMemoryError` in production (disk budget + PII controls)  
- On-call knows MAT basics: dominator + path to root  
- Dumps are sensitive artifacts — access control  

## Decision 5 — ThreadLocal / Request Context

Prefer explicit parameters / `ScopedValue` where suitable. If `ThreadLocal`, mandate `remove()` in framework filters. Pooled threads make leaks deterministic.

## Decision 6 — Classloader / Plugin Systems

Hot reload requires proof that old loaders become unreachable. Metaspace dashboards after redeploy are mandatory for app servers/plugins.

## Decision 7 — Large Payloads

Edge limits + streaming defaults. In-memory “load whole blob” needs an ADR and a size cap.

## Anti-Decisions

- Infinite `-Xmx` growth as the “fix”  
- Disabling heap dumps “to save disk” with no alternative  
- SoftReference cache without metrics  
- Ignoring container OOMKill because Java heap MXBean looked fine  

## Review Questions (PR / design)

1. What retains this object after the request ends?  
2. What is the maximum retained size under worst cardinality?  
3. Which metric pages at 80%?  
4. Which OOM message would we see if this fails?  

### Related

[investigation.md](./investigation.md) · [incidents.md](./incidents.md) · [object-retention.md](./object-retention.md)
