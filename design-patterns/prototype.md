# Prototype

## Problem

Create new objects by **copying** an existing instance when construction is expensive or varies by runtime state.

## Why naive approach fails

Re-running costly setup; or `new` + manual field copy that misses deep state and breaks encapsulation.

## Pattern

Clone a prototypical instance; customize afterward.

## Implementation

```java
public sealed interface Shape permits Circle {
    Shape copy();
}

public record Circle(double r, String color) implements Shape {
    @Override public Shape copy() { return new Circle(r, color); }
}

Shape proto = new Circle(1.0, "red");
Shape s2 = proto.copy();
```

`Object.clone()` is awkward (marker `Cloneable`, shallow by default) — prefer copy constructors / records / explicit `copy()`.

## Trade-offs

| Pros | Cons |
|------|------|
| Avoids costly re-init | Deep vs shallow copy bugs |
| Runtime-configured templates | Registry of prototypes to manage |
| Fits immutable copies | `Cloneable` legacy pitfalls |

## Real-world usage

Game entity templates, document editors (“duplicate slide”), prototype beans in Spring (`@Scope("prototype")` is related naming, different mechanism).

Related: [builder.md](./builder.md), [immutability.md](./immutability.md).
