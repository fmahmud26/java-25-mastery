# BiConsumer\<T,U>

`void accept(T t, U u)` — two-arg side effects. See also [bi-function.md](./bi-function.md).

## Mental Model

```text
(key, value) ──► effect
Map.forEach, pairwise logging
```

## Imperative vs Functional

```java
for (Map.Entry<String, Long> e : hits.entrySet()) {
    metrics.gauge(e.getKey(), e.getValue());
}

hits.forEach(metrics::gauge); // if gauge(String,long) matches
```

## Production Example

```java
BiConsumer<PaymentId, Exception> onFailure = (id, ex) -> {
    metrics.markFail(id);
    alert.pager("capture failed " + id, ex);
};

try {
    psp.capture(cmd);
} catch (Exception ex) {
    onFailure.accept(cmd.paymentId(), ex);
}
```

## When Better / Worse

Good for Map iteration and callbacks with two inputs. Bad when it becomes an error-handling framework in a lambda.

## Interview / PE

- BiConsumer vs Consumer?  
- **PE:** structured failure handlers as BiConsumer vs Result types?

### Related

[consumer.md](./consumer.md) · [bi-function.md](./bi-function.md) · [side-effects.md](./side-effects.md)
