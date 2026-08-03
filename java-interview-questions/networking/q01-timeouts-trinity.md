# Timeout configuration

## Question

HTTP calls hang for minutes despite “timeouts configured.” What must be set?

## Difficulty

Mid

## Expected answer

Connect timeout, request/read timeout, and total deadline (and possibly connection pool acquire timeout). Incomplete timeout config leaves hangs.

## Common mistake

Only setting connect timeout.

## Follow-up

How do you propagate a 200ms budget across two hops?
