# Deadlock

Cycle of threads waiting for locks the others hold — permanent stuck (unless timeout).

## Mental Model

```text
T1: holds A waits B
T2: holds B waits A
```

Coffman conditions: mutual exclusion, hold-and-wait, no preemption, circular wait.

## Internal Mechanics

Intrinsic locks / ReentrantLock / resources. Detect via thread dump cycle.

## Code

```java
// Deadlock-prone
synchronized (a) {
    synchronized (b) { … }
}
// elsewhere
synchronized (b) {
    synchronized (a) { … }
}

// Fix — ordered lock acquisition
private void lockBoth() {
    Object first = System.identityHashCode(a) < System.identityHashCode(b) ? a : b;
    Object second = first == a ? b : a;
    synchronized (first) {
        synchronized (second) { … }
    }
}
```

## Production Scenario — orders + inventory

Service locks order then inventory; another path inventory then order → deadlock under load.

## Failure Scenario

All request threads BLOCKED; CPU idle; latency infinite; watchdog pages.

## Debugging Strategy — thread dump

```text
Found one Java-level deadlock:
"pool-1-thread-1":
  waiting to lock monitor 0xB (Inventory)
  which is held by "pool-1-thread-2"
"pool-1-thread-2":
  waiting to lock monitor 0xA (Order)
  which is held by "pool-1-thread-1"
```

`jcmd <pid> Thread.print` / `jstack`. Fix: lock ordering, tryLock timeouts, avoid nested locks, reduce lock scope.

## Performance

Prevention > detection. Timeouts convert deadlock to errors.

## Trade-offs

Global order vs finer lock splitting vs lock-free/DB.

## Interview Questions

- Four conditions?  
- How to diagnose with dump?  
- Prevention strategies?

## Principal-Level Discussion

Ban nested locks across aggregates. Prefer single-lock-per-aggregate + messaging. Practice reading dumps in game days.

### Related

[thread-dumps-and-debugging.md](./thread-dumps-and-debugging.md) · [livelock.md](./livelock.md) · [synchronized.md](./synchronized.md)
