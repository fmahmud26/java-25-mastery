# Higher-Order Functions

Functions that **take or return** functional types — the API style of Streams, Optional, and strategy injection.

## Mental Model

```text
map(Function)          ← takes a function
retry(Supplier)        ← takes a function
Function.andThen(...)  ← returns a function
```

## Imperative vs Functional

```java
// Imperative: hard-coded steps
List<String> out = new ArrayList<>();
for (Order o : orders) out.add(o.id().value());

// HOF: behavior passed in
List<String> out = map(orders, o -> o.id().value());

static <T,R> List<R> map(List<T> in, Function<? super T, ? extends R> f) {
    List<R> out = new ArrayList<>(in.size());
    for (T t : in) out.add(f.apply(t));
    return List.copyOf(out);
}
```

## Production Example

```java
public final class Retry {
    public static <T> T call(Supplier<T> action, int maxAttempts, Predicate<Exception> retryOn)
            throws Exception {
        Exception last = null;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                return action.get();
            } catch (Exception ex) {
                last = ex;
                if (!retryOn.test(ex)) throw ex;
            }
        }
        throw last;
    }
}

CaptureResult result = Retry.call(
        () -> psp.capture(cmd),
        3,
        ex -> ex instanceof TimeoutException);
```

## When Better / Worse

HOFs unlock reusable infrastructure (retry, metrics wrappers, transactions templates). Overuse → callback hell / hard stack traces.

## Interview / PE

- Define HOF with Java examples  
- **PE:** decorator `Function<T,T> timed(Function<T,T>)` for metrics — good pattern?

### Related

[function-composition.md](./function-composition.md) · [supplier.md](./supplier.md) · [when-to-use.md](./when-to-use.md)
