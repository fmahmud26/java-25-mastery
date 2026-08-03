# Composition Over Inheritance

## Prefer composition when

- You’re assembling behaviors (cache + metrics + logging)  
- Lifecycle parts differ (Elevator has Door, Motor)  
- You’d otherwise inherit only to reuse code  

## Prefer inheritance / sealed hierarchies when

- True variant set (`Vehicle`, payment states as sealed)  
- Exhaustiveness matters in pattern matching  

## Staff example

`BookingService` **has** `SeatInventory`, `PaymentGateway`, `PricingPolicy` — it does not extend `PaymentGateway`.
