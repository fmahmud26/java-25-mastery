# Thread pool exhaustion

## Question

Tomcat/platform pool of 200 threads all blocked on a slow dependency; new requests queue then timeout. CPU is near idle. What’s happening and how do you mitigate?

## Difficulty

Senior

## Expected answer

Pool threads are stuck in blocking I/O—classic exhaustion. Mitigate: timeouts, bulkheads/separate pools, circuit breakers, shed load, virtual threads carefully with **bounded** DB/HTTP pools, fail fast. Idle CPU is a tell.

## Reasoning

Throughput limited by blocked workers, not CPU. Queues grow → latency SLO burn.

## Follow-up

How do you size a pool for blocking vs CPU work?

## Common mistake

Increasing pool to 5000 without bounding dependencies.

## Principal-level discussion

Platform standards for deadlines and bulkheads; golden signals for pool wait. Capacity models use Little’s law. Prefer VT for blocking but mandate pool limits on JDBC/HTTP.
