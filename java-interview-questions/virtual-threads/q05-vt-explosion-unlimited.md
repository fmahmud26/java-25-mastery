# Unbounded accept × virtual threads

## Question

Gateway accepts unlimited connections, each handled by a VT doing JDBC. Under attack/load, DB and app collapse. Principal fix?

## Difficulty

Principal

## Expected answer

Admission control: bounded accepts, load shedding, bulkheads, rate limits, queue lengths. VT are not a substitute for capacity management. Protect DB with pool caps and 503s.

## Reasoning

Unlimited concurrency × limited resources = failure. VT make it easier to accidentally create huge in-flight work.

## Follow-up

Where should limits live—edge, app, DB?

## Common mistake

“VT scale infinitely so we’re safe.”

## Principal-level discussion

Define max in-flight per service in SLOs; enforce at gateway + app; game-day overload tests; cost/reliability dashboards for in-flight gauges.
