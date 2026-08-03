# Disaster Recovery

Plan for **loss of region, account, or primary datastore**. Quantify:

| Term | Meaning |
|------|---------|
| **RPO** | How much data can you lose? (time since last durable copy) |
| **RTO** | How long until service restored? |

## Patterns

| Pattern | RPO | RTO | Notes |
|---------|-----|-----|-------|
| Backups + restore | Hours–minutes (backup interval) | Hours | Must **test** restores |
| Async cross-region replica | Seconds–minutes | Minutes–hours promote | Lag = RPO |
| Sync multi-AZ | ≈0 inside region | Seconds | Doesn’t save region loss |
| Active-active multi-region | Policy-dependent | Low | Conflict design required |
| Warm standby | Low–med | Med | Cost for idle capacity |

## What to replicate

DB, object store (cross-region replication), queue offsets/config, secrets, infra as code. Stateless services redeploy from artifacts.

## Runbooks & drills

Untested DR is fiction. Game days: promote replica, restore backup to new cluster, expire certs, kill AZ.

## Ransomware / logical corruption

Replicas copy bad writes. Need **point-in-time recovery** / immutable backups with delayed deletion.

## Interview phrasing

“Single region multi-AZ for HA; async DR replica with RPO≈1 min; RTO≈30 min promote runbook; PITR backups 7 days; quarterly restore drill.”

Related: [availability.md](./availability.md), [replication.md](./replication.md), [reliability.md](./reliability.md).
