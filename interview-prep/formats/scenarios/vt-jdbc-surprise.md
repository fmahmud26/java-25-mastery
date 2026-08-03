# Scenario: VT adopted, latency worse

## Prompt

Moved to virtual threads; Hikari still 10; p99 worse with pool waits. What went wrong?

## Decision shape

VT ≠ DB capacity; admit/queue; size pool from DB; Result = pool wait metric.

## Deep sources

- Bank: [../../../java-interview-questions/virtual-threads/q02-vt-and-jdbc-pool.md](../../../java-interview-questions/virtual-threads/q02-vt-and-jdbc-pool.md)
- PE: [../../../principal-engineer/scenarios/connection-pool-exhaustion.md](../../../principal-engineer/scenarios/connection-pool-exhaustion.md)
- Lab: [../../../scenario-lab/06-database-pool-exhaustion.md](../../../scenario-lab/06-database-pool-exhaustion.md)
- Chapter: [../../../virtual-threads/database-connection-pools.md](../../../virtual-threads/database-connection-pools.md)
