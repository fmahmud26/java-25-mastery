# Debug: Thread pool exhaustion

## Symptoms

CPU ~5%; load average high; p99 huge; active threads = max; many BLOCKED/WAITING on HTTP client.

## Your move (speak)

Hypotheses → which metric/thread dump confirms → mitigate (timeouts/shed) → fix (bulkhead/CB) → Result.
