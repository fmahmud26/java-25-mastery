# Supplier\<T>

`T get()` — **defer** creation or lookup; no input.

## Mental Model

```text
() ──get──► T
lazy default / factory / retry probe
```

## Imperative vs Functional

```java
User u = cache.get(id);
if (u == null) {
    u = db.load(id);
    cache.put(id, u);
}

User u = cache.computeIfAbsent(id, db::load); // Function not Supplier
User fallback = Optional.ofNullable(cache.get(id))
        .orElseGet(() -> db.load(id));      // Supplier — lazy
```

## Production Example

```java
public final class ExpensiveReports {
    public Report dashboard(Supplier<Instant> clock, Supplier<Rates> rates) {
        // injectable clock/rates for tests — no static Instant.now() hardwire
        return build(clock.get(), rates.get());
    }
}

// retry
Supplier<CaptureResult> attempt = () -> psp.capture(cmd);
CaptureResult result = Retry.of(attempt).max(3).backoff(200).run();

// logging — don’t build huge strings eagerly
log.debug("order={}", () -> order.toDebugString()); // if API takes Supplier
```

`Optional.orElseGet`, `Objects.requireNonNullElseGet`, `ThreadLocal` patterns, factory `Supplier<DataSource>`.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Lazy costly defaults | Supplier that closes over mutable shared state unsafely |
| Test seams (time, random, ids) | Over-abstracting every `new` |

## Performance & Readability

`orElse(rebuild())` always builds; `orElseGet(this::rebuild)` defers — classic perf footgun.

## Common Mistake

```java
orElse(loadFromDb())      // eager — always hits DB
orElseGet(this::loadFromDb) // lazy
```

## Interview / PE

- `orElse` vs `orElseGet`?  
- **PE:** Supplier-injected clocks for payment settlement — why?

### Related

[function.md](./function.md) · [closures.md](./closures.md) · [side-effects.md](./side-effects.md)
