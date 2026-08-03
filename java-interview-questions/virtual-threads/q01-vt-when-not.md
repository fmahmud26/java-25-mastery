# When virtual threads don’t help

## Question

A CPU-bound image hashing loop moves to VT per task and throughput drops slightly. Why?

## Difficulty

Mid

## Expected answer

VT don’t add CPU cores. Extra scheduling overhead can hurt pure CPU. Use platform pool sized to CPU for compute; VT for blocking concurrency.

## Common mistake

“VT always faster.”

## Follow-up

How would you prove this with a lab?
