# String concatenation in a loop

## Question

Building a large string with `s += chunk` in a tight loop is slow. Fix?

## Difficulty

Junior

## Expected answer

Use `StringBuilder` (or `StringJoiner`/`Collectors.joining`). `+=` creates many intermediate `String`s (compiler may rewrite simple cases, not all loop patterns optimally historically—still write builders intentionally).

## Common mistake

Micro-optimizing unrelated code first.

## Follow-up

Is `StringBuffer` needed?
