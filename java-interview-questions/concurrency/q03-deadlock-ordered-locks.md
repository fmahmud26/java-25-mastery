# Deadlock from lock ordering

## Question

`transfer(a,b)` locks `a` then `b`; another path locks `b` then `a`. Occasional freeze. Fix?

## Difficulty

Mid

## Expected answer

Impose global lock order (e.g., by accountId). Or use tryLock with timeout and retry. Detect with jstack/thread dump focusing on BLOCKED states.

## Common mistake

Adding more synchronized methods without ordering discipline.

## Follow-up

How does `ReentrantLock.tryLock` change the failure mode?
