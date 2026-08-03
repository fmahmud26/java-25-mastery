# Home-grown microbench lies

## Question

Engineer claims “new serializer is 40% faster” using a loop + `nanoTime` without warmup. Critique.

## Difficulty

Mid

## Expected answer

No warmup, DCE risk, GC noise, single shot—invalid. Use JMH with forks/warmup; validate with JFR/prod metrics.

## Common mistake

Publishing blog numbers from `main`.

## Follow-up

What is a blackhole in JMH?
