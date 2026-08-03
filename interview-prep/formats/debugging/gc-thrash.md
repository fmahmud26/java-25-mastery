# Debug: GC thrash with stable live set

## Symptoms

Frequent young GC; heap after GC small; allocation rate high.

## Your move

Allocation vs retention → JFR alloc samples → fix churn → Result (GC frequency ↓, p99).
