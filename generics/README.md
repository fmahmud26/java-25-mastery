# Generics — Type-Safe APIs on Java 25

Compile-time type parameters for libraries and domain APIs — with **erasure** at runtime. Focus: correct API design (PECS), inheritance pitfalls, and interview traps.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. [type-parameters](./type-parameters.md) → [generic-classes](./generic-classes.md) → [generic-methods](./generic-methods.md) → [generic-interfaces](./generic-interfaces.md)  
2. Bounds: [bounded-types](./bounded-types.md) → [upper-bounds](./upper-bounds.md) (`extends`) → [lower-bounds](./lower-bounds.md) (`super`) → [pecs](./pecs.md) → [wildcards](./wildcards.md)  
3. Runtime truth: [type-erasure](./type-erasure.md) → [limitations](./limitations.md)  
4. [generic-inheritance](./generic-inheritance.md) · Drill: [interview.md](./interview.md)

## Mental map

```text
Declaration site:  class Repo<T> / <T> T get(...)
Use site:          List<? extends T>  /  List<? super T>
Compile time:      check + insert casts
Runtime:           erased → Object or bound (+ bridges)
```

## Principal stance

Generics are an **API tool**. Prefer precise type parameters on declarations; use wildcards on **inputs** that only produce or only consume. Never design around reified type arguments — Java doesn’t have them for ordinary classes.
