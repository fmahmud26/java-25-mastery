# Operators and Pipelines

Reactive libraries turn the Reactive Streams SPI into **composable operators**. Mental model matters more than memorizing every operator name.

## Mental Model

```text
source  →  transform  →  fan-in/out  →  side-effect  →  subscribe
  |           |              |              |
 cold/hot   map/filter   flatMap/merge   doOnNext     demand starts here
```

Nothing runs until someone **subscribes** (cold publishers). Hot sources (shared streams) emit regardless of late subscribers — know which you have.

## Core Operator Families

| Family | Intent | Watch for |
|--------|--------|-----------|
| `map` / `filter` | 1:1 sync transform | Heavy CPU on event-loop thread |
| `flatMap` | 1:N async fan-out | **Concurrency** — unbounded flatMap storms |
| `concatMap` | 1:N sequential | Latency; preserves order |
| `switchMap` | Cancel previous inner | Latest-only UX (typeahead) |
| `merge` / `zip` / `combineLatest` | Multi-source | Failure/cancel semantics differ |
| `publishOn` / `subscribeOn` | Thread hop | Blocking work placement |
| `buffer` / `window` / `sample` | Shape time/space | Memory vs freshness |

*(Names are Reactor-flavored; RxJava is similar with different spelling.)*

## flatMap Concurrency — the Principal trap

```text
// Conceptual — unbounded inner subscriptions
orders.flatMap(o -> charge(o))   // 50k in-flight charges → PSP / pool death

// Safer: cap concurrency (library-specific API)
orders.flatMap(o -> charge(o), /* concurrency */ 32)
```

Illustrative example: concurrency 32 vs unbounded often separates “healthy lag” from “retry storm.”

## Code Sketch (conceptual Reactor)

```java
// Conceptual — Project Reactor / WebFlux ecosystem, not JDK API
Flux<Order> flux = orderSource
        .filter(Order::isOpen)
        .flatMap(this::enrich, 16)
        .map(this::toView)
        .timeout(Duration.ofSeconds(2));
```

Prefer timeouts and bounded concurrency on every remote hop.

## Debugging Cost

| Sync / VT stack | Reactive pipeline |
|-----------------|-------------------|
| One clear stack | Async boundaries; operator assembly vs execution |
| Thread dump shows waiters | Need reactor debug / checkpoint / micrometer tags |

Invest in correlation IDs and operator checkpoints **before** the first SEV-1.

## Production Scenario — search typeahead

`switchMap` cancels stale queries when the user types again → lower DB load. Using `flatMap` instead keeps old queries running → wasted capacity and confusing UI order.

## When Not to Use Heavy Operator Graphs

Simple request → JDBC → JSON: VT (or plain servlet) usually wins on readability and hireability. Operators earn their keep for **streams**, fan-in, and demand control.

## Principal Perspective

Operators are a **domain-specific language for async flow**. Measure concurrency, cancel behavior, and memory — not just “we’re reactive so we’re scalable.”

### Related

[backpressure.md](./backpressure.md) · [production-pitfalls.md](./production-pitfalls.md) · [vs-virtual-threads.md](./vs-virtual-threads.md)
