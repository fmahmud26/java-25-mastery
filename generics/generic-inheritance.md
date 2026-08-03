# Generic Inheritance

How extends/implements interact with type parameters, invariance, and erasure bridges.

## Mental Model

```text
ArrayList<String>  is-a  List<String>     ✓
ArrayList<String>  is-a  List<Object>     ✗  (invariant)
ArrayList<String>  is-a  List<?>          ✓
ArrayList<String>  is-a  Collection<String> ✓
```

Subtyping is on the **raw hierarchy**, but type arguments must match (unless wildcards).

## Simple Example

```java
interface Repository<ID, E> {
    E save(E entity);
}

class OrderRepository implements Repository<OrderId, Order> {
    @Override public Order save(Order entity) { return entity; }
}
```

## Advanced Example (bridges & overrides)

```java
class Node<T> {
    public T value() { return null; }
}

class StringNode extends Node<String> {
    @Override
    public String value() { return "x"; }
    // compiler also emits bridge: Object value() { return value(); }
}
```

```java
// covariant returns + generics — careful with overrides
interface Factory<T> { T create(); }
class UserFactory implements Factory<User> {
    @Override public User create() { return new User("u1"); }
}
```

Inheritance of concrete parameterized types:

```java
class PaymentList extends ArrayList<Payment> { } // locks element type — rarely worth it
```

Prefer composition + `List<Payment>` fields over subclassing concrete collections.

## Internal Behavior

Bridge methods restore overriding after erasure. Mixing raw and parameterized types in hierarchies → unchecked warnings and pollution risk.

## Production Use Case

`interface Client<Req, Res>`, specialized `PaymentsClient extends Client<CaptureReq, CaptureRes>`, handler hierarchies `Handler<E extends Event>`.

## Common Mistake

```java
class BadRepo extends Repo { // raw
    Object find(Object id) { ... }
}
```

Or assuming `List<Integer>` can override a method taking `List<Number>` interchangeably.

## Interview Trap

“Is `ArrayList<String>` a subtype of `ArrayList<Object>`?” — **No**.  
“Can `List<String>` be passed where `List<? extends Object>` is required?” — **Yes**.

Overriding `compareTo` with generics / raw `Comparable` — classic bridge/erasure interview.

## Principal-Level Discussion

Prefer **implementing generic interfaces** over extending concrete generic classes (especially collections). Locking `extends ArrayList<Foo>` couples callers to mutability and implementation. For domain events, prefer sealed hierarchies + generic handlers over deep generic class trees.

### Related

[type-erasure.md](./type-erasure.md) · [generic-interfaces.md](./generic-interfaces.md) · [pecs.md](./pecs.md) · [limitations.md](./limitations.md)
