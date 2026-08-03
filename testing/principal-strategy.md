# Principal Engineer Testing Strategy

## Decisions you own

### 1) Risk-based pyramid

Money, PII, authz, inventory → strongest net (unit + integration). Cosmetic UI copy → lighter.

### 2) Seam policy

- **Domain:** unit, real objects  
- **Outbound IO:** mock/stub in unit; real in IT/contract  
- **SQL:** Testcontainers, not H2-if-prod-is-Postgres (unless dialect-identical and accepted risk)

### 3) CI budgets

| Suite | Budget example |
|-------|----------------|
| Unit | seconds–1 min |
| IT | few minutes |
| Contract | on provider + consumer pipelines |
| E2E | scheduled / pre-release |

Fail the build on flake thresholds; assign owners.

### 4) Contract ownership

Consumer-driven contracts reviewed like API changes. Breaking provider = blocked merge.

### 5) Production feedback loop

Every Sev-2+ asks: *which test layer should have caught this?* Add that test in the same PR as the fix when feasible.

### 6) Coverage metrics

Coverage is a **flashlight**, not a goal. Prefer critical-path coverage over vanity %. See [test-coverage.md](./test-coverage.md).

## Anti-decisions

- Ban integration tests “for speed”  
- Require mocking all Spring beans forever  
- Keep quarantined flaky tests for months  

### Related

[test-strategy.md](./test-strategy.md) · [interview.md](./interview.md)
