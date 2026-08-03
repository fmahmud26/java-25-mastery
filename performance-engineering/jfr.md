# JFR — Java Flight Recorder

Low-overhead HotSpot event recorder — default production profiling tool on modern JDKs.

## Measure

```bash
jcmd <pid> JFR.start name=lab settings=profile duration=60s filename=/tmp/lab.jfr
jcmd <pid> JFR.check
jcmd <pid> JFR.dump name=lab filename=/tmp/lab.jfr
jcmd <pid> JFR.stop name=lab

java -XX:StartFlightRecording=filename=/tmp/start.jfr,duration=2m,settings=profile -jar app.jar
```

`default` = lighter; `profile` = more samples (still usually modest overhead — **verify** on your service).

## Hypothesize with events

| Area | Events / views (conceptual) |
|------|------------------------------|
| CPU | Execution / CPU-time samples (CPU-time experimental on Linux — JDK 25 JEP 509) |
| Alloc | Object allocation / TLAB |
| GC | GC phase, heap summary |
| Locks | Java monitor blocked |
| I/O | Socket/file read/write |

## Analyze

Open in [JMC](./jmc.md) or `jfr print` / `jfr view`. Align timestamps with load-test spikes.

## Production hygiene

- Timed recordings; disk budgets  
- PII in http headers/sessions — scrub policies  
- Don’t leave ultra-verbose custom settings forever  

### Related

[tools/java-flight-recorder.md](./tools/java-flight-recorder.md) · [jmc.md](./jmc.md) · [cpu-profiling.md](./cpu-profiling.md)
