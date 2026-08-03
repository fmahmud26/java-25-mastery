# Limitations of Java Generics

What you **cannot** (or should not) do — consequences of erasure, invariance, and the type system.

## Mental Model

```text
Compile-time power  ↑↑
Runtime reification ↓  (for ordinary type args)
⇒  express constraints in signatures, carry tokens at edges
```

## Simple Example (illegal)

```java
// new T();
// new T[10];
// List<String>.class;
// if (x instanceof List<String>) { }
// class Two implements List<String>, List<Integer> { }
```

## Advanced Example (workarounds used in real libraries)

```java
public final class SerializerRegistry {
    private final Map<Class<?>, Serializer<?>> byType = new ConcurrentHashMap<>();

    public <T> void register(Class<T> type, Serializer<T> serializer) {
        byType.put(type, serializer);
    }

    @SuppressWarnings("unchecked")
    public <T> Serializer<T> get(Class<T> type) {
        return (Serializer<T>) byType.get(type); // token restores T
    }
}
```

```java
// arrays of parameterized types — avoid; use lists
List<List<String>> pages = new ArrayList<>();
```

## Internal Behavior

Limitations are mostly “erasure + invariant generics + reified arrays.” Arrays are covariant and reified → clash with generic invariance ([interview.md](./interview.md)).

## Production Use Case

Knowing limits prevents impossible designs (“auto-new T in a generic DAO”). Successful designs: `Class<T>`, sealed types, factories, codegen (MapStruct), annotation processors.

## Common Mistake

Spreading `@SuppressWarnings("unchecked")` instead of fixing PECS or introducing a type token.

## Interview Trap

List of limitations without **why** — interviewers want erasure + migration story + array conflict.

### Catalog of limitations

| Limitation | Why | Typical escape |
|------------|-----|----------------|
| No `new T()` | Erased | Factory / `Supplier<T>` / `Class<T>` |
| No `new T[]` | Erasure + array reification | `List<T>`; `Array.newInstance` (unchecked) |
| No `List<String>.class` | Erasure | `TypeReference` / Guava `TypeToken` |
| No `instanceof List<String>` | Erasure | `instanceof List` + element checks |
| Can’t overload on type args alone | Same erasure | Different names / carriers |
| Primitives not type args | Historical erasure model | Wrappers; specialized collections |
| Invariance | Safety | Wildcards / PECS |
| Raw types allowed | Compatibility | Ban in new code |
| Heap pollution | Unchecked + erasure | Avoid raw/unchecked |

## Principal-Level Discussion

Limitations are **design constraints**, not trivia. Choose:

- **Static safety** (generics, sealed, PECS) for in-process APIs  
- **Tokens / schemas** at serialization boundaries  
- **Codegen** when reflection+unchecked becomes a maintenance sink  

Don’t invent a mini-reified runtime inside the app unless the platform requires it (DI mappers, serializers).

### Related

[type-erasure.md](./type-erasure.md) · [pecs.md](./pecs.md) · [generic-classes.md](./generic-classes.md)
