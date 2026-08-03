# Humongous object pain on G1

## Question

G1 logs show humongous allocations; old gen fragmentation-like symptoms. Cause?

## Difficulty

Senior

## Expected answer

Objects ≥ region size/2 treated humongous—expensive, can cause premature GCs/fragmentation. Shrink allocations (buffers), tune region size carefully, or avoid giant contiguous arrays when possible.

## Reasoning

Humongous path bypasses normal eden ergonomics.

## Follow-up

How do you find the allocating call sites?

## Common mistake

Blindly maxing heap.

## Principal-level discussion

Coding standards against multi-MB temporary arrays on request path; prefer chunked I/O; monitor humongous metrics in golden dashboards.
