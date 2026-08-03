# JEP 511 — Module Import Declarations

| | |
|--|--|
| **JEP** | [511](https://openjdk.org/jeps/511) |
| **Status** | **Final** (SE language) — JDK 25 |
| **History** | Preview: 476 → 494 → **511 final** (no change from second preview) |

## Purpose

Import, on demand, all public top-level types from packages **exported** by a module (and relevant transitive exports), without requiring the importing code to be modularized.

## Problem Solved

Many on-demand package imports (`java.util.*`, `java.util.stream.*`, …) when using cohesive modular APIs — noisy for beginners, scripts, and exploration.

## Previous Approach

Single-type or package on-demand imports only.

## New Approach

```java
import module java.base;
import module java.sql;
```

## Syntax / API

```java
import module java.base;

Map<String, String> m = Stream.of("apple", "berry")
        .collect(Collectors.toMap(s -> s.substring(0, 1), Function.identity()));
```

Ambiguities (e.g. `List` from `java.util` vs `java.awt`) resolved by more specific imports (single-type or package on-demand shadow module imports).

## Internal Behavior

Resolves against module exports / transitive reads analogous to module graph rules. Cannot import the unnamed module (classpath) via `import module`. Works on classpath code consuming JDK modules.

## Production Example

Prototypes, JShell, compact source files, teaching; optionally coalesce many package imports in small tools. Large codebases may still prefer explicit single-type imports for clarity.

## Limitations

- Name ambiguity risk across modules.  
- Not a substitute for understanding packages in large systems.  
- Aggregator modules (e.g. `java.se`) have specific rules — see JEP for `requires transitive java.base` changes.

## Migration Considerations

Safe to adopt incrementally. Pair with JEP 512 compact sources (implicit `java.base` module import behavior there).

## Interview Questions

1. What does `import module M` import?  
2. Does the file need to be in a module?  
3. How resolve ambiguous simple names?  
4. Relation to compact source files?

### Related

[jep-512-compact-source-files.md](./jep-512-compact-source-files.md)
