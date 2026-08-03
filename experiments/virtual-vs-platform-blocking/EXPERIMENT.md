# Experiment: Virtual vs platform threads on blocking work

## Hypothesis

For many concurrent **blocking** tasks (`sleep` as stand-in for I/O), a virtual-thread-per-task executor finishes a fixed batch faster than a **small** fixed platform pool (e.g. 16), because VT don’t need one OS thread per blocker. For **CPU-bound** tasks, VT should not beat a CPU-sized platform pool.

## Setup

JDK 25; batch of `tasks` with `delayMs` sleep; compare pool sizes. Print wall time only as local observation — not JMH.

## Code

```bash
./run.sh
./run.sh 5000 10 16
```

Args: `tasks delayMs platformPoolSize`

## Expected behavior

Blocking: VT wall time ≪ small platform pool. (Optional CPU mode in program notes.) Confirm locally.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: tasks=2000 delayMs=10 platformPool=16; platform pool wallMs=1266; virtual per-task wallMs=35
- Production implication: For this blocking sleep batch, VT finished ~36× sooner than a 16-thread platform pool, so prefer virtual threads (with bounded external pools) for high-concurrency blocking I/O.

```text
tasks=2000 delayMs=10 platformPool=16 (teaching; not JMH)
platform pool wallMs=1266
virtual per-task wallMs=35
```


## Explanation

VT unmount on blocking, freeing carriers. A 16-thread pool runs at most 16 blockers at once → queueing delay.

## Production implication

Use VT for high-concurrency blocking I/O. Size platform pools for CPU. Bound DB pools either way.

## Interview takeaway

“VT help blocking concurrency; they don’t create CPU. I demonstrate both.”
