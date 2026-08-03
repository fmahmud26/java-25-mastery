# HashMap growth on the hot path

## Question

A request handler builds a `HashMap` and inserts ~50k entries per call with default capacity. CPU profiles show `resize`/`transfer` dominating. What do you change?

## Difficulty

Mid

## Expected answer

Pre-size: `new HashMap<>(expected)` or `ensure`-style sizing; avoid rehash per request. Consider whether 50k map allocations per request belong on the hot path at all (reuse buffers, streaming, DB-side aggregation).

## Common mistake

Switching to `Hashtable` or synchronizing without fixing sizing/alloc.

## Follow-up

What’s the role of load factor 0.75?
