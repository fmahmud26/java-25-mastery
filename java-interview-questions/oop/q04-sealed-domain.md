# Sealed types for payment states

## Question

Why model payment status as a `sealed interface` hierarchy (or sealed + records) instead of an enum plus switch with default?

## Difficulty

Mid

## Expected answer

Sealed hierarchies + pattern switch give exhaustiveness checking when states carry different data (`Captured(ref)`, `Failed(reason)`). Enums alone don’t attach payloads cleanly; non-sealed allows rogue subtypes.

## Common mistake

`default` branch that silently ignores new states.

## Follow-up

How do sealed types interact with libraries that reflectively subclass?
