# Command

## Problem

You need to treat a request as an object — queue it, log it, retry it, undo it, or schedule it — decoupled from the invoker.

## Forces

- Decouple UI/API from execution  
- Support retry/idempotency  
- Audit trail of intents  
- Optional undo/redo  

## Naive solution

```java
button.onClick(() -> orderService.cancel(id));
// cannot queue, retry, or serialize the intent cleanly
```

## Pattern

Encapsulate an action in a command object with `execute()` (and maybe `undo()`); invoker runs commands without knowing receivers’ details.

## Implementation

```java
public interface Command {
    void execute();
}

public final class CancelOrderCommand implements Command {
    private final OrderId id;
    private final OrderService orders;
    public void execute() { orders.cancel(id); }
}

public final class CommandBus {
    public void dispatch(Command command) {
        // persist outbox, metrics, then:
        command.execute();
    }
}

// API layer
bus.dispatch(new CancelOrderCommand(orderId, orders));
```

Records as command DTOs + handlers is a common CQRS flavor of the same idea.

## Trade-offs

| + | − |
|---|---|
| Queue/retry/audit friendly | More types |
| Undo possible | Undo hard for distributed side effects |
| Invoker simplification | Can become anemic if overused |

## When to use

Job systems, GUI actions, transactional outbox messages, macros.

## When NOT to use

Trivial direct service call with no queue/audit/undo need.

## Production example

**Ops console:** “Retry charge” enqueued as `RetryChargeCommand` with idempotency key.

## Interview question

*Command vs method call? Command vs event? Where does idempotency live?*

**SOLID/LLD:** SRP; LLD async workflows.

### Related

[observer.md](./observer.md) · [chain-of-responsibility.md](./chain-of-responsibility.md)
