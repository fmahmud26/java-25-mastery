# async-profiler

Low-overhead sampling profiler (external, Async-profiler / async-profiler project) — excellent companion to **JFR** for CPU, allocation, and wall-clock views.

## Mental Model

```text
JFR  = rich HotSpot events + timeline (GC, locks, I/O, …)
async-profiler = sharp flame graphs (CPU / alloc / wall) with small overhead
```

Use both: metrics/traces → suspect resource → profiler → code attribution → experiment.

## Measure (illustrative)

```bash
# CPU flame graph (HTML) — tool must be installed separately
asprof -e cpu -d 60 -f /tmp/cpu.html <pid>

# Allocations
asprof -e alloc -d 60 -f /tmp/alloc.html <pid>

# Wall-clock (includes blocked time — different from CPU)
asprof -e wall -d 60 -f /tmp/wall.html <pid>
```

Exact CLI flags evolve — check your installed version. Prefer short, incident-scoped captures in production.

## When Prefer async-profiler

| Situation | Why |
|-----------|-----|
| Need a flame graph fast | HTML output is immediate |
| CPU attribution on Linux | Mature sampling story |
| Compare CPU vs wall | Separates “busy” vs “waiting” |

## When Prefer JFR

| Situation | Why |
|-----------|-----|
| GC / monitor / I/O timeline | Event model richer |
| Always-on / dump on demand | Built into HotSpot; `jcmd` |
| Correlating multiple event types | One recording |

JDK 25 also improves CPU-time profiling via JFR (JEP 509 — experimental CPU-time sampler on Linux): validate on your build before relying on it exclusively.

## Production Hygiene

- Same as JFR: timed runs, disk budgets, treat profiles as sensitive  
- Coordinate with load so the profile covers the symptom window  
- Do not optimize a method that is hot in CPU but off the SLO path — check traces  

## Claim Template

“Wall profile showed 48% time in `pool.await`; CPU profile quiet → not a CPU bug. Raised pool + fixed slow query; p99 800→90ms under same RPS.”

### Related

[../jfr.md](../jfr.md) · [../cpu-profiling.md](../cpu-profiling.md) · [../jvm-observability.md](../jvm-observability.md) · [README.md](./README.md)
