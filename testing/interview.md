# Interview — Java Testing

## Strategy

Pyramid: many unit, fewer integration, rare e2e. Risk-based.

---

### What to unit test?

Domain rules, pure logic, edges. Fast, no real IO.

---

### What to integration test?

SQL, HTTP adapters, messaging, migrations — where mocks lie.

---

### What NOT to mock?

Domain model, class under test, real DB for SQL correctness; prefer fakes/stubs at ports.

---

### Mock vs stub vs fake?

Stub returns data; mock verifies interaction; fake is simplified working impl.

---

### Testcontainers?

Real Docker deps in tests — Postgres matching prod.

---

### Contract tests?

Consumer/provider agreement without full mesh e2e.

---

### Why prod breaks despite tests?

Over-mocking, H2≠Postgres, happy path only, flakes ignored, no concurrency/authz tests.

---

### PE one-liner

Right seam, real integrations where expensive, contracts between services, feedback from incidents into the suite.

### Related

[README.md](./README.md) · [principal-strategy.md](./principal-strategy.md) · [how-tests-fail-in-production.md](./how-tests-fail-in-production.md)
