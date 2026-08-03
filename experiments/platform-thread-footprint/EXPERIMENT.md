# Experiment: Platform thread footprint

## Hypothesis

Creating and starting many **platform** threads for trivial blocking work hits practical limits (memory/OS) sooner than the same count of **virtual** threads — measured by successful starts vs OOME/failure and reported peak live threads.

## Setup

JDK 25; try `N` platform threads sleeping; then `N` virtual threads. Catch failures. **Do not** set N so high you destabilize the machine — start modest, increase carefully.

## Code

```bash
./run.sh
./run.sh 5000
```

## Expected behavior

Platform threads: higher memory / possible failure at large N. Virtual threads: succeed at larger N for sleep-style work. Exact limits are OS-dependent — measure locally.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: n=2000 sleepers; platform started=2000/2000 ready=true startWallMs=96; virtual started=2000/2000 ready=true startWallMs=48
- Production implication: At n=2000 both modes succeeded, but virtual startWallMs=48 vs platform 96 shows cheaper spawning for sleep-style work—still avoid unbounded platform threads per request and bound DB pools.

```text
Attempting n=2000 sleepers (careful; teaching demo)
platform: started=2000/2000 ready=true startWallMs=96
virtual: started=2000/2000 ready=true startWallMs=48
```

## Explanation

Platform threads ≈ OS threads (stack memory). Virtual threads are scheduled on carriers; stacks are heap-managed and unmounted when blocking (for most JDK blocking points).

## Production implication

Don’t spawn unbounded platform threads per request. Prefer VT or bounded pools. Still bound DB/connection pools.

## Interview takeaway

“Platform threads are scarce; virtual threads change concurrency style — I still bound external resources.”
