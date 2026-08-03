# Blind GC tuning

## Question

On-call pastes 15 historic GC flags from a blog into prod overnight. Staff response?

## Difficulty

Staff

## Expected answer

Revert; establish baseline JFR/GC logs; change one variable at a time with load tests; prefer fixing allocation/retention; document allowed flags per JDK.

## Reasoning

Copied flags often target obsolete collectors or different heaps—can worsen pauses.

## Follow-up

What’s a minimal useful GC log config on Java 17+?

## Common mistake

Equating “more flags” with expertise.

## Principal-level discussion

Runtime config ownership; ban unknown `-XX` without RFC; continuous capacity testing in CI for critical services.
