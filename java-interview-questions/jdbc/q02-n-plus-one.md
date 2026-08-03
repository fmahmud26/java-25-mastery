# N+1 query pattern

## Question

Listing 100 orders triggers 101 SQL calls (1 + per-line items). Fix options?

## Difficulty

Mid

## Expected answer

Join fetch / batch select by ids / denormalized read model. Measure with SQL logging. Avoid lazy load in loops.

## Common mistake

Caching as first fix without reducing queries.

## Follow-up

How does this appear in APM?
