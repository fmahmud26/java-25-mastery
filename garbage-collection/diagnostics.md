# Diagnostics: JFR, JMC, jcmd, GC Logs, Heap Analysis

Evidence toolkit for GC and latency incidents.

## Mental Model

```text
GC logs  → what collections happened and how long
JFR/JMC  → timeline + allocation + pause detail
jcmd     → live heap / histogram / dump triggers
Heap dump → retention when live set is the story
```

## JFR / JMC

```bash
java -XX:StartFlightRecording=duration=120s,filename=/tmp/app.jfr,settings=profile ...

jcmd <pid> JFR.start name=gcinc settings=profile
jcmd <pid> JFR.dump name=gcinc filename=/tmp/gcinc.jfr
```

In **JDK Mission Control**: GC charts, pause durations, allocation pressure, safepoints. Use the same window as the latency spike.

## jcmd

```bash
jcmd <pid> GC.heap_info
jcmd <pid> GC.run_finalization          # rarely useful; avoid GC.run in prod folklore
jcmd <pid> GC.heap_dump /tmp/heap.hprof
jcmd <pid> GC.class_histogram
jcmd <pid> Thread.print
jcmd <pid> VM.native_memory summary     # if NMT enabled
```

## GC Logs

See [gc-logs.md](./gc-logs.md). Always-on rotated `gc*` logs are cheap insurance.

## Heap Analysis

When after-GC heap rises or OOM: dump → MAT dominator / path-to-root. That is a **retention** investigation — GC is correctly keeping live objects.

## Suggested Order (latency spike)

1. Confirm spike timestamp  
2. GC log + safepoint log in that window  
3. JFR if available  
4. If occupancy climbing → histogram / dump  
5. Only then change flags/collector  

Unified JVM loop (metrics + traces + JFR): [../performance-engineering/jvm-observability.md](../performance-engineering/jvm-observability.md). Scenario drill: [../scenario-lab/03-high-gc.md](../scenario-lab/03-high-gc.md).

### Related

[incidents.md](./incidents.md) · [pause-time.md](./pause-time.md) · [allocation-rate.md](./allocation-rate.md) · [trade-offs.md](./trade-offs.md)
