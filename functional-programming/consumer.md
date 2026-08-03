# Consumer\<T>

`void accept(T t)` — **side-effecting** sink (log, send, persist, metrics).

## Mental Model

```text
T ──accept──► (effects)
andThen to sequence consumers
```

## Imperative vs Functional

```java
for (Payment p : payments) {
    ledger.post(p);
    metrics.markPosted();
}

Consumer<Payment> post = ledger::post;
Consumer<Payment> mark = _ -> metrics.markPosted();
payments.forEach(post.andThen(mark));
```

## Production Example

```java
public final class PaymentListeners {
    private final CopyOnWriteArrayList<Consumer<PaymentCaptured>> listeners =
            new CopyOnWriteArrayList<>();

    public void onCaptured(Consumer<PaymentCaptured> listener) {
        listeners.add(listener);
    }

    public void fire(PaymentCaptured event) {
        listeners.forEach(l -> l.accept(event));
    }
}

// registration
listeners.onCaptured(ledger::handle);
listeners.onCaptured(notifier::sendReceipt);
```

Prefer explicit service methods when order, transactions, and error handling matter more than sugar.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Callbacks, forEach sinks | Hiding critical business writes only in consumers with no error policy |
| Listener lists | `stream().forEach` instead of a real loop when you need break/return |

## Performance & Readability

`forEach` + Consumer is fine for clarity; not automatically faster. Side effects in `map` are worse style than Consumer/`peek` (avoid `peek` for real logic).

## Common Mistake

`stream().map(x -> { save(x); return x; })` — side effect in transform. Use `forEach`/`Consumer` or imperative service call.

## Interview / PE

- Consumer vs Function?  
- **PE:** fan-out listeners — Consumer list vs domain events/outbox?

### Related

[side-effects.md](./side-effects.md) · [bi-consumer.md](./bi-consumer.md) · [supplier.md](./supplier.md)
