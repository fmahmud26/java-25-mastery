# Observer

## Problem

When a subject’s state changes (order paid), many interested parties must react (email, analytics, inventory) without the subject knowing them all concretely.

## Forces

- Decouple publishers from subscribers (OCP)  
- Dynamic subscribe/unsubscribe  
- Ordering / failure isolation  
- Avoid god service calling everything  

## Naive solution

```java
order.markPaid();
email.send(...);
sms.send(...);
analytics.track(...);
inventory.reserve(...);
```

Subject grows with every side effect; hard to test; cyclical deps.

## Pattern

Subject notifies observers through a listener interface (or event bus). Modern systems often use domain events / messaging — same forces.

## Implementation

```java
public interface OrderEventListener {
    void onPaid(OrderPaid event);
}

public final class OrderService {
    private final List<OrderEventListener> listeners;
    public void markPaid(OrderId id) {
        OrderPaid event = repo.markPaid(id);
        for (OrderEventListener l : List.copyOf(listeners)) {
            l.onPaid(event); // or async / outbox in production
        }
    }
}

public final class EmailNotifier implements OrderEventListener {
    public void onPaid(OrderPaid e) { mail.sendReceipt(e.orderId()); }
}
```

## Trade-offs

| + | − |
|---|---|
| Decoupling | Notification order / reentrancy bugs |
| Easy to add listeners | Sync observers can stall the subject |
| Testable fakes | Memory leaks if forget unsubscribe |

## When to use

UI models, domain events, cache invalidation hooks, webhooks fan-out (in-process).

## When NOT to use

Need transactional consistency with the write — prefer outbox + message broker over in-process Observer.

## Production example

**Orders:** `OrderPaid` fans out to email + loyalty points; critical inventory uses transactional outbox instead of sync Observer.

## Interview question

*Observer vs pub/sub messaging? How do you prevent listener leaks? Sync vs async notification?*

**SOLID/LLD:** OCP + ISP (narrow events); LLD notification side effects.

### Related

[command.md](./command.md) · [facade.md](./facade.md)
