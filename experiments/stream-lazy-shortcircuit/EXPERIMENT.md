# Experiment: Stream laziness and short-circuit

## Hypothesis

A pipeline with `filter`/`map`/`findFirst` evaluates **only until the first match**, so side-effect counters in intermediate ops stay ≪ `n`, whereas a naive loop marking all elements processes `n`. Lazy streams don’t run until a terminal op.

## Setup

JDK 25; count peek invocations.

## Code

```bash
./run.sh
```

## Expected behavior

`peek` count near the index of first match, not full `n`.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: found=1000 peeks=1001 n=1000000
- Production implication: Only 1001 peeks for target 1000 of 1M elements shows short-circuit terminals stop the pipeline early—use them for search, keep side effects out of map.

```text
found=1000 peeks=1001 n=1000000 (expect peeks ~= target+1, not n)
```

## Explanation

Streams are lazy; short-circuiting terminals stop the pipeline. Side effects in intermediate ops are discouraged but useful here to observe.

## Production implication

Don’t put transactional side effects in `map`. Use streams for pure transforms; short-circuit for search.

## Interview takeaway

“Streams are lazy; terminals pull. I can explain short-circuit with findFirst/anyMatch.”
