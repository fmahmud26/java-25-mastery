# Virtual threads and JDBC pool sizing

## Question

After adopting VT, the app creates huge concurrency but HikariCP is still max 20. Latency explodes with pool wait. What was misunderstood?

## Difficulty

Senior

## Expected answer

VT remove thread scarcity, not connection scarcity. Pool still bounds DB. Size pools from DB capacity; use backpressure; don’t equate VT count with safe DB concurrency.

## Reasoning

Little’s law: in-flight queries ≤ pool. Excess VT wait on pool acquire.

## Follow-up

What metrics show pool wait vs query time?

## Common mistake

Setting Hikari to 10,000 to “match VT.”

## Principal-level discussion

Capacity planning doc: DB `max_connections`, app instances × pool. Platform templates warn on VT+JDBC. Prefer queueing work over inflating pools.
