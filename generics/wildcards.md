# Wildcards

Use-site `?` — “some unknown type” — for flexible parameters/variables without naming a type parameter.

## Mental Model

```text
List<T>     →  exact T (read + write T)
List<?>     →  list of unknown (read Object; almost no writes)
List<? extends T> / List<? super T>  →  PECS
```

Wildcards are **not** declared on `class Foo<?>` as a type parameter name — use `T` there.

## Simple Example

```java
void logSizes(List<?> lists) {
    // size/get only — cannot add non-null elements
    int n = lists.size();
    Object head = lists.isEmpty() ? null : lists.get(0);
    // lists.add("x"); // illegal
}
```

## Advanced Example

```java
public final class Metrics {
    // accept any list of numbers without naming T
    public static double sum(List<? extends Number> values) {
        return values.stream().mapToDouble(Number::doubleValue).sum();
    }
}

sum(List.of(1, 2, 3));          // List<Integer>
sum(List.of(1.0, 2.0));         // List<Double>
```

Why not `List<Number>`? Because `List<Integer>` is **not** a `List<Number>` (invariance).

## Internal Behavior

Capture conversion: each `?` is a fresh “capture”. Erased like other generics. Compiler rejects unsafe inserts into `? extends`.

## Production Use Case

Method args that only read (`Iterable<? extends Event>`), logging helpers, `Class<?>` for reflective service loading, Guava/`Collections` APIs.

## Common Mistake

```java
List<Object> objs = stringList; // error — want List<?> or copy
```

Or using `List<?>` field when you need to add — use `List<T>` or `List<? super T>`.

## Interview Trap

`List<?>` vs `List<Object>` — see [interview.md](./interview.md). Assignment and `add` rules differ.

## Principal-Level Discussion

Wildcards belong on **APIs that abstract over producers/consumers**. Over-wildcarded return types (`List<?> findAll()`) hurt callers — return precise types; accept wildcards on **input**.

### Related

[pecs.md](./pecs.md) · [upper-bounds.md](./upper-bounds.md) · [lower-bounds.md](./lower-bounds.md)
