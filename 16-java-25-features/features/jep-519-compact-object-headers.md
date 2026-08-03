# JEP 519 — Compact Object Headers

| | |
|--|--|
| **JEP** | [519](https://openjdk.org/jeps/519) |
| **Status** | **Product** HotSpot feature (finalized from experimental) — JDK 25 |
| **History** | Experimental JEP 450 (JDK 24) → **519 product**; **not default** (non-goal). Default planned separately (JEP 534 targets a later release). |

## Purpose

Make compact object headers a supported product option (no experimental unlock).

## Problem Solved

Classic 64-bit headers cost significant heap for huge object graphs. Compact layout reduces per-object overhead when enabled.

## Previous Approach

```bash
java -XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders ...
```

## New Approach

```bash
java -XX:+UseCompactObjectHeaders ...
```

## Syntax / API

No language/API change — JVM flag only.

## Internal Behavior

Smaller object header layout (Project Lilliput). Identity hash / locking / GC / class bits packed differently. Reserved bits noted for Valhalla in JEP discussion.

## Production Example

Enable in staging; compare heap occupancy, GC frequency, and CPU for allocation-heavy services. JEP cites SPECjbb2015 experiments (heap/CPU improvements in those settings) — **treat as experiment reports, re-measure your app**.

## Limitations

- **Off by default** on JDK 25.  
- Platform/GC combinations must be validated on your JDK build.  
- Not an application-code migration.

## Migration Considerations

Drop `UnlockExperimentalVMOptions` if you already tested on 24. Roll out behind flags; keep rollback `-XX:-UseCompactObjectHeaders`.

## Interview Questions

1. Final vs default?  
2. Why gradual rollout?  
3. Application code changes required?  

### Related

[../jvm-capabilities.md](../jvm-capabilities.md) · [../performance.md](../performance.md)
