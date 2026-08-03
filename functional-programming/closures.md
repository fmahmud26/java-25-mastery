# Closures (Captures)

A lambda that **closes over** enclosing state — locals, parameters, or `this`.

## Mental Model

```text
lambda + captured environment = closure
Java: capture by copying effectively-final locals; fields via this
```

## Simple Example

```java
String prefix = "PAY-";
Function<String, String> tag = id -> prefix + id; // closes over prefix
```

## Production Example

```java
public final class RateLimiterFilter {
    public Predicate<String> allowUnder(long maxPerUser, Map<String, LongAdder> hits) {
        return userId -> hits.computeIfAbsent(userId, _ -> new LongAdder()).sum() < maxPerUser;
    }
}

// factory closes over config
Supplier<HttpClient> clients = () -> HttpClient.newBuilder()
        .connectTimeout(timeout) // capture timeout
        .build();
```

Instance capture:

```java
public final class Ledger {
    private final Writer writer;
    public Consumer<Payment> poster() {
        return p -> writer.write(p); // captures this.writer
    }
}
```

## Internal Behavior

Capturing lambdas typically allocate an object holding captured values. Non-capturing may use a singleton. Escaping closures that capture `this` can extend object lifetime (memory).

## When Better / Worse

| Better | Worse |
|--------|-------|
| Binding config into small policies | Capturing large objects / enclosing services accidentally |
| Testable factories | Closures over mutable shared maps without concurrency control |

## Common Mistake

Returning a lambda that captures a request-scoped object and storing it globally → leaks / wrong data on later calls.

## Interview / PE

- Is a Java lambda a closure?  
- Capture of `this` implications for GC?  
- **PE:** listener lambda captures controller — memory leak pattern?

### Related

[effectively-final.md](./effectively-final.md) · [side-effects.md](./side-effects.md) · [lambda-expressions.md](./lambda-expressions.md)
