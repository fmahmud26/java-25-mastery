# JEP 518 — JFR Cooperative Sampling

| | |
|--|--|
| **JEP** | [518](https://openjdk.org/jeps/518) |
| **Status** | **Final** (JDK / JFR) — JDK 25 |

## Purpose

Improve JFR sampling so stack walking cooperates safely/efficiently with the runtime (foundation for better profiling, including CPU-time profiling work).

## Problem Solved

Reliable, low-overhead sampling must avoid unsafe interactions with the JVM (crashes / bias / missed samples) when recording stacks for profiling events.

## Previous Approach

Existing JFR execution sampling with known limitations; external profilers sometimes used unsupported internals.

## New Approach

Cooperative sampling mechanism inside HotSpot/JFR (see JEP 518) that other events (e.g. experimental CPU-time profiling in JEP 509) can build upon.

## Syntax / API

No application code API — JFR implementation enhancement. Consumed via JFR recordings / views.

## Internal Behavior

Sampler coordinates with thread states so stacks can be walked more reliably; reduces certain failure modes of asynchronous sampling approaches.

## Production Example

Always-on or periodic JFR profiles in production become safer/more accurate as dependent events improve; use JDK Mission Control / `jfr` views.

## Limitations

- Not a user-facing feature toggle with a new programming model.  
- Platform specifics still apply to some advanced events (see JEP 509 Linux focus).

## Migration Considerations

Upgrade JFR tooling knowledge; no code change. Prefer JFR over unsafe agents when possible.

## Interview Questions

1. Why “cooperative” sampling?  
2. Relation to JEP 509?  
3. Why avoid unsupported profiler APIs?

### Related

[jep-509-jfr-cpu-time-profiling.md](./jep-509-jfr-cpu-time-profiling.md) · [jep-520-jfr-method-timing-tracing.md](./jep-520-jfr-method-timing-tracing.md)
