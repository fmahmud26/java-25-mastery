# Test Strategy

## Mental Model

```text
Many fast unit tests (domain rules)
  + fewer integration tests (DB, messaging, HTTP adapters)
  + selective contract / e2e / smoke
  = confidence without a 4-hour suite
```

Classic pyramid (adjust for your risk):

```text
        /\
       /E2E\        rare, critical journeys
      /------\
     / Integr \     real DB/queue/containers
    /----------\
   /    Unit     \  pure logic, edge cases
  /----------------\
```

## Goals

| Goal | Meaning |
|------|---------|
| Prevent regressions | Business rules, money, authz |
| Fast feedback | Unit on every commit |
| Production likeness | Integration with real Postgres/Kafka where it matters |
| Maintainability | Tests express intent; low brittle coupling |

## Anti-strategies

- 100% mock coverage theater  
- Only e2e (slow, flaky, opaque)  
- Testing framework internals instead of your code  
- No integration tests for SQL/JSON mapping  

## Outputs of a good strategy

Documented: what each layer covers, CI time budgets, flake policy, ownership of contract tests.

Deep PE version: [principal-strategy.md](./principal-strategy.md)

### Related

[what-to-unit-test.md](./what-to-unit-test.md) · [what-to-integration-test.md](./what-to-integration-test.md)
