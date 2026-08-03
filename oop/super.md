# `super`

Access **superclass** constructor and members — chain construction; call overridden implementations deliberately.

## 1. Mental Model

```text
Subclass ctor → super(...) → parent fields init → subclass fields
override → super.method() optional collaboration
```

## 2. Problem It Solves

Subclasses must initialize parent state; sometimes extend rather than fully replace behavior.

## 3. Bad Design → Problems → Better Design

**Bad:** Override `validate()` and forget `super.validate()` when parent holds critical checks.

**Problems:** Invariants silently disappear.

**Better:** Template method on parent is `final`; subclasses only fill hooks — no obligatory `super` calls.

```java
public abstract class PspAdapter {
    private final String merchantId;
    protected PspAdapter(String merchantId) {
        this.merchantId = Objects.requireNonNull(merchantId);
    }
    protected final String merchantId() { return merchantId; }
}
```

## 4. Technical Rules (Java 25)

- `super(...)` ctor call: first statement when used (classic rule).  
- Implicit `super()` if no explicit chain and parent has no-arg.  
- `super.method()` invokes superclass version (non-virtual for that call).  
- No `super` for fields preference — keep fields private.

## 5. Internal Behavior

`invokespecial` for `super.method` / ctors. Bypasses dynamic dispatch for that invocation.

## 6. Domain Scenarios

- **Notifications:** base logs correlation id; subclass should not skip required `super` if you didn’t make template final — better make it final.  
- **Inventory:** shared adapter stores credentials via `super(config)`.

## 7. Trade-offs & When Not

Relying on “please call super” is a design smell (fragile). Prefer final templates + abstract hooks or composition.

## 8. Failure Scenario

Parent adds audit in `save()`; child override skips `super.save()` → unaudited ledger writes. Fix: final `save()` template.

## 9. LLD Interview Scenario

Show a hierarchy that requires `super` calls — then refactor to eliminate that requirement.

## 10. SOLID / Extensibility

“Call super” contracts violate easy LSP reasoning. Design extension points explicitly.

## 11. Interview Ladder

- When is `super()` inserted?  
- `super.method` vs virtual call?  
- Why is “must call super” a smell?

## 12. Principal Engineer Perspective

Ban undocumented call-super requirements in shared libraries. Extension = hooks, strategies, or decorators — not tribal memory.

### Related

[this.md](./this.md) · [inheritance.md](./inheritance.md) · [constructor-chaining.md](./constructor-chaining.md) · [abstract-classes.md](./abstract-classes.md)
