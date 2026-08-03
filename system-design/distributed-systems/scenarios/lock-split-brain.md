# Scenario: Redis Lock Split-Brain

## Production story

Nightly payout job uses `SET nx ex 60`. Job runs long; GC pause 90s; lock expires; second node starts; both pay vendors. Finance finds duplicates next day.

## What’s failing

Lease-based lock **without fencing**. Expiry ≠ safety.

## Bad responses

- “Increase TTL to 1 hour” (still pause/network longer eventually)  
- Redlock debate without fencing tokens  
- Distributed lock for what DB uniqueness could solve  

## Principal response

1. Stop using bare Redis lock for money.  
2. Make payout idempotent: `payout_id` unique; PSP idempotency key.  
3. If exclusion needed: DB row `UPDATE payouts SET status='RUNNING' WHERE id=? AND status='READY'` returning 1 row — version/fencing built-in.  
4. Or leader election with epoch; PSP rejects stale epoch.  
5. Reconcile duplicate payouts; add alert on duplicate vendor+period.

## Trade-offs

DB serialization throughput vs incorrect dual runners. For payouts, correctness ≫ parallelism.

## Interview probes

- Define a fencing token for this job.  
- Why TTL extension (watchdog) is still tricky under GC.  

Related: [../distributed-locks.md](../distributed-locks.md), [../idempotency.md](../idempotency.md).
