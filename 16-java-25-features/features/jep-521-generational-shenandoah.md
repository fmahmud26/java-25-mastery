# JEP 521 — Generational Shenandoah

| | |
|--|--|
| **JEP** | [521](https://openjdk.org/jeps/521) |
| **Status** | **Product** feature — JDK 25 |
| **History** | Experimental generational mode JEP 404 (JDK 24) → **521 product** |

## Purpose

Promote Shenandoah’s **generational mode** from experimental to product (no experimental unlock).

## Problem Solved

Experimental barrier for production users who want generational efficiency with Shenandoah’s concurrent evacuation design.

## Previous Approach

```bash
java -XX:+UseShenandoahGC \
     -XX:+UnlockExperimentalVMOptions \
     -XX:ShenandoahGCMode=generational ...
```

## New Approach

```bash
java -XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational ...
```

## Critical Non-Goal (from JEP)

**Default Shenandoah mode remains non-generational** unless you set `ShenandoahGCMode=generational`. Do not claim generational-is-default on JDK 25.

## Syntax / API

JVM flags only.

## Internal Behavior

Generational hypothesis applied inside Shenandoah: young collections cheaper when most objects die young; concurrent mark/evacuate retained. Exact barriers are implementation details.

## Production Example

Latency-sensitive service already on Shenandoah evaluates generational mode in a bakeoff vs single-gen Shenandoah and vs G1/ZGC — compare pause, throughput, CPU, footprint.

## Limitations

- Opt-in mode.  
- Still must choose Shenandoah explicitly (`UseShenandoahGC`).  
- Workload-dependent results — no universal winner claim.

## Migration Considerations

Remove experimental unlock from launch scripts. Document mode in runbooks.

## Interview Questions

1. Is generational Shenandoah the default in 25?  
2. Experimental vs product difference?  
3. How enable?

### Related

[../jvm-capabilities.md](../jvm-capabilities.md) · [../../garbage-collection/shenandoah.md](../../garbage-collection/shenandoah.md)
