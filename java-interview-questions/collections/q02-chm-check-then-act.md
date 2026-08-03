# ConcurrentHashMap check-then-act bug

## Question

Code does `if (!map.containsKey(k)) map.put(k, create())` on a `ConcurrentHashMap` under VT load. Duplicates of `create()` side effects appear. Why, and what’s correct?

## Difficulty

Senior

## Expected answer

Race between contains and put. Use `computeIfAbsent`, `putIfAbsent`, or `merge`. CHM thread-safety doesn’t make multi-step compound actions atomic unless you use its atomic methods.

## Reasoning

Each call is thread-safe; the sequence isn’t. Classic check-then-act.

## Follow-up

Is `computeIfAbsent` allowed to run the mapping function concurrently for the same key? (Implementation-dependent historically—know current guarantees / don’t rely on side effects being once without care.)

## Common mistake

“CHM is thread-safe so my if/put is fine.”

## Principal-level discussion

Ban check-then-act on concurrent maps in code standards; provide examples in the paved path. For create-expensive values, consider single-flight patterns. Review SEVs from duplicate side effects (double email, double charge) as concurrency contract failures.
