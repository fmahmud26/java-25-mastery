# Thread Dumps & Debugging

Primary production tool for hung JVMs, deadlocks, pool exhaustion, and contention.

## Mental Model

```text
dump = snapshot of every thread’s state + stack + locks
read: what are threads doing? who holds what? cycles?
```

## How to Capture

```bash
jcmd <pid> Thread.print
jstack <pid>
kill -3 <pid>          # HotSpot: dump to stdout (ops familiarity)
# containers: kubectl exec … jcmd 1 Thread.print
```

Take **several dumps ~5–10s apart** to see progress vs stuck.

## What to Look For

| Signal | Likely issue |
|--------|----------------|
| `Found one Java-level deadlock` | Deadlock cycle |
| Many `BLOCKED` on same monitor | Contention / coarse lock |
| Many `WAITING` on `BlockingQueue.take` | Hungry consumers / empty queue OK; if producers also stuck → deeper |
| Many `WAITING` on `Future.get` / socket | Downstream slow; timeout missing |
| Huge thread count | Unbounded pool / leak |
| RUNNABLE in tight loop | Livelock / spin / busy poll |

## Example Snippet (illustrative)

```text
"pay-3" #31 BLOCKED
   java.lang.Thread.State: BLOCKED (on object monitor)
   at inventory.InventoryService.reserve
   - waiting to lock <0x00000000a1b2c3d0> (a java.lang.Object)
   at order.OrderService.checkout
   - locked <0x00000000a1b2c3e0>

"pay-7" #35 BLOCKED
   - waiting to lock <0x00000000a1b2c3e0>
   - locked <0x00000000a1b2c3d0>
```

→ Classic AB-BA deadlock between order and inventory locks.

## Production Playbook

1. Confirm symptom (latency, timeouts, CPU).  
2. Dump ×3.  
3. Name-filter pools (`pay-`, `http-`).  
4. Classify: deadlock / contention / wait-on-IO / thread leak.  
5. Mitigate: rollback, shed load, fix lock order, add timeouts.  
6. Add metrics so next time dump isn’t the first signal.

## Interview Questions

- How do you take and read a dump?  
- BLOCKED vs WAITING example?  
- Why multiple dumps?

## Principal-Level Discussion

Game-day dump reading. Standard thread naming. Prefer continuous lock/pool metrics over dump-only culture.

### Related

[deadlock.md](./deadlock.md) · [contention.md](./contention.md) · [thread-lifecycle.md](./thread-lifecycle.md) · [executor-service.md](./executor-service.md)
