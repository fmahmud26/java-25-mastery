# JFR — Java Flight Recorder

Low-overhead HotSpot event recorder — default production profiling tool on modern JDKs.

## Mental Model

```text
Enable events → ring buffer / file → dump .jfr → JMC / jfr print
Correlate event timestamps with load-test or incident window
```

## Measure

```bash
jcmd <pid> JFR.start name=lab settings=profile duration=60s filename=/tmp/lab.jfr
jcmd <pid> JFR.check
jcmd <pid> JFR.dump name=lab filename=/tmp/lab.jfr
jcmd <pid> JFR.stop name=lab

java -XX:StartFlightRecording=filename=/tmp/start.jfr,duration=2m,settings=profile -jar app.jar
```

`default` = lighter; `profile` = more samples (still usually modest overhead — **verify** on your service). Custom `.jfc` for focused event sets.

## Hypothesize with events

| Area | Events / views (conceptual) |
|------|------------------------------|
| CPU | Execution samples; CPU-time sampling on Linux (JEP 509 — experimental; validate on JDK 25 build) |
| Alloc | Object allocation / TLAB |
| GC | GC phase, heap summary |
| Locks | Java monitor blocked |
| I/O | Socket/file read/write |
| Threads | Thread park / virtual thread related events (where available) |

## Analyze

Open in [JMC](./jmc.md) or `jfr print` / `jfr view`. Align timestamps with load-test spikes. Pair with [async-profiler](./tools/async-profiler.md) when you need a quick flame graph.

## Production Scenario — p99 every GC

Recording shows GC pauses lining up with latency spikes; allocation pressure in one package. Fix alloc first; collector swap later.

## Production hygiene

- Timed recordings; disk budgets  
- PII in http headers/sessions — scrub policies  
- Don’t leave ultra-verbose custom settings forever  
- One recording name per incident to avoid clobbering  

## When Not Enough Alone

Retention leaks need heap dumps; distributed causality needs traces; pure micro A/B needs JMH.

### Related

[tools/java-flight-recorder.md](./tools/java-flight-recorder.md) · [jmc.md](./jmc.md) · [cpu-profiling.md](./cpu-profiling.md) · [jvm-observability.md](./jvm-observability.md)
