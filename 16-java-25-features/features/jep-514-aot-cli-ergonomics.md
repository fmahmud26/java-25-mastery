# JEP 514 — Ahead-of-Time Command-Line Ergonomics

| | |
|--|--|
| **JEP** | [514](https://openjdk.org/jeps/514) |
| **Status** | **Final** (JDK / HotSpot) — JDK 25 |
| **Related** | JEP 483 AOT Class Loading & Linking (JDK 24) |

## Purpose

Simplify creating **AOT caches** that speed application startup (Project Leyden ergonomics).

## Problem Solved

JDK 24 two-step workflow (`AOTMode=record` then `AOTMode=create`) plus leftover config files was cumbersome for common cases.

## Previous Approach

```bash
java -XX:AOTMode=record -XX:AOTConfiguration=app.aotconf -cp app.jar com.example.App
java -XX:AOTMode=create -XX:AOTConfiguration=app.aotconf -XX:AOTCache=app.aot
java -XX:AOTCache=app.aot -cp app.jar com.example.App
```

## New Approach

One-step training+create for common cases:

```bash
java -XX:AOTCacheOutput=app.aot -cp app.jar com.example.App ...
java -XX:AOTCache=app.aot -cp app.jar com.example.App ...
```

Temporary AOT configuration handled for you. `JDK_AOT_VM_OPTIONS` can pass options to the cache-creation sub-invocation.

## Syntax / API

Command-line / env only (`AOTCacheOutput`, existing `AOTCache` / modes).

## Internal Behavior

Launcher splits into record then create sub-invocations. Two-step remains for split hardware (train small, create large) or constrained memory (one-step may need ~2× heap memory transiently — per JEP).

## Production Example

Container services with slow cold start: produce `app.aot` in CI/CD, mount into runtime image, start with `-XX:AOTCache=...`.

## Limitations

- Does not invent new AOT optimizations by itself — ergonomics for existing/future Leyden opts.  
- Resource-constrained envs may need two-step.  
- Benefits depend on training coverage.

## Migration Considerations

Adopt one-step in CI; keep two-step docs for advanced cases. Coordinate with JEP 515 profiling data when used.

## Interview Questions

1. What problem does AOTCacheOutput solve?  
2. When keep two-step?  
3. Relation to Leyden / JEP 483?

### Related

[jep-515-aot-method-profiling.md](./jep-515-aot-method-profiling.md)
