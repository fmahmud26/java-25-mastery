# When to use Testcontainers

## Question

When are Testcontainers worth CI cost vs H2?

## Difficulty

Mid

## Expected answer

When behavior depends on real DB engine (SQL dialect, isolation, JSONB, locks). H2 for fast pure logic OK if dialect differences don’t matter—but don’t trust H2 for Postgres-specific features.

## Common mistake

H2 in CI, Postgres in prod, surprise SQL.

## Follow-up

How do you keep containers fast in CI?
