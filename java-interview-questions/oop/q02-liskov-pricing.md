# Liskov violation in discount types

## Question

`PremiumDiscount extends Discount` overrides `apply` to throw if cart currency ≠ USD, while callers of `Discount` assume all implementations work for any cart. Production sees unexpected exceptions after a “simple subtype.” Diagnose.

## Difficulty

Senior

## Expected answer

LSP break: subtype strengthens preconditions. Callers written to `Discount` are no longer safe. Fix: model restriction in the type (separate interface), return `Result`/empty, or make currency part of the abstraction.

## Reasoning

Substitution safety is behavioral, not just “extends.” Exceptions and narrowed inputs break polymorphism.

## Follow-up

How would you test for LSP-ish safety in a suite?

## Common mistake

“It compiles, so polymorphism is fine.”

## Principal-level discussion

Encode variant behavior with sealed interfaces / strategy objects reviewed in API guidelines. Ban “throw from override for unsupported” without documenting that the base type allows it. Architecture reviews check subtype contracts when shared libraries cross teams.
