# JEP 513 — Flexible Constructor Bodies

| | |
|--|--|
| **JEP** | [513](https://openjdk.org/jeps/513) |
| **Status** | **Final** (SE language) — JDK 25 |
| **History** | Preview: 447 → 482 → 492 → **513 final** (no change) |

## Purpose

Allow statements **before** `super(...)` / `this(...)` in constructors, so arguments can be validated/prepared first, and subclass fields can be initialized before superclass code might observe them.

## Problem Solved

Old rule “constructor invocation must be first” blocked fail-fast validation and forced awkward helper calls inside `super(...)`. Superclass constructors calling overridable methods could see uninitialized subclass fields.

## Previous Approach

```java
Employee(..., int age) {
    super(..., age);                 // runs first
    if (age < 18 || age > 67) throw ...;
}
// or: super(..., verifyAge(age));
```

## New Approach

```java
Employee(..., int age) {
    if (age < 18 || age > 67)
        throw new IllegalArgumentException(...);
    super(..., age);
}
```

Pre-`super`/`this` code must not use the object under construction in illegal ways; it **may** initialize fields (per JEP rules) so state is set before superclass callbacks.

## Syntax / API

Language change only — no new package. Two phases conceptually: prologue (before explicit constructor invocation) and rest of constructor body after superclass construction.

## Internal Behavior

Preserves top-down initialization safety while extending what prologue may do. Improves integrity when superclass constructors invoke methods overridden by subclasses (fields can be written in prologue).

## Production Example

Domain entities validating IDs/ranges before expensive superclass setup; records/value-rich hierarchies that need shared computed arguments for `super`.

## Limitations

- Prologue still restricted: no reading uninitialized state / illegal `this` use (see JLS updates in JEP).  
- Does not remove need for careful overridable-method design in constructors.

## Migration Considerations

Refactor constructors that used static helpers solely to satisfy “super first.” Available without preview on 25+.

## Interview Questions

1. What could you not do before JEP 513?  
2. Why initialize fields before `super`?  
3. Is arbitrary `this` use allowed in prologue?  

### Related

[../language-changes.md](../language-changes.md)
