# CAP Theorem

Under **network partition**, a system cannot be simultaneously **fully Available** for all conflicting requests and **strictly Consistent**. Something gives.

## Interview use (don’t recite only)

1. Name the operation.  
2. Say what happens if replica set cannot form quorum.  
3. Choose: refuse write (CP-ish) vs accept and repair (AP-ish).

| Product op | Typical choice | Why |
|------------|----------------|-----|
| Payment capture | Refuse if unsure | Double-charge worse than downtime |
| Like counter | Accept, merge later | Availability UX |
| Unique username | Refuse on uncertainty | Uniqueness invariant |

## PACELC add-on

Even without partition (**Else**): trade Latency vs Consistency (sync replicate vs async).

Related: [consistency.md](./consistency.md), [replication.md](./replication.md).

**Deep dive (canonical failure-first treatment):** [../distributed-systems/cap.md](../distributed-systems/cap.md).
