# Records vs classes for domain models

## Question

When should you **not** model a domain entity as a `record`?

## Difficulty

Mid

## Expected answer

When you need identity mutation, JPA-style no-arg + proxies, inheritance hierarchies, or cyclical mutable graphs. Records are shallowly immutable nominal tuples—great for DTOs/values, poor for classic mutable entities.

## Common mistake

Forcing records into JPA entities for “modern Java” aesthetics.

## Follow-up

Can a record implement an interface? Implement sealed interfaces?
