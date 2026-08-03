# Optimizing the wrong layer

## Question

Team spends a quarter micro-optimizing JSON serialization (+8%) while DB cross-AZ chatty queries dominate 70% of p99. Principal intervention?

## Difficulty

Principal

## Expected answer

Re-prioritize by measured impact. Kill or defer JSON work; fix query/chatty patterns/caching. Install “profile before optimize” culture; demote vanity optimizations.

## Reasoning

Opportunity cost; leadership is sequencing bets.

## Follow-up

How do you say no without demotivating?

## Common mistake

Equating hard engineering with high impact.

## Principal-level discussion

Portfolio view of perf work; shared dashboards; celebrate wins that move SLOs/$; architecture reviews require flamegraph/trace evidence.
