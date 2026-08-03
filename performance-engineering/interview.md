# Interview — Java Performance Engineering

## Must recite

```text
Measure → Hypothesize → Experiment → Analyze → Optimize → Re-measure
```

Never: “X is faster” without workload, env, metric, method.

---

## Tool picker

| Symptom | First tools |
|---------|-------------|
| High CPU | JFR / async-profiler CPU |
| High alloc / GC | JFR alloc + GC logs |
| Retention / OOM | Heap dump |
| Blocked latency | jstack ×3 + JFR monitors |
| Micro compare | JMH |
| Service SLO | Load test + JFR |

---

## Concepts

**Throughput vs latency vs tail?** Work/time vs per-op time vs high percentiles.  

**Why JMH?** Warmup, forks, DCE.  

**JFR vs instrumentation profilers?** Low-overhead events vs heavier exact counts.  

**Contention vs CPU?** BLOCKED can tank p99 with idle CPU.  

**GC pressure?** Alloc rate + pauses + live set — not “GC bad.”  

---

## Scenario prompt

“p99 is 2s every 5 minutes.”  
Answer: correlate GC/safepoint/locks with timestamps; one hypothesis; one experiment; re-measure at same RPS.

---

## Claim hygiene (say this)

“Under workload W on env E, metric M went A→B measured with T (n runs). Secondary metrics …”

### Related

[scientific-method.md](./scientific-method.md) · [incidents.md](./incidents.md) · [experiments/README.md](./experiments/README.md)
