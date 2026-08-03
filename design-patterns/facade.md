# Facade

## Problem

Clients need a simple operation (“checkout”) but the subsystem is a graph of services (cart, inventory, payment, ledger, notifications).

## Forces

- Reduce coupling to many internals  
- Provide a stable use-case API  
- Hide orchestration complexity  
- Avoid a “god” that also owns all business rules forever  

## Naive solution

Controller calls 8 services in order, duplicating orchestration in each entrypoint (REST, batch, admin).

## Pattern

A facade offers coarse-grained methods that delegate to subsystem components.

## Implementation

```java
public final class CheckoutFacade {
    private final InventoryPort inventory;
    private final PaymentGateway payments;
    private final OrderRepository orders;
    private final DomainEvents events;

    public CheckoutResult checkout(CheckoutCommand cmd) {
        inventory.reserve(cmd.items());
        ChargeResult charge = payments.charge(cmd.payment());
        Order order = orders.createPaid(cmd, charge);
        events.publish(new OrderPaid(order.id()));
        return CheckoutResult.ok(order.id());
    }
}
```

## Trade-offs

| + | − |
|---|---|
| Simple client API | Facade can become a god class |
| Central orchestration | Extra layer for trivial CRUD |
| Easier entrypoint testing | Must still design internals well |

## When to use

Multi-step use cases; libraries with sprawling APIs (`Slf4j` isn’t Facade, but “simplify subsystem” is the idea); LLD checkout/booking.

## When NOT to use

Single repository call — don’t invent ceremony. Don’t use Facade to hide a missing domain model forever.

## Production example

**Travel booking:** `BookingFacade.book()` reserves seat, charges, emits tickets.

## Interview question

*Facade vs Adapter? Facade vs Application Service in DDD?*

**SOLID/LLD:** SRP at the edge; primary LLD pattern for use-case APIs.

### Related

[adapter.md](./adapter.md) · [patterns-and-lld.md](./patterns-and-lld.md)
