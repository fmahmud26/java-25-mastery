# JEP 506 — Scoped Values

| | |
|--|--|
| **JEP** | [506](https://openjdk.org/jeps/506) |
| **Status** | **Final** (SE) — JDK 25 |
| **History** | Incubator/preview path: 429 → 446 → 464 → 481 → 487 → **506 final** |

## Purpose

Share **immutable** data with callees in a thread and with child threads, with clearer lifetime than `ThreadLocal`, and lower cost especially with virtual threads / structured concurrency.

## Problem Solved

Deep call chains and frameworks need request/context data without adding parameters everywhere. `ThreadLocal` is mutable, easy to leak on pooled threads, and expensive at huge thread counts.

## Previous Approach

- Pass context through every method signature, or  
- `ThreadLocal` / `InheritableThreadLocal` (mutation, leaks, unclear lifetime)

## New Approach

`ScopedValue` bound for a syntactic scope via `ScopedValue.where(...).run(...)` / `call(...)`. Value is immutable for the binding’s lifetime; automatically unbound when the scope exits.

## Syntax / API

```java
private static final ScopedValue<RequestContext> CTX = ScopedValue.newInstance();

void serve(Request req) {
    var ctx = RequestContext.from(req);
    ScopedValue.where(CTX, ctx).run(() -> application.handle(req));
}

UserInfo readUserInfo() {
    RequestContext ctx = CTX.get(); // legal only inside binding
    return framework.readKey("userInfo", ctx);
}
```

Finalization note (JEP 506): `ScopedValue.orElse` no longer accepts `null` as its argument.

## Internal Behavior

Designed for cheap large-scale sharing with virtual threads; bindings nest; child threads started in structured ways can inherit bindings per API rules. Prefer over ThreadLocal when data is one-way immutable context.

## Production Example

Web framework binds authenticated user + trace id for the request; service code and JDBC helpers read `CTX.get()` without signature noise; no `remove()` finally needed for correctness of unbinding (scope exit clears binding).

## Limitations

- Not a drop-in replacement for every `ThreadLocal` (mutable per-thread caches differ).  
- Must be bound before `get()` — unbound access fails.  
- Does not replace method parameters for ordinary dataflow.

## Migration Considerations

Introduce at framework boundaries first. Keep ThreadLocal where mutation is required until redesigned. Works well with virtual threads (JDK 21+) and preview structured concurrency.

## Interview Questions

1. ScopedValue vs ThreadLocal?  
2. Why immutable bindings?  
3. Why finalize in 25 after several previews?  
4. Interaction with virtual threads?

### Related

[jep-505-structured-concurrency.md](./jep-505-structured-concurrency.md) · [../feature-status.md](../feature-status.md)
