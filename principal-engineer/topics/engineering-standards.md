# Engineering Standards

Standards encode **learned failure** as defaults — enforceable where it matters.

## Worth standardizing

| Area | Example standard |
|------|------------------|
| HTTP clients | Timeouts, retries with jitter, max retry budget |
| Money APIs | Idempotency-Key required |
| Logging | JSON + trace id; no PII fields |
| Schema | Expand/contract only; no break without version |
| On-call | SLO-based paging; runbook URL in alert |
| Data stores | Ownership tag; backup verified |

## Enforcement levels

1. Doc only (weak)  
2. Template defaults (better)  
3. CI arch tests / lint (strong)  
4. Gateway policy (strongest for edge)  

## PE anti-pattern

50-page standards nobody reads. Prefer **10 rules that blocked last year’s SEVs** plus paved road.

Related: [operational-excellence.md](./operational-excellence.md), [technical-influence.md](./technical-influence.md).
