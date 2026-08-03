# Anemic inventory under concurrency

## Question

Inventory is updated by three services setting `item.quantity` via setters after reading. Oversell occurs. How does richer domain modeling help?

## Difficulty

Senior

## Expected answer

Move invariant into the aggregate: `inventory.reserve(n)` does conditional decrement (`if qty>=n`) atomically at the persistence layer (version/CAS). Anemic setters scatter invariants across callers—races follow.

## Reasoning

Consistency belongs with the data owner. Domain methods + DB constraints beat distributed setter etiquette.

## Follow-up

Optimistic locking vs `UPDATE … WHERE quantity >= ?`?

## Common mistake

More synchronized blocks in each service instead of single-writer aggregate rules.

## Principal-level discussion

Declare single-writer ownership for inventory; publish APIs not tables. Staff/Principal enforce via schema grants and service boundaries—not coding tips alone.
