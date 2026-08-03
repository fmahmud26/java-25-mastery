# JVM Scheduling of Virtual Threads

The JVM (not the OS) decides which virtual thread runs on which carrier.

## Mental Model

```text
Runnable VTs → VT scheduler → mount on idle carrier
Block → unmount → carrier picks next runnable VT
```

Similar spirit to goroutine M:N scheduling; details are HotSpot-specific.

## Internal Mechanics

- Scheduler queue of runnable VTs.  
- Carriers from a worker pool (FJP-like).  
- Unmount on park for supported blocking.  
- Time-sharing: long CPU bursts on a VT occupy a carrier (fairness via scheduling, not magic).  
- `ForkJoinPool.commonPool()` is **not** “your VT pool” for application tasks — use `newVirtualThreadPerTaskExecutor()`.

## Code

```java
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    // each task → its own VT; JVM schedules onto carriers
    IntStream.range(0, 10_000).forEach(i ->
            exec.execute(() -> callDownstream(i)));
}
```

## Production Scenario — high-concurrency REST

Framework dispatches each request on a VT; JVM schedules tens of thousands of blocked requests with hundreds of carriers (or fewer).

## Failure Scenario

Assuming commonPool parallelism caps your VT concurrency the way it caps parallel streams — wrong mental model for VT-per-task executors.

## Interview / PE

Who schedules VTs? What happens on blocking? How does this differ from OS thread scheduling?

### Related

[carrier-threads.md](./carrier-threads.md) · [blocking-io.md](./blocking-io.md) · [thread-scalability.md](./thread-scalability.md)
