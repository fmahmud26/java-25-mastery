# Methods & Parameters

Named callable units — signatures, overloading, pass-by-value, and entrypoints.

## 1. Mental Model

```text
caller ──args (copied)──► method frame
         references copy the pointer, not the object
```

## 2. Simple Explanation

Methods encapsulate behavior. Java passes **arguments by value**: primitives copy bits; references copy the reference. A method can mutate the object a reference points to, but cannot rebind the caller’s variable.

## 3. Technical Explanation

| Topic | Rule |
|-------|------|
| Signature | Name + parameter types (not return type alone for overloading) |
| Overload | Same name, different params |
| Override | Same signature in subtype — runtime dispatch |
| `static` | No receiver; hide don’t override |
| Return | Value / reference / `void` |
| Compact main | Instance `main` for simple programs (JEP 512) |

Parameters are local variables initialized by the caller.

## 4. Internal Behavior

`invokevirtual` / `invokestatic` / `invokeinterface` / `invokespecial`. Overload resolved at compile time; override at runtime (unless final/private/static). Too many overloads confuse humans and inference.

## 5. Java 25 Example

```java
public final class SettlementService {
    public SettlementResult settle(String paymentId, long amountCents) {
        validate(paymentId, amountCents);
        return gateway.capture(paymentId, amountCents);
    }

    private static void validate(String paymentId, long amountCents) {
        if (paymentId == null || paymentId.isBlank()) throw new IllegalArgumentException("paymentId");
        if (amountCents <= 0) throw new IllegalArgumentException("amountCents");
    }
}

// parameters: rebinding vs mutation
void attachTag(Order order, String tag) {
    // order = new Order(...); // rebinds local only — caller unchanged
    order.tags().add(tag);     // mutates object reachable via copied reference
}
```

## 6. Real-World Scenario

**Settlement API:** overloaded `settle(String)` and `settle(String, boolean)` where boolean meant “force.” Call sites used wrong overload after refactor. Replaced with explicit `SettleCommand` record.

## 7. Common Mistake

Thinking Java is pass-by-reference; boolean flag parameters; huge parameter lists; side-effecting getters.

## 8. Failure Scenario

Silent wrong overload → skipped fraud check. Prefer command objects and clear names; add tests per path.

## 9. Performance Implications

Virtual calls are cheap when monomorphic (JIT inlines). Mega-morphic call sites can inhibit inlining — design for clarity first; profile later.

## 10. Interview Questions

- Pass-by-value meaning?  
- Overload vs override?

## 11. Senior-Level Follow-ups

- When replace overloads with a command type?  
- How do you keep service method APIs stable?

## 12. Principal Engineer Perspective

Methods are **contracts**. Prefer explicit types over flag soup; validate at boundaries; keep side effects obvious in the name.

### Related

[varargs.md](./varargs.md) · [static.md](./static.md) · [javac-java-jshell.md](./javac-java-jshell.md)
