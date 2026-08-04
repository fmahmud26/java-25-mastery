# Tools Index

| Tool | Role | Doc |
|------|------|-----|
| JFR | Production event profiling | [../jfr.md](../jfr.md) · [java-flight-recorder.md](./java-flight-recorder.md) |
| JMC | Analyze recordings | [../jmc.md](../jmc.md) · [java-mission-control.md](./java-mission-control.md) |
| JMH | Microbenchmarks | [jmh.md](./jmh.md) |
| jcmd | Unified diagnostics | [../jcmd.md](../jcmd.md) · [jcmd.md](./jcmd.md) |
| jstack / Thread.print | Thread dumps | [../jstack.md](../jstack.md) · [jstack.md](./jstack.md) |
| jmap | Legacy dump/histo | [jmap.md](./jmap.md) |
| VisualVM | Dev sampling / dumps | [visualvm.md](./visualvm.md) |
| jconsole | JMX GUI | [jconsole.md](./jconsole.md) |
| async-profiler | CPU/alloc/wall (external) | [async-profiler.md](./async-profiler.md) |

**Prod default:** `jcmd` + JFR + JMC (+ dumps on retention issues). Pair with async-profiler for fast flame graphs. Unified loop: [../jvm-observability.md](../jvm-observability.md).
