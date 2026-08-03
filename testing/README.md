# Testing — Production-Grade Java Guide

Tests are a **product risk control**: they should catch bugs that matter, stay maintainable, and match how the system actually runs.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Strategy: [test-strategy](./test-strategy.md) · [what-to-unit-test](./what-to-unit-test.md) · [what-to-integration-test](./what-to-integration-test.md) · [what-not-to-mock](./what-not-to-mock.md)  
2. Techniques: [unit-testing](./unit-testing.md) · [integration-testing](./integration-testing.md) · [test-doubles](./test-doubles.md) · [mocking](./mocking.md) · [assertions](./assertions.md) · [parameterized-tests](./parameterized-tests.md)  
3. Cross-service: [testcontainers](./testcontainers.md) · [contract-testing](./contract-testing.md)  
4. Reality: [how-tests-fail-in-production](./how-tests-fail-in-production.md) · [service-examples](./service-examples.md) · [principal-strategy](./principal-strategy.md) · [interview](./interview.md) · [tools/](./tools/)

## One-line PE rule

**Test behavior at the right seam — mock IO boundaries, not your own domain model; prove integrations with real collaborators when the bug would be expensive.**
