# Scenario: Latency Cliff — p99 Melts Under Partial Failure

## Context

Average latency fine; p99/p999 explode when one dependency slows. Retries amplify. Circuit breaker absent or mis-tuned. Business sees “random slowness.”

## Constraints

- Multi-dependency checkout path  
- Error budget 99.9%; cannot fail all traffic on first blip  

## Options

| Option | Approach |
|--------|----------|
| **A. More retries** | Raise attempts / remove timeouts |
| **B. Timeouts + budgets** | Per-dep deadline; overall budget |
| **C. Bulkhead + CB** | Isolate pools; fail fast when unhealthy |
| **D. Async / degrade** | Non-critical deps off request path |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Apparent resilience | Retry storms; worse cliffs |
| B | Predictable failure | Need correct values |
| C | Blast-radius control | Tuning; false opens |
| D | Headroom | Eventual consistency UX |

## Decision

**B → C → D; never A alone.** p99 is a **budget**. Attribute spans; put timeouts inside the budget; bulkhead scarce threads/connections; degrade non-critical work.

## Reasoning

Tail latency is usually **queueing + retries + shared pools**, not average CPU. PE draws the critical path and names which dep ate the budget.

## Risks

- Timeout longer than caller’s patience  
- CB flapping without probe discipline  
- Cache stampede after CB open  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | Trace p99 breakdown | — |
| 1 | Timeouts trinity (connect/request/call) | Wrong values |
| 2 | Bulkheads + CB | Error budget burn |
| 3 | Degrade / async non-critical | Product reject |

## Success metrics

- p99 within SLO under dep +200ms injection test  
- Retry rate bounded; no thundering herd  

Related: [../../scenario-lab/07-high-latency.md](../../scenario-lab/07-high-latency.md) · [../../system-design/fundamentals/latency.md](../../system-design/fundamentals/latency.md) · [../../system-design/distributed-systems/circuit-breakers.md](../../system-design/distributed-systems/circuit-breakers.md)
