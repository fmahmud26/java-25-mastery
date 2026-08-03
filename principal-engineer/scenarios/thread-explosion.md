# Scenario: Thread / Task Explosion and Pool Starvation

## Context

Latency cliffs under load. Dumps show hundreds–thousands of threads waiting on the same pool’s `Future.get`, or unbounded VT/tasks queued. Nested submit to the same executor. Or “fix” was raising pool size again.

## Constraints

- Shared FJP / commonPool may also run parallel streams  
- Downstream DB/HTTP cannot absorb unbounded concurrency  

## Options

| Option | Approach |
|--------|----------|
| **A. Raise pool size** | More workers |
| **B. Separate pools** | CPU vs blocking; never block a pool on itself |
| **C. Bound fan-out** | Semaphore, queues, structured scopes |
| **D. Non-blocking composition** | CF with proper executor; avoid `get` on worker |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Short relief | Hides deadlock/starvation; burns memory |
| B | Isolation | More ops surface |
| C | Stability | Requires admission UX |
| D | Throughput under load | Code change |

## Decision

**B + C + D; A only with a measured model.** Same-pool recursive blocking is a design bug. Unbounded VT submit is still a dependency bomb ([connection-pool-exhaustion](./connection-pool-exhaustion.md)).

## Reasoning

Starvation and explosion are **scheduling topology** problems. PE names the executor graph and the scarce resource (cores, connections, permits).

## Risks

- Blocking I/O on `ForkJoinPool.commonPool()`  
- VT explosion mistaken for “need more carriers”  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | Dump: who waits on whom | — |
| 1 | Split executors; remove self-join | Deadlock remains |
| 2 | Bound concurrency; timeouts | Dep overload |
| 3 | Load test pool metrics | — |

## Success metrics

- No self-deadlock under stress  
- Queue depth / active threads bounded  
- p99 stable when RPS rises  

Related: [../../scenario-lab/04-thread-starvation.md](../../scenario-lab/04-thread-starvation.md) · [../../concurrency/](../../concurrency/)
