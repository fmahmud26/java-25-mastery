# PriorityQueue with mutable priorities

## Question

Tasks in a `PriorityQueue` have mutable priority fields. After changing priority, ordering looks wrong. Why?

## Difficulty

Senior

## Expected answer

PQ doesn’t re-heapify on external mutation. Changing priority after insert breaks heap invariant. Remove+reinsert, or use a structure supporting decrease-key, or immutable priority keys.

## Reasoning

Heap assumes comparisons stable unless structure updated via queue operations.

## Follow-up

How would you implement efficient “update score” for a large job scheduler?

## Common mistake

Calling `comparator` manually and expecting automatic reorder.

## Principal-level discussion

For schedulers at scale, pick an explicit data structure (Indexed heap, DelayQueue patterns, DB-backed queues). Document mutability rules in platform libraries; add tests that mutate and assert order.
