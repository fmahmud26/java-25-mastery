# jstack / Thread Dumps

Snapshots of thread states — fast incident tool for contention, deadlocks, stuck pools.

## Measure

```bash
jcmd <pid> Thread.print > /tmp/threads.txt
# or
jstack <pid> > /tmp/threads.txt
```

Take **3 dumps ~5–10s apart** under the symptom to see persistence.

## Analyze

| Look for | Meaning |
|----------|---------|
| Same BLOCKED stacks | Lock contention |
| BLOCKED / WAITING on pools | Exhaustion / deadlock |
| RUNNABLE hot stacks repeating | CPU hotspot candidate |
| Deadlock section | Classic deadlock report |

## Hypothesize → Experiment

Contention on `Foo.lock` → stripe or shrink critical section → re-dump under same load → confirm BLOCKED gone; re-measure p99.

## Limits

Coarse; no timing histogram. Pair with JFR for duration/frequency of blocks.

### Related

[tools/jstack.md](./tools/jstack.md) · [thread-analysis.md](./thread-analysis.md) · [contention.md](./contention.md)
