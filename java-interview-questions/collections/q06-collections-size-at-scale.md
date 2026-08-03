# Unbounded collections as outage amplifiers

## Question

A service buffers inbound Kafka records in an `ArrayList` “until downstream recovers.” After a 20-minute outage, JVM OOMs. What should Staff engineering standards require?

## Difficulty

Staff

## Expected answer

Bounded buffers with backpressure (block, drop with metrics, or spill). Never unbounded in-memory accumulation for recovery. Prefer consumer pause, durable queue, or disk spill. Alert on buffer size.

## Reasoning

Memory is finite; recovery time × rate = disaster. Unbounded lists convert dependency outages into self-DoS.

## Follow-up

How do you choose bound size relative to SLO and heap?

## Common mistake

Increasing heap as the “fix.”

## Principal-level discussion

Platform defaults: bounded queues in templates; load-shed policies; runbooks. Error budgets include “don’t OOM to protect a dependency.” Review designs for any `List` growth without eviction.
