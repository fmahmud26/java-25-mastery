# Consistency

What readers observe after writes. **Pick per API**, not one global religion.

## Models (practical)

| Model | Guarantee | Use when |
|-------|-----------|----------|
| Strong / linearizable | Single order; read latest committed | Inventory decrement, unique short-code assign |
| Sequential | Per-process order preserved | Rarely stated alone |
| Read-your-writes | Actor sees own writes | Profile update then view |
| Monotonic reads | Never go back in time | Feed scroll |
| Eventual | Converge if writes stop | Click counts, digests |

## CAP (interview-accurate)

On partition, you choose **cancel availability of conflicting ops** or **serve possibly stale/divergent answers**. Most systems are CP or AP **for specific operations**. See [cap-theorem.md](./cap-theorem.md).

## Cross-region

Multi-region active-active implies conflict policy (LWW, CRDT, merge). Money systems often **single-writer region** + failover.

## How to say it

“Redirect can be slightly stale if we cache; code creation must be strongly unique. Click analytics are eventually consistent via Kafka.”

Related: [replication.md](./replication.md), [idempotency](../distributed-systems/idempotency.md), [cap-theorem.md](./cap-theorem.md).

**Deep dive:** [../distributed-systems/consistency.md](../distributed-systems/consistency.md).
