# Interview — Exception Handling

Strategy > syntax. Target **Java 25**.

---

## Core distinctions

| Topic | Sketch |
|-------|--------|
| Checked vs unchecked | Compiler forced vs not; wrap at boundaries |
| Exception vs Error | App/library vs JVM; don’t catch Error for business |
| try-with-resources | Auto close; suppressed on close failure |
| Propagation | Bubble until catch / thread handler |
| Translation | SQL/PSP → domain + cause |

---

## Scenario questions (answer with strategy)

### Payment failure
Decline vs timeout — Result vs exception? Retry? Idempotency?

### Database failure
Where catch SQLException? What client sees? When retry?

### Network timeout
Backoff? Jitter? Circuit breaker? What HTTP status?

### File failure
TWR? Quarantine? Suppressed close errors?

### Third-party API
4xx vs 5xx mapping? Logging PII? Metrics labels?

---

## Bad → improved (verbalize)

1. Empty catch  
2. Catch Exception log continue  
3. `throw new RuntimeException(e.getMessage())` dropping cause  
4. Retry all exceptions on capture  
5. Catch OOM return empty report  

---

## Principal Engineer discussions

1. How do exception types map to **SLOs** and alert routes?  
2. Design failure taxonomy for payments (transient/permanent/poison).  
3. Where do retries live — client SDK, service, message bus?  
4. How do you prevent duplicate captures under timeout+retry?  
5. What is your code-review bar for `catch (Exception)`?  
6. How should a worker handle interrupt vs task failure?  
7. Observability: structured logs + metrics + traces for one failed checkout — walk the fields.  

---

## Quick fire

| Q | A |
|---|---|
| finally always? | Not on kill/exit |
| Suppressed? | Close failures on TWR |
| Multi-catch? | Same handler, unrelated types |
| InterruptedException? | Restore interrupt flag |
| Custom exception musts? | Cause + context + clear type |

### Related

[README.md](./README.md) · [error-handling-strategy.md](./error-handling-strategy.md) · [retry-decisions.md](./retry-decisions.md) · [logging-and-observability.md](./logging-and-observability.md)
