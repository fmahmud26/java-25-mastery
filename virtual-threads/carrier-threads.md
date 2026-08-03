# Carrier Threads

**Carrier** = platform thread currently executing a virtual thread’s stack.

## Mental Model

```text
Many VTs ──mount──► Few carriers (platform threads)
           unmount when VT blocks (normally)
```

Carriers are typically drawn from a **ForkJoinPool**-style scheduler dedicated to virtual threads (not something you manage like a business executor).

## Internal Mechanics

- Only mounted VTs consume a carrier.  
- Blocked unmounted VTs consume heap metadata, not a carrier.  
- **Pinned** VTs hold the carrier even while blocked — see [thread-pinning.md](./thread-pinning.md).  
- CPU-bound VT loops hold carriers continuously (expected — they’re running).

## Code (observability mindset)

```java
// You don't assign carriers manually. You observe effects:
// - high carrier utilization + CPU → compute overload
// - many VTs waiting + low CPU → downstream/pool wait
```

## Production Scenario — slow downstream API

10K VTs blocked on HTTP: carriers mostly free (unmounted). Latency dominated by dependency. Adding carriers doesn’t help — timeouts/bulkheads do.

## Failure Scenario

Pinning or CPU work: few carriers busy, unrelated VTs starve → global latency.

## Debugging

JFR / thread dumps: carriers show as platform threads running VT frames; pinned stacks historically annotated. Correlate with CPU and pool metrics.

## Interview / PE

What is a carrier? Why can millions of VTs coexist with few carriers? When do carriers become the bottleneck?

### Related

[jvm-scheduling.md](./jvm-scheduling.md) · [thread-pinning.md](./thread-pinning.md) · [platform-threads.md](./platform-threads.md)
