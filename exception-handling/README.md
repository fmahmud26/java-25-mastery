# Exception Handling — Strategy Guide (Java 25)

Exceptions are a **control and signaling mechanism**. Syntax is cheap; **where you catch, what you log, what you retry, and what you translate** decides whether a payment system recovers or lies.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Model: [exception-hierarchy](./exception-hierarchy.md) → [errors](./errors.md) → [checked-exceptions](./checked-exceptions.md) → [unchecked-exceptions](./unchecked-exceptions.md)  
2. Mechanics: [try](./try.md) · [catch](./catch.md) · [finally](./finally.md) · [throw](./throw.md) · [throws](./throws.md) · [multi-catch](./multi-catch.md)  
3. Resources: [try-with-resources](./try-with-resources.md) → [suppressed-exceptions](./suppressed-exceptions.md)  
4. Design: [custom-exceptions](./custom-exceptions.md) → [exception-propagation](./exception-propagation.md) → [exception-translation](./exception-translation.md)  
5. Operations: [error-handling-strategy](./error-handling-strategy.md) → [logging-and-observability](./logging-and-observability.md) → [retry-decisions](./retry-decisions.md)  
6. Drill: [interview.md](./interview.md)

## Failure scenarios in this folder

| Scenario | Typical signal | Strategy sketch |
|----------|----------------|-----------------|
| Payment failure | Domain / PSP errors | Translate → no blind retry on business decline |
| Database failure | SQLException / timeout | Retry transient; trip circuit; don’t leak SQL |
| Network timeout | SocketTimeout / HTTP | Retry with backoff + idempotency key |
| File failure | IOException | Fail request or quarantine file; TWR |
| Third-party API | 4xx/5xx / client ex | Map status → domain; retry only 429/5xx policy |

## Principal stance

Catch at **boundaries** (HTTP adapter, message consumer, batch job). Keep domain code free of JDBC/PSP types. Log with correlation ids. Metrics on failure class. Prefer **explicit results** for expected business outcomes (`declined`) and exceptions for **unexpected / contract-breaking** paths.
