# ThreadLocal under virtual threads

## Question

Request context in `ThreadLocal` “randomly” disappears or memory climbs with VT executors. Causes?

## Difficulty

Senior

## Expected answer

`newVirtualThreadPerTaskExecutor()` creates a **new virtual thread per task** — not platform-pool worker reuse. Primary issues: (1) ThreadLocal values do **not** automatically propagate across task/VT boundaries; (2) large ThreadLocal state × **in-flight** VT count burns heap; (3) framework MDC/copy bugs. Prefer **Scoped Values (JEP 506 — Final on Java 25)** for immutable request context, or pass context explicitly. Always `remove()` when you must use ThreadLocal.

## Reasoning

The classic “ThreadLocal sticks on reused pool workers” story is the wrong lead for per-task VTs. Scale and propagation dominate.

## Follow-up

ScopedValue vs ThreadLocal trade-offs on Java 25?

## Common mistake

Copying MDC ThreadLocal patterns unchanged into VT-heavy code without verifying propagation and memory.

## Principal-level discussion

Standardize context propagation (trace/baggage) via framework instrumentation; discourage new ThreadLocal in services; migrate MDC with vendor guidance; measure heap under peak in-flight VTs.
