# G1 vs ZGC selection

## Question

Service has 40GB heap, p99 latency SLO 5ms for a trading-ish API, infrequent full-heap scans. How do you choose collector directionally?

## Difficulty

Senior

## Expected answer

Ultra-low pause → consider ZGC (generational ZGC in modern JDKs); G1 often default balanced choice. Measure—don’t creed. Note Java version features (generational ZGC). Footprint/CPU trade-offs matter.

## Reasoning

Collector choice is SLO + heap size + CPU budget + version capability.

## Follow-up

What changes if heap is 2GB and throughput matters more than 5ms?

## Common mistake

“ZGC always better.”

## Principal-level discussion

Standardize 1–2 collectors org-wide with playbooks; require load tests before collector swaps; keep flags in versioned runtime config.
