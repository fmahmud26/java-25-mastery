# static

Class-level members — no instance receiver; one copy per Class (per loader).

## 1. Mental Model

```text
Class SettlementService
   └── static Clock CLOCK     // shared by all instances / callers
Instance s1, s2 ──► own fields only
```

## 2. Simple Explanation

`static` fields and methods belong to the type, not an instance. Use for pure functions and true process-wide constants. Mutable statics are shared global state — a concurrency and testability hazard.

## 3. Technical Explanation

| Use | OK? |
|-----|-----|
| `static final` constants | Yes |
| Pure `static` helpers | Yes |
| Caches / registries | Careful — lifecycle & memory |
| Per-request mutable static | **No** |
| Override static | **No** — hiding only |

Static initialization runs on class init (`<clinit>`), under lock, once per class loader.

## 4. Internal Behavior

`invokestatic`. Class init failures → `ExceptionInInitializerError` / `NoClassDefFoundError` later. Mutable statics visible across threads without synchronization → data races.

## 5. Java 25 Example

```java
public final class MoneyFormat {
    private MoneyFormat() {}
    public static String cents(long cents) {
        return Long.toString(cents);
    }
}

// BAD in multi-tenant services:
// static Map<String, Cart> CARTS = new HashMap<>();
```

## 6. Real-World Scenario

**Feature flags:** static `boolean ENABLED` flipped in tests leaked into next test class → flaky CI. Replaced with instance config / DI. Prod bug: static `SimpleDateFormat` shared → corrupted dates under load.

## 7. Common Mistake

Static mutable caches without eviction; static non-thread-safe formatters; using static for “easy access” instead of DI.

## 8. Failure Scenario

Cross-request data bleed via static map. Symptom: customer A sees customer B’s cart. Fix remove static; add isolation tests.

## 9. Performance Implications

Static constants are fine. Unbounded static caches → memory leak / OOM. Class init on critical path can delay first request.

## 10. Interview Questions

- static vs instance?  
- Can you override static methods?

## 11. Senior-Level Follow-ups

- Safe designs for a process-wide cache?  
- Class initialization deadlocks?

## 12. Principal Engineer Perspective

Treat mutable statics as **global variables**. Prefer immutable statics and injected collaborators. Any static cache needs bounds, eviction, and concurrency policy.

### Related

[variables.md](./variables.md) · [final.md](./final.md) · [methods.md](./methods.md)
