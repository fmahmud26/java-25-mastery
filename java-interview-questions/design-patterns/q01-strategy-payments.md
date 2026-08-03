# Strategy for payment providers

## Question

You must support Stripe and Adyen with possible failover. Which pattern and why?

## Difficulty

Mid

## Expected answer

Strategy/port: `PaymentProvider` implementations + router. Open for new PSPs without editing callers (OCP).

## Common mistake

Giant `switch (provider)` in the service forever.

## Follow-up

Where does Circuit Breaker sit relative to Strategy?
