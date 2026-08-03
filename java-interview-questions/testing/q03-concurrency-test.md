# Testing a concurrency bug

## Question

How do you write a test that fails reliably for a CHM check-then-act race?

## Difficulty

Senior

## Expected answer

High parallel stress with many threads/VT; assert invariants (single create side effect); optionally use concurrency test tools; don’t sleep-based hope. Count `create()` calls with AtomicInteger.

## Reasoning

Races need contention; single-threaded tests won’t catch them.

## Follow-up

Why might the test still be flaky and how do you harden it?

## Common mistake

One thread “concurrent” test.

## Principal-level discussion

Critical invariants get stress tests in CI with bounded time; quarantine flaky without deleting coverage of the bug class.
