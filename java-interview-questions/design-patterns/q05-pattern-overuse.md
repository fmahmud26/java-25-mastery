# Pattern theater

## Question

A service has AbstractFactoryBeanStrategyVisitor for one concrete path. Staff critique?

## Difficulty

Staff

## Expected answer

YAGNI—patterns must earn complexity. Prefer simple code until variation appears. Patterns are tools, not goals.

## Reasoning

Indirection without variance harms maintainability and onboarding.

## Follow-up

When would Abstract Factory become justified?

## Common mistake

Equating “enterprise” with more layers.

## Principal-level discussion

Review culture rewards clarity; refactor to patterns when second implementation arrives; document rejected complexity in ADRs.
