# Interview — Functional Programming

Java 25. Prefer **imperative vs functional** comparisons and **when not to**.

---

## Core Q&A

### Lambda vs anonymous class?

SAM + `invokedynamic` vs synthetic class; `this` differs; prefer lambdas for SAMs.

### Functional interface rules?

One abstract method; defaults/statics OK; `@FunctionalInterface` validates.

### Effectively final?

Captured locals can’t be reassigned; use reduce/collect instead of mutating counters.

### Method reference kinds?

Static, bound, unbound, constructor/array.

### andThen vs compose?

`f.andThen(g)` = g(f(x)); `f.compose(g)` = f(g(x)). Prefer andThen for pipelines.

### Predicate / Function / Consumer / Supplier?

test / apply / accept (effects) / get (lazy).

### UnaryOperator / BinaryOperator?

Same-type Function / same-type BiFunction — replaceAll, reduce, merge.

---

## Imperative vs functional (say both)

Filter+map payments; show for-loop and stream. Mention debugging and exceptions.

## Performance / readability

Boxing, parallel misuse, lambda size limits, method refs for clarity, `orElse` vs `orElseGet`.

## When FP is worse

Deep nesting, side effects in `map`, parallel + mutation, transactional workflows as Function chains.

---

## Principal Engineer scenarios

1. **Pricing:** Compose `UnaryOperator<Money>` vs `PricingService` — criteria to switch.  
2. **Fraud:** `List<Predicate<Payment>>` with metrics per rule — how to observe failures.  
3. **Listeners:** `Consumer` fan-out vs outbox events — reliability.  
4. **Capture pipeline:** Keep validation pure; isolate PSP I/O — sketch modules.  
5. **Hot path:** Stream of millions of ints boxing — what do you change?  
6. **Closure leak:** Lambda listener captures `@RestController` — diagnosis.  
7. **Review bar:** Ban side effects in `map`/`filter`? Allow logging?

---

## Rapid fire

| Q | A |
|---|---|
| Side effect in map? | Avoid — use forEach/service |
| Closure? | Lambda + captured env |
| HOF? | Takes/returns functions |
| identity()? | `t -> t` |
| Primitive FI why? | Avoid boxing |

### Related

[README.md](./README.md) · [when-to-use.md](./when-to-use.md) · [side-effects.md](./side-effects.md) · [effectively-final.md](./effectively-final.md)
