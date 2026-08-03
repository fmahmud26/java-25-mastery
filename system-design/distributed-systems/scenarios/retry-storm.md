# Scenario: Retry Storm Cascade

## Production story

Search dependency latency rises to 2s. Checkout calls search sync for “related items.” Default client: 3 retries × 2s, no jitter, shared thread pool. Nodes tip over; core pay/inventory starved. Outage continues after search recovers due to retry backlog.

## What’s failing

Retries + shared bulkheads + sync nonessential dependency on critical path.

## Bad responses

- Increase timeouts further  
- More app pods (amplify search load)  
- Disable CB because “error rate high during incident”

## Principal response

1. Mitigate: feature-flag off search on checkout; CB open.  
2. Restart/clear saturated pools if needed.  
3. Fix: remove sync search from checkout; async or cached.  
4. Platform HTTP defaults: deadline budget, max retries 1 on non-idempotent-safe, full jitter, CB, **per-dep pools**.  
5. Load-test failure injection for deps.

## Trade-offs

Slightly worse UX (no related items) ≫ full checkout outage.

## Interview probes

- Calculate amplification: nodes × retries × fanout.  
- What’s a retry budget as % of traffic?  

Related: [../retry.md](../retry.md), [../backoff.md](../backoff.md), [../failure-handling.md](../failure-handling.md).
