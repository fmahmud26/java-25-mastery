# Practical: Deadlock

## Symptoms

- App hangs; CPU near idle  
- Throughput drops to zero for some operations  
- Health checks fail while process “alive”  

## Capture

```bash
jcmd <pid> Thread.print
jstack -l <pid>
```

HotSpot often prints:

```text
Found one Java-level deadlock:
"thread-A":
  waiting to lock monitor ...
  held by thread-B
"thread-B":
  waiting to lock monitor ...
  held by thread-A
```

## Playbook

1. Confirm deadlock section / circular `BLOCKED` waits  
2. Note **lock order** and code sites  
3. Fix: consistent global lock ordering, `tryLock`, shrink critical sections, avoid nested locks  
4. Redeploy; add tests / stress  
5. For native deadlocks (rare), need broader tooling  

## Prevention (interview)

- Lock ordering  
- Timeouts  
- Higher-level concurrency utilities  
- Prefer immutable / concurrent collections  

Related: [thread-dump.md](./thread-dump.md), [../concurrency/deadlock.md](../../concurrency/deadlock.md).
