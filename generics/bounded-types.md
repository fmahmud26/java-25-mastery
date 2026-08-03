# Bounded Types

Restrict what can substitute for a type parameter so you can call bound members safely.

## Mental Model

```text
<T extends Number>     →  T is some subtype of Number
<T extends A & B>      →  T must satisfy all bounds (class first)
```

Bounds are **declaration-site** constraints on `T` (distinct from use-site wildcards).

## Simple Example

```java
public static <T extends Number> double total(Collection<T> values) {
    return values.stream().mapToDouble(Number::doubleValue).sum();
}

total(List.of(1, 2, 3));
total(List.of(1.5, 2.5));
```

## Advanced Example

```java
public final class MoneyMath {
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
}

// recurring / self-bounding pattern used by Enum, Comparable:
public static <E extends Enum<E>> Optional<E> parse(Class<E> type, String name) {
    try {
        return Optional.of(Enum.valueOf(type, name));
    } catch (IllegalArgumentException ex) {
        return Optional.empty();
    }
}
```

```java
public static <T extends CharSequence & Appendable> void appendAll(T target, List<String> parts)
        throws IOException {
    for (String p : parts) target.append(p);
}
```

## Internal Behavior

Erasure replaces `T` with its **leftmost bound** (`Number`, `Object`, …). Casts inserted when returning `T`.

## Production Use Case

`<T extends DomainEvent>` on handlers; `<T extends Identifiable>` on repositories; numeric aggregations; `Comparable` helpers.

## Common Mistake

Writing `<T extends List<String>>` when you meant a wildcard parameter `List<? extends T>` at a use site — wrong layer.

## Interview Trap

Multiple bounds order: **class must be first**, then interfaces. `<T extends Runnable & AbstractList>` is illegal if `AbstractList` isn’t first (and AbstractList is a class — must lead).

## Principal-Level Discussion

Bounds document **capabilities**. Prefer a small domain interface bound (`Payable`) over bounding to a fat concrete class — keeps tests and adapters free.

### Related

[upper-bounds.md](./upper-bounds.md) · [type-parameters.md](./type-parameters.md) · [type-erasure.md](./type-erasure.md)
