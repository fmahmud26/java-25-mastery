# High allocation, fine live set

## Question

GC runs frequently but heap after GC is stable and small. App still has latency blips. What’s likely?

## Difficulty

Mid

## Expected answer

High allocation rate → frequent young GCs. Optimize allocations (reuse, less boxing/churn) rather than only raising heap. Use JFR allocation samples.

## Common mistake

Jumping to CMS lore or random `-XX:+Use*` thrash.

## Follow-up

How do you see allocation rate in JFR?
