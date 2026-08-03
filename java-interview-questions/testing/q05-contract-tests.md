# Contract tests between services

## Question

Staff strategy for avoiding “it broke in staging integration” across 20 services?

## Difficulty

Staff

## Expected answer

Consumer-driven contracts (or API schema CI), publish artifacts, provider verification in CI, versioning rules. Complements—not replaces—E2E sparse tests.

## Reasoning

E2E alone is slow/flaky; contracts catch breaking changes early.

## Follow-up

How do you handle additive vs breaking changes?

## Common mistake

Only manual Postman collections.

## Principal-level discussion

Make contracts part of paved path; break builds on incompatible provider changes; ownership of APIs with SLAs.
