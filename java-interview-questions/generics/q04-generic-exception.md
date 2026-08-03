# Generic checked exceptions

## Question

Why can’t you create a generic class `class Fail<T extends Exception>` and freely `throw t` as a checked exception design pattern without caveats?

## Difficulty

Mid

## Expected answer

Erasure and catch limitations make generic checked exceptions awkward; type variables aren’t allowed in catch; sneaky throw patterns exist but are discouraged. Prefer explicit exceptions or unchecked wrappers.

## Common mistake

Copying “sneaky throw” utilities into business logic.

## Follow-up

How does `Callable` declare checked exceptions vs `Runnable`?
