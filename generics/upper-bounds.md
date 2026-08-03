# Upper Bounds (`extends`) — Wildcard Form

`? extends T` means unknown type that is a **subtype of T** (or T). Producer-friendly.

## Mental Model

```text
List<? extends Number>  →  List<Integer> or List<Double> or List<Number> ...
you can READ Number
you cannot ADD Integer safely (might be List<Double>)
```

Declaration-site `<T extends Number>` is different — see [bounded-types.md](./bounded-types.md).

## Simple Example

```java
Number first(List<? extends Number> nums) {
    return nums.get(0);
}

first(List.of(1, 2, 3));
```

## Advanced Example

```java
public interface EventBus {
    void publishAll(List<? extends DomainEvent> events);
}

public final class AuditProjection {
    public void project(List<? extends PaymentEvent> batch) {
        for (PaymentEvent e : batch) {
            sink.write(e);
        }
    }
}
```

Callers can pass `List<PaymentCaptured>` without copying to `List<PaymentEvent>`.

## Internal Behavior

Get returns `T` (upper bound). Add rejected except `null`. Capture prevents treating the list as a concrete consumer.

## Production Use Case

`Collections.max(Collection<? extends T>)`, stream pipelines, “process any subtype list” in domain services.

## Common Mistake

```java
List<? extends Number> xs = new ArrayList<Integer>();
xs.add(1); // compile error — and if allowed would be unsafe
```

## Interview Trap

“If I can’t add, is `extends` useless?” — No: it’s for **safe covariance when reading**.

## Principal-Level Discussion

Use `? extends` on **inputs you only read**. Returning `List<? extends T>` from public APIs is often worse than `List<T>` or a sealed/immutable concrete list — callers can’t use it as a producer of specific subtypes easily.

### Related

[lower-bounds.md](./lower-bounds.md) · [pecs.md](./pecs.md) · [wildcards.md](./wildcards.md)
