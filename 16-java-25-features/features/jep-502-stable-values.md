# JEP 502 — Stable Values (Preview)

| | |
|--|--|
| **JEP** | [502](https://openjdk.org/jeps/502) |
| **Status** | **Preview** — JDK 25 |
| **Related** | Further evolution appears as “Lazy Constants” in later preview discussions (e.g. JEP 526) — track OpenJDK; do not assume 502 is the final name/API |

## Purpose

Hold immutable data initialized **at most once**, at a flexible time, while still enabling JVM constant-folding-style optimizations similar to `final`.

## Problem Solved

`final` fields must be initialized eagerly (ctor / `<clinit>`), hurting startup when many components create expensive deps (loggers, clients) that might never be used.

## Previous Approach

Eager `final` fields, or lazy `static` holder / double-checked locking / racy non-final fields losing optimization.

## New Approach

Preview **Stable Value** API: decouple creation from initialization; guarantee single init; JVM can treat as stable/constant for optimizations.

```java
// Preview API — check java.lang stable value types in JDK 25 with --enable-preview
// Conceptual: obtain a stable holder, set/compute once, then get immutable content
```

Consult [JEP 502](https://openjdk.org/jeps/502) for the precise class/method names in the JDK 25 preview (API may still move).

## Syntax / API

Preview — `--enable-preview`. Prefer reading current `java.lang` javadoc for the GA build you use.

## Internal Behavior

At-most-once initialization even under races; content then trusted like constants for HotSpot optimizations (similar spirit to JDK-internal `@Stable`).

## Production Example

Defer logger/client init until first use without giving up immutability optimizations — **only in preview-enabled experiments** until finalized.

## Limitations

- Preview / naming may evolve.  
- Not a language keyword.  
- Misuse as mutable state store defeats the model.

## Migration Considerations

Don’t couple public APIs to preview types. Prototype behind flags.

## Interview Questions

1. Stable values vs `final`?  
2. Why help startup?  
3. At-most-once under concurrency?  
4. Preview implications?

### Related

[../preview-features.md](../preview-features.md) · [../feature-status.md](../feature-status.md)
