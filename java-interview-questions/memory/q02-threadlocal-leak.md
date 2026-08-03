# ThreadLocal leak

## Question

A filter sets `ThreadLocal` context but rarely clears in `finally`. Under pooled threads, heap grows. Why?

## Difficulty

Mid

## Expected answer

Thread pools reuse threads; ThreadLocal values stick until overwritten/removed. Always `remove()` in finally; prefer scoped context APIs.

## Common mistake

Clearing only on success path.

## Follow-up

How does this interact with virtual threads at high cardinality?
