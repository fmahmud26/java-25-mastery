# Compact object headers on Java 25

## Question

Java 25 includes compact object headers work. A Principal asks: “Should we enable it everywhere tomorrow for 20% RAM savings?”

## Difficulty

Staff

## Expected answer

Know product status vs default: compact headers may be available but **not necessarily default**. Require benchmarks on real heaps, compatibility testing, staged rollout, rollback plan. Don’t promise a fixed “20%” without measurement.

## Reasoning

Header layout changes interact with GC/JNI; savings workload-dependent.

## Follow-up

How do you measure live set before/after?

## Common mistake

Confusing “finalized/product” with “enabled by default.”

## Principal-level discussion

Flag governance for JVM ergonomics; performance council sign-off; track JEPs in a version matrix; celebrate wins only with capacity metrics.
