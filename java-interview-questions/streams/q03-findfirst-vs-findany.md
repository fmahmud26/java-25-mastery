# findFirst vs findAny

## Question

When would you use `findAny` instead of `findFirst`?

## Difficulty

Junior

## Expected answer

`findAny` allows less deterministic short-circuit—useful in parallel pipelines when any match is fine. `findFirst` respects encounter order, which can be more expensive in parallel.

## Common mistake

Using `findFirst` in parallel by habit when order doesn’t matter.

## Follow-up

What does encounter order mean for a `HashSet` stream?
