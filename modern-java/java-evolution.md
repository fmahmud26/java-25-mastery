# Java Evolution: 8 → 25

Why each era exists, what to adopt, what to leave behind.

## Problem Before Modern Java

Pre-8 Java meant anonymous classes for callbacks, mutable Date APIs, verbose POJOs, and `switch` fall-through bugs. Teams still on 8 miss a decade of language and API design aimed at **data**, **safety**, and **readability**.

## Era Guide

### Java 8 — Functional leap (2014, LTS)

| Added | Replaces / improves |
|-------|---------------------|
| Lambdas, method refs | Anonymous `Runnable`/`Comparator` classes |
| Functional interfaces | Ad-hoc single-method types |
| Stream API | Manual loops for many transforms |
| `Optional` | Null-as-return without structure |
| `java.time` | `Date` / `Calendar` |

**Migration:** Adopt lambdas/streams where clarity wins; don’t force streams on every loop.

### Java 9–11 — Platform (LTS 11)

| Added | Why |
|-------|-----|
| JPMS | Strong encapsulation; JDK modularized |
| `var` (10) | Local noise reduction |
| `List.of` / `Set.of` / `Map.of` (9) | Immutable literals |
| HttpClient (11) | Standard HTTP without Apache-only |
| `jshell` (9) | REPL |

**Migration:** Many apps stay on classpath (unnamed module) while using JDK 11+ APIs. That’s valid.

### Java 12–17 — Language productivity (LTS 17)

| Added | Why |
|-------|-----|
| Switch expressions (14) | Exhaustive value-producing switch |
| Text blocks (15) | Readable SQL/JSON |
| `instanceof` patterns (16) | Cast-free type narrow |
| Records (16) | Immutable data carriers |
| Sealed classes (17) | Closed hierarchies |

**Migration:** 8→17 is the highest ROI jump for most codebases (records + text blocks + switch expr).

### Java 18–21 — Patterns & concurrency (LTS 21)

| Added | Why |
|-------|-----|
| Pattern switch (21) | Data-oriented control flow |
| Record patterns (21) | Deconstruction |
| Sequenced collections (21) | Predictable encounter order APIs |
| Virtual threads (21) | Cheap blocking concurrency |

**Migration:** Pattern switch + sealed events is the modern alternative to Visitor/`instanceof` chains.

### Java 22–25 — Polish to LTS 25

| Added | Java 25 status |
|-------|----------------|
| Unnamed variables/patterns `_` (22) | Final |
| Compact source files & instance main | Final (JEP 512) |
| Module import declarations | Final (JEP 511) |
| Scoped values | Final |
| Primitive patterns (JEP 507) | **Preview** |
| Structured concurrency | Preview (track separately) |

## How to Decide What to Adopt

```text
1. Safety & clarity (records, sealed, switch expr, text blocks) → yes
2. Local ergonomics (var, _) → yes with style guide
3. Architecture (JPMS, VT) → deliberate project
4. Preview → only with enablement policy
```

## Before → After (team coding standard)

```java
// Legacy Java 8-ish service DTO
public class PaymentDto {
    private String id;
    private long cents;
    // getters, setters, equals, hashCode, toString...
}

// Java 25
public record PaymentDto(String id, long cents) {
    public PaymentDto {
        Objects.requireNonNull(id);
        if (cents <= 0) throw new IllegalArgumentException("cents");
    }
}
```

## Production Usage

- Pin **toolchain = 25**, `--release 25`, runtime image aligned.  
- Adopt features **per PR boundary** (DTOs first, then domain events, then control flow).  
- Keep a short “preview allowed?” ADR.

## Trade-offs

Newer syntax can widen the gap with contractors still on 11. Training cost is real; staying on 8 forever is more expensive in bugs and hiring signal.

## When NOT to Rewrite

- Stable module with no change pressure — don’t churn for fashion.  
- Generated code / frameworks that expect JavaBeans setters.  
- Hot paths where a clear `for` loop profiles better than a stream (measure).

## Interview Questions

- Name three Java 8 features still foundational on 25.  
- What did 17 unlock that 11 lacked for domain modeling?  
- What is still preview on 25 that you would not ship casually?  
- How would you migrate a payment service from 11 to 25 in phases?

### Related

[README.md](./README.md) · [modern-coding-style.md](./modern-coding-style.md) · [interview.md](./interview.md)
