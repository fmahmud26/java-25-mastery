# Architecture: Latency budget

## Prompt

p99 SLO 100ms. Dependencies: auth 20ms, pricing 30ms, inventory 40ms (p99). Can you keep sync fan-out? Redesign.

## Expect

Math on serial vs parallel; Option to async noncritical; Decision with budget table; Result = per-span budgets.
