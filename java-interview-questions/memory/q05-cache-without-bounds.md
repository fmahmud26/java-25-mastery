# Unbounded cache as a product decision

## Question

Product wants “cache everything forever for speed.” Memory incidents recur. Principal framing?

## Difficulty

Principal

## Expected answer

Refuse unbounded caches. Define hit-rate SLO, max size, TTL, eviction, stampede control, cost of staleness. Provide Redis/Caffeine with bounds as paved path. Tie memory error budget to cache policy.

## Reasoning

Infinite cache is infinite retained set—conflicts with finite heaps and correctness.

## Follow-up

When is “disable cache” the correct incident mitigate?

## Common mistake

Blaming GC for a policy problem.

## Principal-level discussion

Architecture review gate: every cache lists bound + invalidation. Cost dashboards for cache cluster $. Educate PMs on staleness vs RAM trade-off in business language.
