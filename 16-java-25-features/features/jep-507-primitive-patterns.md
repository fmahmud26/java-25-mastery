# JEP 507 — Primitive Types in Patterns, instanceof, and switch (Third Preview)

| | |
|--|--|
| **JEP** | [507](https://openjdk.org/jeps/507) |
| **Status** | **Preview** (third) — JDK 25 |

## Purpose

Extend pattern matching to **primitive types** in `instanceof`, `switch`, and related pattern contexts (with careful conversion/safety rules).

## Problem Solved

Pattern matching for reference types matured earlier; primitive patterns were still incomplete — forcing awkward boxing or manual casts.

## Previous Approach

```java
if (o instanceof Integer i) { int v = i; ... }
// or switch on boxed types only
```

## New Approach

Preview primitive type patterns / switch cases on primitives with defined conversion dominance rules (see JEP 507 for the exact preview grammar and safety constraints).

```java
// Preview --enable-preview
// Example shape (illustrative — verify against JEP 507 / javac 25):
// switch (x) { case int i when i > 0 -> ...; case long l -> ...; }
```

**Always verify** examples against the JEP and your `javac`; preview rules evolved across previews.

## Syntax / API

Language preview — `--enable-preview`.

## Internal Behavior

Compiler emits checks/conversions per pattern dominance and exactness rules so narrowing isn’t silently unsafe.

## Production Example

Not for irreversible production use while preview. Useful in learning/pattern-heavy codebases experimenting on 25.

## Limitations

- Third preview ⇒ still changing.  
- Complex dominance/conversion rules — easy to misuse in interviews without the JEP.  
- Don’t invent pattern forms not in the JEP.

## Migration Considerations

Keep on preview branch; recompile each JDK. Prefer reference patterns (final) in production code today.

## Interview Questions

1. Why preview again?  
2. Risk of primitive conversions in patterns?  
3. Difference from `instanceof Integer`?

### Related

[../preview-features.md](../preview-features.md) · [../language-changes.md](../language-changes.md)
