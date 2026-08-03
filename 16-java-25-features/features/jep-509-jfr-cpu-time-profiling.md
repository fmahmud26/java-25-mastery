# JEP 509 — JFR CPU-Time Profiling (Experimental)

| | |
|--|--|
| **JEP** | [509](https://openjdk.org/jeps/509) |
| **Status** | **Experimental** — JDK 25 |
| **Platform** | **Linux** (CPU-timer based) |
| **Depends on** | Cooperative sampling (JEP 518) |

## Purpose

Record more accurate **CPU-time** profiles in JFR using Linux CPU timers (samples by CPU time, not only wall-clock execution sampling).

## Problem Solved

`jdk.ExecutionSample`-style approximations miss native CPU, miss some threads, and can under-represent true CPU hot methods — especially when much work is in native code.

## Previous Approach

JFR execution sampler; or third-party tools (e.g. async-profiler) using unsupported internals (crash risk).

## New Approach

Experimental events such as `jdk.CPUTimeSample` (and lost-sample accounting). Enable in recordings:

```bash
java -XX:StartFlightRecording=jdk.CPUTimeSample#enabled=true,filename=profile.jfr ...
```

```bash
jfr view cpu-time-hot-methods profile.jfr
```

Per JEP: events are `@Experimental` but **do not** require `-XX:+UnlockExperimentalVMOptions`.

## Syntax / API

JFR event configuration / `jfr` / JMC — Linux only for this mechanism.

## Internal Behavior

Linux CPU timer signals sampling; cooperative stack walking; throttle controls overhead (`throttle` period or rate). Can profile continuous low-rate production if overhead acceptable.

## Production Example

CPU-bound service on Linux: 4-minute `jcmd JFR.start` with CPU-time enabled; flame graph in JMC; optimize methods that actually burn CPU (vs IO-wait heavy methods).

## Limitations

- Experimental — may change.  
- Linux-focused in this JEP.  
- Higher throttle ⇒ more overhead.  
- Still statistical sampling.

## Migration Considerations

Use in staging first; document Linux-only. Keep execution samples if you need cross-platform wall approximation.

## Interview Questions

1. CPU-time vs wall-time profiling?  
2. Why experimental?  
3. Why safer than unsupported profilers?  
4. Relation to JEP 518?

### Related

[jep-518-jfr-cooperative-sampling.md](./jep-518-jfr-cooperative-sampling.md) · [../experimental-features.md](../experimental-features.md)
