# volatile vs AtomicInteger

## Question

When is `volatile int` insufficient for a counter incremented by many threads?

## Difficulty

Junior

## Expected answer

`volatile` gives visibility, not atomic read-modify-write. Use `AtomicInteger`/`LongAdder` for increments. volatile fine for flags/publication of immutable snapshots.

## Common mistake

Using volatile for `count++`.

## Follow-up

What does AtomicInteger use internally?
