# CPU Profiling

Attribute **CPU cycles** to methods/threads — wall time needs a different lens if threads are blocked.

## Mental Model

```text
High container CPU ≠ always your Java code
  → mutator vs GC vs JIT vs other processes
  → then attribute mutator CPU to methods
```

## Measure

```bash
jcmd <pid> JFR.start name=cpu settings=profile duration=60s filename=/tmp/cpu.jfr
# Linux JDK25+: experimental CPU-time events may be enabled in custom .jfc — see JEP 509
# async-profiler: asprof -e cpu -d 60 -f /tmp/cpu.html <pid>
```

Confirm process CPU with `top`/cgroup metrics first. If CPU is low but latency high, switch to wall / traces / pool wait — see [profiling.md](./profiling.md).

## Hypothesize

| Flame / hot method | Possible story |
|--------------------|----------------|
| App crypto/JSON | Algorithmic / library use |
| GC threads | Allocation / heap pressure |
| Compiler threads | Warmup / deopt storm |
| `epollWait` etc. | Not CPU — wrong mode |

## Production Scenario — JIT storm after deploy

CPU spike 10 minutes post-release; compiler threads hot; latency then settles. Warmup policy / reserve capacity during bake; avoid declaring “code regression” too early.

## Analyze

Separate mutator vs GC vs JIT. Optimize code that owns **your** CPU budget on the SLO path (confirm with traces).

## Optimize → Re-measure

Change algorithm → same load → CPU% and p99/throughput. State warm JVM. Document machine and JDK.

### Related

[allocation-profiling.md](./allocation-profiling.md) · [jfr.md](./jfr.md) · [tools/async-profiler.md](./tools/async-profiler.md) · [experiments/02-cpu-hotspot.md](./experiments/02-cpu-hotspot.md)
