# Generic Interfaces

Contracts parameterized by types — the backbone of collections, ports, and functional APIs.

## Mental Model

```text
interface Repository<ID, E> { ... }
interface Function<T, R> { R apply(T t); }
class OrderRepo implements Repository<OrderId, Order> { }
```

Implementors bind parameters; callers see concrete safety.

## Simple Example

```java
public interface Serializer<T> {
    byte[] serialize(T value);
    T deserialize(byte[] data);
}

public final class MoneySerializer implements Serializer<Money> {
    @Override public byte[] serialize(Money value) { /* ... */ return new byte[0]; }
    @Override public Money deserialize(byte[] data) { return new Money(0, "USD"); }
}
```

## Advanced Example (hexagonal port)

```java
public interface EventHandler<E extends DomainEvent> {
    void on(E event);
    Class<E> eventType(); // runtime token for routing
}

public final class PaymentCapturedHandler implements EventHandler<PaymentCaptured> {
    @Override public void on(PaymentCaptured event) { ledger.post(event); }
    @Override public Class<PaymentCaptured> eventType() { return PaymentCaptured.class; }
}
```

Functional interfaces are generic too: `Function<T,R>`, `Predicate<T>`, `Comparator<T>`.

## Internal Behavior

Same erasure as classes. Overriding must respect erased signatures → bridge methods when return types specialize.

## Production Use Case

`List<E>`, `Map<K,V>`, Spring `Converter<S,T>`, Reactor `Publisher<T>`, custom `Port<Cmd, Result>`.

## Common Mistake

```java
interface Repo {
    Object find(Object id); // loses type safety vs Repo<ID,E>
}
```

Or implementing raw `List` — never.

## Interview Trap

Can an interface be generic and a class implement multiple instantiations?

```java
// illegal: List<String> and List<Integer> erase to same List
class Bad implements List<String>, List<Integer> { }
```

## Principal-Level Discussion

Generic interfaces define **families of ports**. Prefer `Handler<E>` + registry over `instanceof` chains. When the platform must dispatch at runtime, pair generics with an explicit `Class<E>` / sealed type — erasure won’t do it for you.

### Related

[generic-classes.md](./generic-classes.md) · [generic-inheritance.md](./generic-inheritance.md) · [type-erasure.md](./type-erasure.md)
