# Flaky tests depending on wall clock

## Question

Tests fail near midnight or on slow CI. Cause and fix?

## Difficulty

Mid

## Expected answer

`Instant.now()` / sleeps. Inject `Clock`, use Awaitility carefully, avoid timing assertions; deterministic inputs.

## Common mistake

`Thread.sleep(1000)` to “wait for async.”

## Follow-up

How do you test expiry logic?
