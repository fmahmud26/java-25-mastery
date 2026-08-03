# Scenario: p99 regression after release

## Prompt

p99 80ms → 400ms after deploy; p50 flat; CPU +10%; error rate flat. How do you investigate?

## Spine

Hypotheses as Options (GC, lock, dep, N+1, cold cache). Trade-off of rollback vs dig. Decision: triage with traces/JFR. Result: attribute span before optimizing.

Related: [../debugging](../debugging/) · performance track.
