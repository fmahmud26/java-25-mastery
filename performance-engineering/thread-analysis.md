# Thread Analysis

Understand what threads are doing: running, blocking, waiting, virtual vs platform.

## Measure

```bash
jcmd <pid> Thread.print
# JFR thread / virtual thread events when relevant
```

## Analyze

- Pool saturation (all workers busy or blocked)  
- Deadlocks  
- Carrier pinning symptoms (when on VTs — verify with dumps/JFR)  
- One runaway thread vs systemic  

## Tie to metrics

High latency + low CPU → often BLOCKED/WAITING. High CPU + RUNNABLE stacks → CPU profile next.

### Related

[jstack.md](./jstack.md) · [contention.md](./contention.md) · [cpu-profiling.md](./cpu-profiling.md)
