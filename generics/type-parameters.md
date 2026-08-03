# Type Parameters

Named placeholders (`T`, `E`, `K`, `V`, …) introduced on classes, interfaces, or methods.

## Mental Model

```text
Box<T>     →  “Box of some type T chosen by the caller”
<T> T id(T x)  →  “same T in and out for this call”
```

`T` is not a runtime class — it is a compile-time name erased later.

## Simple Example

```java
public final class Id<T> {
    private final T value;
    public Id(T value) { this.value = Objects.requireNonNull(value); }
    public T value() { return value; }
}

Id<String> paymentId = new Id<>("pay_123");
```

## Advanced Example (library API)

```java
public interface Repository<ID, E> {
    Optional<E> findById(ID id);
    E save(E entity);
}

public final class OrderRepository implements Repository<OrderId, Order> {
    @Override public Optional<Order> findById(OrderId id) { /* ... */ return Optional.empty(); }
    @Override public Order save(Order entity) { return entity; }
}
```

## Internal Behavior

Compiler substitutes checks/casts. Bytecode for unbound `T` uses `Object` (or bound). No per-`T` class is generated for `Box<String>` vs `Box<Integer>`.

## Production Use Case

Payment SDK: `PaymentClient<Req extends PaymentRequest, Res>` keeps request/response paired without casts at call sites.

## Common Mistake

Using raw types (`List` instead of `List<String>`) “to save typing” — disables checking and causes heap pollution.

## Interview Trap

“Is `T` available at runtime?” — **No** (erasure), except via patterns like storing `Class<T>` tokens explicitly.

## Principal-Level Discussion

Name parameters for domain (`ID`, `E`, `Event`) in public APIs; single-letter is fine for tiny utilities. Too many type parameters (`A,B,C,D,E`) signals the type should be split or use a context object.

### Related

[generic-classes.md](./generic-classes.md) · [type-erasure.md](./type-erasure.md) · [bounded-types.md](./bounded-types.md)
