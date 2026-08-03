# False sharing on adjacent counters

## Question

Two threads update adjacent `long` fields in a shared object; throughput collapses on multi-core. Suspect?

## Difficulty

Senior

## Expected answer

False sharing: same cache line invalidated across cores. Pad/align (`@Contended`), use `LongAdder`/striping, or separate objects.

## Reasoning

Coherence traffic, not logical lock contention.

## Follow-up

How do you confirm with perf counters / JFR?

## Common mistake

Adding more synchronized without measuring.

## Principal-level discussion

Rare but real in mechanical sympathy hotspots; only pursue with evidence; teach `@Contended` carefully (may need flags).
