# JEP 505 — Structured Concurrency (Fifth Preview)

| | |
|--|--|
| **JEP** | [505](https://openjdk.org/jeps/505) |
| **Status** | **Preview** — JDK 25 |
| **History** | Incubator 428/437; preview 453 → 462 → 480 → 499 → **505**; further preview work continues (e.g. JEP 525 listed as related) |

## Purpose

Treat related concurrent subtasks as one unit of work: clearer lifetimes, cancellation, error handling, observability.

## Problem Solved

`ExecutorService` + `Future` allow unstructured concurrency → thread leaks, cancelled parents with running children, poor thread-dump narrative.

## Previous Approach

```java
Future<String> user = executor.submit(this::findUser);
Future<Integer> order = executor.submit(this::fetchOrder);
return new Response(user.get(), order.get()); // failure/cancel semantics manual
```

## New Approach

Open a `StructuredTaskScope` via **static factory methods** (JDK 25 preview change — constructors not the primary API), `fork` subtasks, `join`, scope ends together.

```java
// Preview — requires --enable-preview
// Shape per JEP 505: StructuredTaskScope.open() / open(Joiner)
try (var scope = StructuredTaskScope.open()) {
    var user = scope.fork(this::findUser);
    var order = scope.fork(this::fetchOrder);
    scope.join();
    return new Response(user.get(), order.get()); // after successful join policy
}
```

Exact `Joiner` policies: see JEP 505 — default waits for all success or any failure.

## Syntax / API

Preview API in `java.util.concurrent`. Enable:

```bash
javac --release 25 --enable-preview Main.java
java --enable-preview Main
```

## Internal Behavior

Reifies parent/child task tree (excellent with virtual threads). Cancellation/shutdown policies applied to the subtree. Improves thread-dump understanding of “what is this request doing?”

## Production Example

Wait for production finalization before hard dependencies. In labs: fan-out user+order fetch with automatic cancel on failure.

## Limitations

- **Preview** — API already changed multiple times; expect more.  
- Not a full replacement for all `ExecutorService` patterns.  
- No channels/streaming concurrency in this JEP (non-goal).

## Migration Considerations

Keep behind preview flags; isolate in modules. Prefer virtual threads + clear timeouts. Plan rewrite when API finalizes.

## Interview Questions

1. Why is this still preview in 25?  
2. What changed vs earlier previews (factories / Joiner)?  
3. How does it fix thread leaks?  
4. Relation to Scoped Values?

### Related

[jep-506-scoped-values.md](./jep-506-scoped-values.md) · [../feature-status.md](../feature-status.md)
