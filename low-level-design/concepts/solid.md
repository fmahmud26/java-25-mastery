# SOLID — Decision Lens (not a checklist)

At Staff level, SOLID is used to **justify boundaries**, not to spray interfaces.

## SRP — one reason to change

**Ask:** If requirement X changes, how many types must edit?

- Fee rules change → only `PricingPolicy` implementations.  
- Spot layout changes → `Floor` / `SpotRegistry`, not payment.

**Smell:** `XManager` that parks, prices, pays, and notifies.

## OCP — extend without editing callers

**Ask:** Can a new variant ship as a new type wired in DI?

- New notification channel → new `Channel` adapter.  
- Rejected: growing `switch (channel)` in core.

## LSP — substitutes must honor contracts

**Ask:** Can every implementor of `PaymentProvider` be used where the interface is expected?

- A provider that sometimes charges twice violates the contract even if it “implements” the interface.  
- Document: idempotent `charge(IdempotencyKey, …)`.

## ISP — narrow ports

**Ask:** Do callers depend on methods they never use?

- Split `PaymentProvider` vs `RefundProvider` if refunds are rare/optional.  
- Rejected: 20-method `IParkingEverything`.

## DIP — depend on abstractions at boundaries

**Ask:** Does domain import Twilio/Stripe/JDBC?

- Domain depends on `NotificationSender`, `PaymentGateway`, `Clock`, `IdGenerator`.  
- Adapters live at the edge.

## Staff phrasing

“I’m applying OCP here because pricing varies by city; SRP so `ParkingService` orchestrates but doesn’t own fee math; DIP so tests inject a fixed `Clock`.”
