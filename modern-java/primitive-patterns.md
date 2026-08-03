# Primitive Patterns (Preview)

> **Preview on Java 25** — JEP 507 (third preview). Requires `--enable-preview`. May change; not a casual production default.

## Problem Before

Pattern matching favored references; primitive `switch`/`instanceof` stories were inconsistent vs boxed types.

## The Feature

Patterns for primitives (`int`, `byte`, …) with **exact conversion** semantics (match only when the value fits without information loss where applicable).

## How It Works

Enable preview at compile and runtime. Primitive type patterns in `switch`/`instanceof`; nested use in records like `Id(int v)`.

## Before → After

```java
// Traditional
String status(int code) {
    return switch (code) {
        case 0 -> "ok";
        case 1 -> "warn";
        default -> "other:" + code;
    };
}

// Preview direction — richer primitive pattern forms (see JEP 507 for current syntax)
// static String status(int code) { ... case int i -> ... }
```

```bash
javac --release 25 --enable-preview Main.java
java --enable-preview Main
```

## Production Usage

- **Today:** prefer final pattern features; experiment in branches/labs  
- When finalized in a future JDK, revisit error-code / status matching

## Trade-offs

Early access to uniform patterns vs toolchain/preview risk and training cost.

## When NOT to Use

- Production mainline without an ADR allowing preview  
- Libraries that must run on non-preview JVMs

## Migration Notes

Wait for final (or isolate behind preview module). Do not bake preview syntax into shared libraries.

## Interview Questions

- What does “preview” mean for delivery?  
- Why care about exact conversion for primitives?  
- How do you enable preview on 25?

### Related

[pattern-matching.md](./pattern-matching.md) · [java-evolution.md](./java-evolution.md)
