# Structured concurrency in interviews (Java 25)

## Question

A candidate designs all fan-out with Structured Concurrency APIs as “Java 25 standard.” What’s the accurate Staff response?

## Difficulty

Staff

## Expected answer

Check preview/final status for the JDK you run. Structured concurrency has been preview across releases—**don’t claim finalized if still preview on Java 25**. Prefer CF/VT executors in production unless preview enabled consciously. Discuss cancellation/clarity benefits accurately.

## Reasoning

Principal-level accuracy on language evolution beats buzzwords.

## Follow-up

What problem does structured concurrency solve vs raw CF?

## Common mistake

Enabling preview flags silently in prod without policy.

## Principal-level discussion

Track JEPs in an internal compatibility matrix; allow preview only in labs; production uses stable APIs unless risk accepted by architecture board.
