# Leader / Follower

One **leader** accepts writes (or sequences them); **followers** replicate and serve reads (often). Election replaces a dead leader.

## Why leaders exist

- Total order of writes without full mesh consensus on every client  
- Simpler conflict model than multi-writer  
- Natural place for constraints/validation  

## Mechanisms (interview-level)

| System style | Leader role |
|--------------|-------------|
| Postgres primary/replicas | Single writer |
| Raft / etcd / Consul | Elected log leader |
| Kafka partition leader | Per-partition leader + ISR |
| MongoDB replica set | Primary |

## Production scenario: failover flap

Leader GC pause → followers think dead → election → old leader returns → **split-brain** if no fencing.  
**Defense:** epoch/term numbers, fencing tokens, STONITH, clients must reject stale leader (token on every write).

## Follower read failures

- Replica lag → stale reads (see RYW scenario).  
- Read-only mode during election → availability dip (CP-ish).  
- All reads on leader → no scale benefit; leader overload.

## Trade-offs

| Single leader | Multi-leader |
|---------------|--------------|
| Clear write authority | Conflict resolution |
| Failover complexity | Write locality |
| Throughput ceiling on leader | Higher write avail |

## Principal interview angles

- “How do clients fence a deposed leader?”  
- “What’s ordered by the leader — whole cluster or per shard?”  

Related: [replication.md](./replication.md), [distributed-locks.md](./distributed-locks.md), [cap.md](./cap.md).
