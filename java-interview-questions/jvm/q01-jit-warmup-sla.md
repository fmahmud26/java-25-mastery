# JIT warmup vs latency SLO

## Question

A canary at 1% traffic looks fine; full launch shows p99 regression for 15 minutes then recovers. Hypothesis?

## Difficulty

Senior

## Expected answer

Tiered compilation / warmup: early interpreted/C1 code slower; caches cold. Mitigate with warmup traffic, retention of profiles (when applicable), gradual ramp, don’t judge steady-state from first minutes.

## Reasoning

HotSpot optimizes based on profiles; uncommon paths stay cold.

## Follow-up

How do you warm a service before attaching to LB?

## Common mistake

Blaming only GC for the first 15 minutes without JFR.

## Principal-level discussion

Release engineering: mandatory soak/warmup gates; progressive delivery; document cold-start budgets for serverless separately from always-on JVMs.
