# Side Effects

Observable effects beyond the return value — I/O, mutation, logging, metrics, network.

## Mental Model

```text
Pure:     f(x) → y          (same x ⇒ same y; no external change)
Impure:   f(x) → y + world  (DB write, log, mutate list)
```

Java doesn’t enforce purity; **you** do by design.

## Imperative vs Functional Discipline

```java
// Mixed — hard to test
List<String> ids = payments.stream()
        .map(p -> {
            ledger.post(p);      // side effect inside map — bad style
            return p.id();
        })
        .toList();

// Separated
List<Payment> captured = payments.stream().filter(Payment::captured).toList();
captured.forEach(ledger::post);           // effects at edge
List<String> ids = captured.stream().map(Payment::id).toList();
```

## Production Example

```java
public final class CaptureService {
    public CaptureResult capture(CaptureCommand cmd) {
        // pure validation
        validate(cmd);
        // effects at boundary
        CaptureResult result = psp.capture(cmd);
        ledger.record(result);
        metrics.mark(result);
        return result;
    }
}
```

Functional interfaces: prefer **Function/Predicate/UnaryOperator** for pure core; **Consumer/Supplier**/explicit services for effects.

## When FP Improves / Hurts

| Improves | Hurts |
|----------|-------|
| Pure transforms + explicit effect stage | “Functional” code that mutates globals inside lambdas |
| Testable cores | Debugging effect order in parallel `forEach` |

## Performance & Readability

`peek` for debugging only — not production business effects. Parallel streams + side effects → races.

## Common Mistake

Relying on `forEach` order for critical writes under `parallel()`.

## Interview / PE

- What is a side effect?  
- Why avoid side effects in `map`?  
- **PE:** design a pipeline with pure pricing + effectful capture — module boundaries?

### Related

[immutability.md](./immutability.md) · [consumer.md](./consumer.md) · [when-to-use.md](./when-to-use.md)
