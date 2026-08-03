# Circuit Breakers (Distributed Context)

Stop calling a sick dependency so pools and error budgets survive.

## States

`CLOSED → OPEN (fail fast) → HALF_OPEN (probe) → CLOSED|OPEN`

## Failure focus

- CB on business 4xx → opens incorrectly  
- No bulkhead → one dep still exhausts shared pool before CB trips  
- Fallback invents success for payments  

## Trade-offs

Fail fast (user errors) vs hammering (prolonged outage). Prefer fail fast + degrade safely.

Related: [retry.md](./retry.md), [failure-handling.md](./failure-handling.md), [fault-tolerance.md](./fault-tolerance.md).
