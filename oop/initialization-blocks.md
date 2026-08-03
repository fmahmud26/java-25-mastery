# Initialization Blocks

Instance and `static` initializer blocks run as part of class/instance setup — prefer constructors and clear static factories in production domain code.

## 1. Mental Model

```text
Class load → static fields + static {} (once)
new → super ctor → instance fields/blocks → ctor body
```

## 2. Problem It Solves

Rare shared setup (static maps) or field init that doesn’t fit a single expression.

## 3. Bad Design → Problems → Better Design

**Bad:** Large `static { }` loads config files, opens DB pools, starts threads.

**Problems:** Class-load failures → `ExceptionInInitializerError`; hard to test; hidden latency on first use.

**Better:** Explicit initialization in composition root / Spring `@PostConstruct` / factory with checked failures.

```java
public final class CountryCodes {
    private static final Set<String> ISO;
    static {
        ISO = Set.of("USD", "EUR", "GBP", "BDT"); // tiny, pure data — OK
    }
    private CountryCodes() {}
    public static boolean supported(String code) { return ISO.contains(code); }
}
```

## 4. Technical Rules (Java 25)

- Multiple blocks allowed; textual order with field inits.  
- Static runs once per class loader.  
- Instance blocks copy into each ctor.

## 5. Internal Behavior

Compiler merges blocks into `<clinit>` / `<init>`. Failures in `<clinit>` poison the class for that loader.

## 6. Domain Scenarios

- **Payments:** don’t open PSP HTTP client in static block.  
- **Inventory:** static tiny enum-like sets OK; connection pools not OK.

## 7. Trade-offs & When Not

Prefer field initializers and ctors for instance state. Prefer explicit lifecycle for I/O.

## 8. Failure Scenario

First request fails with `NoClassDefFoundError` cause `ExceptionInInitializerError` from bad static config path. Fix: lazy explicit init with actionable error metrics.

## 9. LLD Interview Scenario

Where should a service load feature flags — static block, ctor, or sidecar config client at startup?

## 10. SOLID / Extensibility

Hidden static init creates invisible coupling to environment — breaks testability (DIP/ops).

## 11. Interview Ladder

- Order of initialization?  
- Why are static blocks dangerous for I/O?  
- `<clinit>` failure modes?

## 12. Principal Engineer Perspective

Keep `<clinit>` **pure and tiny**. Anything that can fail for ops reasons belongs in explicit startup with health checks.

### Related

[constructor.md](./constructor.md) · [static fundamentals](../java-fundamentals/static.md)
