# Maintainability

## Purpose

Minimize cost of **safe change** over years — the real enterprise metric.

## Before

God classes, hidden globals, copy-paste pricing in 4 services, no tests on refunds.

## After

- Clear module boundaries (api/domain/adapters)  
- Shared libraries versioned intentionally  
- Tests at risk seams  
- Runbooks + ownership in CODEOWNERS  
- Delete or quarantine dead flags  

## Levers

| Lever | Effect |
|-------|--------|
| Naming & structure | Find code fast |
| Immutability & types | Prevent illegal states |
| Tests | Courage to refactor |
| Observability | Diagnose without guesswork |
| Small PRs | Review quality |

## Trade-offs

Abstractions for hypothetical futures vs YAGNI. Premature platforms tax every team.

## PE Decision

Budget refactoring when change latency rises; measure lead time for money-path changes.

### Related

[clean-code.md](./clean-code.md) · [api-design.md](./api-design.md) · [trade-offs.md](./trade-offs.md)
