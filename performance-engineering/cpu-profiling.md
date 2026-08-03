# CPU Profiling

Attribute **CPU cycles** to methods/threads — wall time needs a different lens if threads are blocked.

## Measure

```bash
jcmd <pid> JFR.start name=cpu settings=profile duration=60s filename=/tmp/cpu.jfr
# Linux JDK25+: experimental CPU-time events may be enabled in custom .jfc — see JEP 509
# async-profiler: asprof -e cpu -d 60 -f /tmp/cpu.html <pid>
```

Confirm process CPU with `top`/cgroup metrics first.

## Hypothesize

| Flame / hot method | Possible story |
|--------------------|----------------|
| App crypto/JSON | Algorithmic / library use |
| GC threads | Allocation / heap pressure |
| Compiler threads | Warmup / deopt storm |
| `epollWait` etc. | Not CPU — wrong mode |

## Analyze

Separate mutator vs GC vs JIT. Optimize code that owns **your** CPU budget.

## Optimize → Re-measure

Change algorithm → same load → CPU% and p99/throughput. State warm JVM.

### Related

[allocation-profiling.md](./allocation-profiling.md) · [jfr.md](./jfr.md) · [experiments/02-cpu-hotspot.md](./experiments/02-cpu-hotspot.md)
