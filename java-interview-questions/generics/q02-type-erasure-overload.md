# Erasure and overloads

## Question

Why can’t you declare both `void f(List<String> x)` and `void f(List<Integer> x)`?

## Difficulty

Junior

## Expected answer

Type erasure makes both `f(List)` at runtime—signature clash. Overloads must differ after erasure.

## Common mistake

Thinking generics are reified like arrays.

## Follow-up

How do arrays differ regarding reification?
