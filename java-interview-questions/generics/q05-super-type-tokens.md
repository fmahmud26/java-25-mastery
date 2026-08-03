# Passing type parameters across erasure boundaries

## Question

A Staff engineer needs to deserialize JSON into `List<OrderLine>` at runtime. `OrderLine.class` isn’t enough for nested generics. What approaches exist and what are trade-offs?

## Difficulty

Staff

## Expected answer

Super type tokens (`new TypeToken<List<OrderLine>>(){}`), Jackson `TypeReference`, or explicit `JavaType`. Reflection on anonymous subclass retains generic signatures. Trade-off: verbosity, library coupling; don’t invent unsafe casts.

## Reasoning

Erasure deletes `OrderLine` from `List` at runtime unless something reifies the structure (subclass signature, explicit type descriptor).

## Follow-up

Why is `List.class` insufficient?

## Common mistake

`(List<OrderLine>) (List) json.parse(...)` without element validation.

## Principal-level discussion

Standardize on one JSON stack and TypeReference pattern in the paved path. Ban raw parse-to-Map-then-cast in services handling money. Contract tests for payload shapes.
