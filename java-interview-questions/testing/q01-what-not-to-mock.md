# Over-mocking the database

## Question

Unit tests mock the repository and always pass; production SQL fails. What’s wrong with the strategy?

## Difficulty

Mid

## Expected answer

Mocks don’t validate SQL/schema/constraints. Need repository integration tests (Testcontainers) for persistence; mocks for true unit logic.

## Common mistake

100% unit coverage with zero integration tests.

## Follow-up

What belongs in a pure unit test for a pricing function?
