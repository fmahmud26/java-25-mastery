# Abstract Classes

A class that may leave methods abstract — **partial implementation** + single inheritance slot. Cannot be instantiated directly.

## 1. Mental Model

```text
abstract class OutboxHandler
   ├── shared: load, markProcessed, metrics
   └── abstract: handlePayload(bytes)
```

## 2. Problem It Solves

Several subtypes share fields and a fixed algorithm skeleton (template method) while varying a few steps.

## 3. Bad Design → Problems → Better Design

**Bad:** Abstract class used only as a “bag of unrelated helpers,” or when an interface + composed helper would do.

**Problems:** Burns the only `extends` slot; forces awkward inheritance for unrelated types.

**Better:** Interface for the contract; abstract class only when template + state are real.

```java
public abstract class NotificationHandler {
    private final NotificationPort port;
    private final MeterRegistry metrics;

    protected NotificationHandler(NotificationPort port, MeterRegistry metrics) {
        this.port = port;
        this.metrics = metrics;
    }

    public final void process(NotificationEvent event) {
        if (!supports(event)) return;
        doSend(event);
        metrics.counter("notify.sent", "type", event.type()).increment();
    }

    protected abstract boolean supports(NotificationEvent event);
    protected abstract void doSend(NotificationEvent event);

    protected final NotificationPort port() { return port; }
}
```

## 4. Technical Rules (Java 25)

- May have ctors, fields, concrete + abstract methods.  
- Subclass must implement all abstract methods (unless also abstract).  
- Can `implements` interfaces.  
- Prefer `protected` hooks; keep template method `final`.

## 5. Internal Behavior

Normal class metadata; abstract methods have no code attribute. Instantiation of abstract type is a compile error.

## 6. Domain Scenarios

- **Payments:** abstract `PspAdapter` with shared signing/hmac; subclasses Stripe/Adyen.  
- **Logistics:** abstract `LabelGenerator` with shared PDF framing.

## 7. Trade-offs & When Not

If there is **no shared state** and **no template**, use an interface. If reuse is optional helpers, use composition (`NotificationSupport` class).

## 8. Failure Scenario

Subclass overrides a non-hook concrete method and breaks metrics. Fix: `final` template; only abstract/protected extension points.

## 9. LLD Interview Scenario

Shared retry/error mapping for three PSPs — abstract class vs decorator around `PaymentPort`? Compare testability.

## 10. SOLID / Extensibility

Template method supports OCP if hooks are stable. Violating LSP in subclasses (throwing everywhere) breaks the abstraction.

## 11. Interview Ladder

- When abstract class over interface?  
- Can abstract class have constructors?  
- Template method pattern risks?

## 12. Principal Engineer Perspective

Abstract classes are **frameworky**. Use them inside a bounded context for a real algorithm skeleton. At service boundaries, prefer interfaces + composition so implementors aren’t forced into your inheritance tree.

### Related

[interfaces.md](./interfaces.md) · [inheritance.md](./inheritance.md) · [abstraction.md](./abstraction.md) · [interview.md](./interview.md)
