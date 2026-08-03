# Singleton pitfalls

## Question

Why is a lazy double-checked locking singleton easy to get wrong historically, and what’s safer in modern Java?

## Difficulty

Junior

## Expected answer

Historically visibility bugs without `volatile`. Prefer enum singleton, holder class, or DI container-managed single instance—avoid hand-rolled concurrency.

## Common mistake

Global mutable singleton hiding dependencies (test hell).

## Follow-up

How does DI change the need for classic singleton?
