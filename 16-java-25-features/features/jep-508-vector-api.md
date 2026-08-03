# JEP 508 — Vector API (Tenth Incubator)

| | |
|--|--|
| **JEP** | [508](https://openjdk.org/jeps/508) |
| **Status** | **Incubator** (tenth) — JDK 25 |
| **Module** | `jdk.incubator.vector` |

## Purpose

Express vector computations that reliably compile to optimal SIMD instructions on supported CPUs.

## Problem Solved

Manual HotSpot intrinsics / JNI / unclear auto-vectorization aren’t a stable programming model for explicit SIMD algorithms.

## Previous Approach

Scalar loops hoping auto-vectorizers help; JNI to SIMD libraries; JDK-internal intrinsics only.

## New Approach

Incubating Vector API: species, shapes, lanes, operations on `Vector<T>` etc.

```bash
javac --add-modules jdk.incubator.vector ...
java --add-modules jdk.incubator.vector ...
```

```java
import jdk.incubator.vector.*;
// VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;
// ... load / mul / add / intoArray — see JEP 508 examples
```

## Syntax / API

Incubator package — **will change** before final. Tenth incubation signals ongoing refinement (often waiting on related Valhalla/runtime work).

## Internal Behavior

HotSpot compiles vector operations to CPU vector ISAs when possible; fallback paths when shapes unsupported.

## Production Example

HPC-style kernels, encryption hot loops, image ops — usually behind feature flags; many teams wait for final.

## Limitations

- Incubator module required.  
- API instability across JDKs.  
- Not all machines get same speedups — measure.  
- Long incubation is expected; don’t treat as “almost final next month” without OpenJDK signals.

## Migration Considerations

Isolate in modules; recompile every JDK; avoid leaking incubator types in public APIs.

## Interview Questions

1. Incubator vs preview?  
2. Why so many incubations?  
3. How enable the module?  

### Related

[../experimental-features.md](../experimental-features.md) · [../feature-status.md](../feature-status.md)
