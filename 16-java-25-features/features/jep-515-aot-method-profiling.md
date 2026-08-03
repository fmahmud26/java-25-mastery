# JEP 515 — Ahead-of-Time Method Profiling

| | |
|--|--|
| **JEP** | [515](https://openjdk.org/jeps/515) |
| **Status** | **Final** (JDK / HotSpot) — JDK 25 |
| **Project** | Leyden-related AOT |

## Purpose

Capture method profiling information during AOT training so subsequent runs / compilers can benefit from profiles earlier (improve warmup beyond class load/link caching).

## Problem Solved

AOT class load/link (JEP 483) helps startup, but peak performance still waits on JIT profiling. Training-time method profiles help bridge that gap.

## Previous Approach

Rely on online interpretation/C1 profiling after each cold start; limited reuse of prior run knowledge except via CDS/AOT load link.

## New Approach

During AOT training workflows, record method profile data into the AOT pipeline so production runs can apply it (see JEP 515 on openjdk.org for exact flags/integration with AOT cache creation).

## Syntax / API

HotSpot AOT flags / training workflow (no Java language change). Use with [JEP 514](./jep-514-aot-cli-ergonomics.md) ergonomics where applicable.

## Internal Behavior

Profiles from a representative training run guide compilation decisions earlier. Effectiveness depends on training representativeness — wrong training can mis-profile.

## Production Example

CI training run with production-like traffic shape; ship AOT cache + profiles; measure time-to-SLA latency vs cold JIT-only baseline.

## Limitations

- Training must resemble production (or profiles mislead).  
- Not a substitute for fixing algorithmic hotspots.  
- Exact operational flags: follow current JDK 25 man pages / JEP 515 text for your build.

## Migration Considerations

Enable after AOT cache basic workflow works. A/B in pre-prod.

## Interview Questions

1. Difference between AOT class linking and method profiling?  
2. What goes wrong with unrepresentative training?  
3. How relate to tiered compilation warm-up?

### Related

[jep-514-aot-cli-ergonomics.md](./jep-514-aot-cli-ergonomics.md) · [../performance.md](../performance.md)
