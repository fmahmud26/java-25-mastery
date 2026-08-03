# Contention

Many threads competing for the same lock, atomic, or hot key — throughput collapses, latency spikes.

## Mental Model

```text
hot lock / hot Atomic / hot CHM key → convoy, CPU in park/unpark, poor scaling
```

## Internal Mechanics

Monitor inflation; CAS failure loops; cache-line ping-pong (false sharing). Amdahl: serial section limits speedup.

## Code

```java
// Hot global lock — bad for payments
synchronized (GLOBAL) { process(payment); }

// Better — stripe by paymentId
Object lock = stripes[paymentId.hashCode() & (stripes.length - 1)];
synchronized (lock) { process(payment); }
```

## Production Scenario — high-traffic APIs / inventory

Single monitor for all stock SKUs → contention; use per-SKU locking or CHM compute or DB row locks.

## Failure Scenario

p99 latency explodes while CPU not 100% — threads BLOCKED/WAITING on one lock.

## Debugging Strategy

```text
jcmd <pid> Thread.print | grep BLOCKED
async-profiler lock profile
metrics: lock wait time, CHM hot keys
```

Thread dump cluster of threads waiting on same `<0x…>` monitor.

## Performance

Reduce critical section; stripe; shard; LongAdder; concurrent collections; eliminate lock (immutable publish).

## Trade-offs

Coarse lock simplicity vs fine-grained complexity/deadlock risk.

## Interview Questions

- How do you detect contention?  
- False sharing?  
- Striping idea?

## Principal-Level Discussion

Contention is an SLO problem. Design data partitions so hot keys don’t serialize the business. Capacity plan for the serial fraction.

### Related

[deadlock.md](./deadlock.md) · [concurrenthashmap.md](./concurrenthashmap.md) · [thread-dumps-and-debugging.md](./thread-dumps-and-debugging.md)
