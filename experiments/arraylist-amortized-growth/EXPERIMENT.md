# Experiment: ArrayList amortized growth

## Hypothesis

Appending `n` elements to an `ArrayList` with default capacity triggers geometric growth copies; a list constructed with `new ArrayList<>(n)` avoids most growth copies and finishes faster for large `n`.

## Setup

JDK 25; warmup + median of timed runs; teaching microbench only.

## Code

```bash
./run.sh
./run.sh 5_000_000
```

## Expected behavior

Pre-sized list median wall time **lower** than default growth (directional).

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: default ArrayList median=42.64ms (min=29.30 max=112.63); ArrayList(n) median=18.49ms (min=6.03 max=40.73)
- Production implication: Pre-sizing cut median growth time from 42.64ms to 18.49ms—ensureCapacity / construct with size when n is known in hot list builds.

```text
default ArrayList median=42.64ms (min=29.30 max=112.63)
ArrayList(n) median=18.49ms (min=6.03 max=40.73)
Teaching microbench only — not JMH.
```

## Explanation

`ArrayList` grows ~1.5×; each growth `System.arraycopy`s. Amortized O(1) append still has resize bursts. Pre-sizing eliminates them when `n` is known.

## Production implication

Hot loops that build large lists (buffering, batch jobs) should `ensureCapacity` / construct with size.

## Interview takeaway

“Amortized O(1) ≠ no pauses; resize copies show up in profiles.”
