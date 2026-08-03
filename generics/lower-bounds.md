# Lower Bounds (`super`) — Wildcard Form

`? super T` means unknown type that is a **supertype of T**. Consumer-friendly.

## Mental Model

```text
List<? super Integer>  →  List<Integer> or List<Number> or List<Object>
you can ADD Integer
you can only READ as Object safely
```

## Simple Example

```java
void addIds(List<? super String> dest, String id) {
    dest.add(id);
}

List<CharSequence> sink = new ArrayList<>();
addIds(sink, "pay_1");
```

## Advanced Example

```java
public final class OrderPipeline {
    public static <T> void drain(
            Iterable<? extends T> src,
            List<? super T> dest) {
        for (T item : src) {
            dest.add(item);
        }
    }
}

List<PaymentCaptured> captured = ...;
List<Object> outbox = new ArrayList<>();
drain(captured, outbox);
```

```java
// Comparator consumer — JDK style
public static <T> void sort(List<T> list, Comparator<? super T> cmp) {
    list.sort(cmp);
}

sort(orders, Comparator.comparing(Order::totalCents)); // Comparator<Order>
sort(orders, Comparator.comparing(Object::toString));  // Comparator<Object> also OK
```

## Internal Behavior

Add of `T` is safe (whatever the list really is, it accepts `T`). get yields `Object`. Erasure unchanged.

## Production Use Case

`Collection.addAll`, copy helpers, listeners/`Consumer<? super Event>`, sorting with wider comparators.

## Common Mistake

Expecting `get` to return `T`:

```java
List<? super Integer> xs = new ArrayList<Number>();
xs.add(1);
Integer i = xs.get(0); // error — type is Object
```

## Interview Trap

Conflating `<T super Foo>` — **illegal**. Lower bounds exist on **wildcards**, not on type-parameter declarations (`T` only uses `extends` bounds).

## Principal-Level Discussion

`super` appears less often than `extends` but is critical for **flexible consumers** (comparators, collectors, output buffers). If an API can’t accept `Comparator<? super T>`, it’s harder to reuse.

### Related

[upper-bounds.md](./upper-bounds.md) · [pecs.md](./pecs.md) · [bounded-types.md](./bounded-types.md)
