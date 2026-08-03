# Deoptimization storm

## Question

JFR shows repeated deoptimizations correlating with latency spikes after a config flag flips uncommon paths to hot. What’s going on?

## Difficulty

Senior

## Expected answer

Speculative optimizations invalidated when profiles change → deopt to interpret/recompile. Sudden path changes (feature flags) can cause storms. Stabilize profiles; avoid flipping hot code shapes constantly.

## Reasoning

JIT bets on observed types/branches; wrong bets are undone costly.

## Follow-up

How do uncommon traps relate?

## Common mistake

Treating deopt as “JVM bug” instead of profile change.

## Principal-level discussion

Feature flag hygiene: don’t reshape hottest loops casually; canary with profile awareness; investigate with JFR Compilation/Deopt events.
