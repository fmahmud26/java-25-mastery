# Replication

Copy data for **durability, HA, read scale**. Always specify **sync vs async**, **failover**, and **RPO**.

## Models

| Model | Write path | Failure / trade-off |
|-------|------------|---------------------|
| Single leader + async followers | Fast ACK | RPO > 0 on primary death before ship |
| Sync / quorum (`acks=all`, Raft) | Slower writes | Stronger durability; write unavailable if quorum lost |
| Multi-leader | Local low latency | Conflicts; needs merge |
| Leaderless (R + W > N) | Tunable | Sloppy quorums, hinted handoff complexity |

## Production scenario: async replica “HA”

Primary dies; promote replica missing last 3 seconds of paid orders.  
**Failure:** money exists at PSP, not in DB.  
**Defense:** sync/quorum for payment tables; or PENDING+reconcile; never pretend async replica is RPO 0.

## Failover failures

- Split-brain two primaries (STONITH / fencing required).  
- Clients cache old primary IP.  
- Replica lag not monitored → promote stale.  
- Failover storms flapping.

## Trade-offs

| Sync replicate | Async |
|----------------|-------|
| Lower RPO | Higher write latency / availability coupling |
| Safer promote | Risk silent data loss |

## Principal interview angles

- “What’s your RPO on the orders table?”  
- “Who is allowed to accept writes after partition?”  

Related: [leader-follower.md](./leader-follower.md), [cap.md](./cap.md), [scenarios/replica-lag-ryw.md](./scenarios/replica-lag-ryw.md).
