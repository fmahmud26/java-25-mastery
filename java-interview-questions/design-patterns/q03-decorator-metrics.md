# Decorator for cross-cutting metrics

## Question

You want timing metrics around every `InventoryRepository` call without modifying each method. Pattern?

## Difficulty

Mid

## Expected answer

Decorator/wrapper implementing the same port, delegating and recording metrics—or AOP/proxy. Keep business logic free of metric clutter.

## Common mistake

Copy-pasting timer code into every method.

## Follow-up

Decorator vs Proxy difference?
