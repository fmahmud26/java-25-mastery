# Downstream collectors

## Question

You need `Map<Dept, Double>` average salary. How do you compose collectors?

## Difficulty

Mid

## Expected answer

`Collectors.groupingBy(Employee::dept, Collectors.averagingDouble(Employee::salary))` — downstream collector aggregates per group.

## Common mistake

Grouping to lists then streaming again without need (extra alloc).

## Follow-up

`groupingBy` vs `groupingByConcurrent`?
