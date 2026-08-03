# Testing LLD — What to Prove

You don’t need a full test suite in the interview — you need to show **what is deterministic and what is faked**.

## Prefer

| Test | Why |
|------|-----|
| Domain rules pure | Fee calc, state transitions, limit allow/deny |
| In-memory fakes for ports | Payment, clock, id gen, notifier |
| Concurrency stress on inventory | Two threads one seat → one winner |
| Expiry / timeout paths | Hold TTL, session timeout |

## Inject always

- `Clock` / `InstantSource`  
- `IdGenerator`  
- Ports: `PaymentGateway`, `NotificationSender`, `CashDispenser`

## Avoid

- Static singletons with mutable config  
- `Thread.sleep` as synchronization  
- Hitting real Stripe in unit tests  

## Staff phrasing

“I’d unit-test `PricingPolicy` with a fixed clock, fake the gateway to assert idempotency keys, and run a parallel park-on-same-spot test proving one failure.”
