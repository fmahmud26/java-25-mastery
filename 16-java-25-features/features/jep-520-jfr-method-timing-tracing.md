# JEP 520 — JFR Method Timing & Tracing

| | |
|--|--|
| **JEP** | [520](https://openjdk.org/jeps/520) |
| **Status** | **Final** (JDK / JFR) — JDK 25 |

## Purpose

Enhance JFR with improved **method timing and tracing** events/views for performance analysis.

## Problem Solved

Developers need first-party, low-overhead ways to see which methods consume time / to trace execution without always attaching heavy external agents.

## Previous Approach

Execution samples, custom JFR events, third-party profilers, or instrumenting bytecode manually.

## New Approach

Additional JFR method timing/tracing capabilities delivered in JDK 25 (configure via JFR settings / `jfr` tool / JMC). See [JEP 520](https://openjdk.org/jeps/520) for event names and configuration details for your build.

## Syntax / API

JFR configuration (`.jfc`), `-XX:StartFlightRecording=...`, `jcmd JFR.*`, `jfr view` — not a Java language feature.

## Internal Behavior

Uses JFR infrastructure (and cooperative sampling where relevant) to record method-level timing/trace data with controllable overhead.

## Production Example

Short timed recording during an incident; open in JMC; compare with GC/safepoint events on the same timeline.

## Limitations

- Overhead rises with aggressiveness — throttle for production.  
- Interpret samples statistically; don’t treat one stack as proof.  
- Exact event set: confirm against JEP/JDK docs (do not invent event names in runbooks without checking).

## Migration Considerations

Update observability playbooks for JDK 25 JFR views. Keep PII policies for recordings.

## Interview Questions

1. When prefer JFR method timing vs async-profiler?  
2. How limit overhead?  
3. How correlate with GC pauses?

### Related

[jep-518-jfr-cooperative-sampling.md](./jep-518-jfr-cooperative-sampling.md) · [jep-509-jfr-cpu-time-profiling.md](./jep-509-jfr-cpu-time-profiling.md)
