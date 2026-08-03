# Replication

Copies of data for **durability, availability, read scale**. Always name **sync vs async** and **failover**.

## Models

| Model | Latency | Durability on ACK | Failover |
|-------|---------|-------------------|----------|
| Single leader + async replicas | Low write | Risk RPO > 0 if primary dies before ship | Promote replica; may lose tail |
| Sync / quorum write | Higher write | Stronger durability | Safer promote |
| Multi-leader | Low local write | Conflict resolution needed | Complex |
| Leaderless (quorum R+W) | Tunable | Tunable consistency | Hinted handoff, read repair |

## Read your writes

Async replica lag → user may not see their write. Mitigations: read-from-primary after write, session consistency sticky, version checks.

## Replication lag as first-class

Monitor lag seconds/bytes. Alert; route lag-sensitive reads to primary.

## Why PE cares

Replication choice sets **RPO** and read scalability. Saying “we’ll replicate” without sync/async is incomplete.

Related: [consistency.md](./consistency.md), [disaster-recovery.md](./disaster-recovery.md), [availability.md](./availability.md).
