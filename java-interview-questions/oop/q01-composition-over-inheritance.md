# Composition when inheritance bit us

## Question

`CachedUserService extends UserService` overrides `find` to add caching, but base class `find` calls `this.loadFromDb()` which subclasses also override—cache is skipped inconsistently. What’s the design failure?

## Difficulty

Mid

## Expected answer

Fragile base class / self-use inheritance problem. Prefer composition: `CachingUserService` wraps a `UserService` port (decorator). Don’t inherit to “reuse” implementation when behavior multiplies.

## Common mistake

Deep inheritance for reuse instead of delegation.

## Follow-up

How does the Decorator pattern apply here?
