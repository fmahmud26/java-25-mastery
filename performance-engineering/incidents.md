# Incidents (Performance Lab)

Each follows Measure → Hypothesize → Experiment → Analyze → Optimize → Re-measure.

---

## 1) p99 Latency Spikes Every Few Minutes

**Measure:** Latency chart + GC log + JFR 5 min.  
**Hypothesis:** Mixed/Full GC or safepoint sync.  
**Experiment:** Correlate timestamps; if GC, check live set/alloc; if not, lock/IO.  
**Optimize:** Fix root (heap/cache/query), not random GC flags.  
**Re-measure:** p99 under same RPS for 30 min.

---

## 2) High CPU, Modest Traffic

**Measure:** cgroup CPU; JFR CPU / async-profiler.  
**Hypothesis:** Hot loop / regex / crypto / JSON; or GC/compiler.  
**Experiment:** Confirm hot frames are app code.  
**Optimize:** Algorithm or cache with bound.  
**Re-measure:** CPU% and RPS ceiling.

---

## 3) Throughput Flat Despite More Pods

**Measure:** RPS per pod, dependency latency, pool metrics.  
**Hypothesis:** Downstream limit or lock serialization (contention).  
**Experiment:** JFR monitors + thread dumps; dependency RPS.  
**Optimize:** Remove global lock or raise **downstream** capacity deliberately.  
**Re-measure:** Scaling curve (RPS vs replicas).

---

## 4) GC Thrash After Deploy

**Measure:** Alloc rate, young GC/min, after-GC occupancy.  
**Hypothesis:** New alloc churn or retained cache.  
**Experiment:** Alloc profile + dump if occupancy climbs.  
**Optimize:** Code-level alloc/retention fix.  
**Re-measure:** GC frequency + p99.

---

## 5) “Optimization” Made Tails Worse

**Measure:** Before/after histograms.  
**Hypothesis:** Mean improved; batching/locks hurt p99.  
**Experiment:** Disable change on canary.  
**Optimize:** Revert or redesign for tails.  
**Re-measure:** Gate on p99 SLO, not mean.

---

## Incident card

```text
Workload: 
Baseline metric:
Evidence artifact: (jfr / dump / gc.log / dumps)
Hypothesis:
Single change:
Result:
Keep/Revert:
```

### Related

[scientific-method.md](./scientific-method.md) · [tail-latency.md](./tail-latency.md) · [experiments/README.md](./experiments/README.md)
