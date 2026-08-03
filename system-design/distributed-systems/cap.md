# CAP Theorem

Under a **network partition**, you cannot serve a system that is simultaneously **fully Available** for conflicting operations and **strictly Consistent**. You choose which guarantee to sacrifice **for that operation**.

## Precise reading (avoid meme CAP)

- **C:** every read sees the latest committed write (linearizability-ish in interview speech).  
- **A:** every non-failing node returns a response (not “the whole company is up”).  
- **P:** partition will happen; you must define behavior when quorum cannot form.

CAP is not “pick two forever.” Most systems are **CP for some ops, AP for others**.

## Production scenarios

| Operation | Partition behavior | Why |
|-----------|-------------------|-----|
| Assign unique short code | Refuse write if no quorum (CP-ish) | Duplicate codes worse than 503 |
| Increment like counter | Accept locally, merge later (AP-ish) | UX > exact count |
| Payment capture | Refuse or pending+reconcile | Never invent success |
| Session cache | Serve stale | Availability for reads |

## Failure focus

- Mis-labeling Dynamo-style stores as “CAP available” while your **app** requires uniqueness without fencing.  
- Multi-region active-active without conflict policy — you chose AP and didn’t admit it.  
- Quorum write with `W=1` thinking you have CP durability.

## Trade-offs

| Choice | Buy | Sell |
|--------|-----|------|
| Quorum refuse on partition | Correctness | Write availability |
| Accept & repair | Write UX | Divergence windows, merge bugs |
| Single-region primary | Simpler C | Regional latency / DR story |

## PACELC add-on

Even without partition: **Latency vs Consistency** (sync replicate vs async). Async replica = lower write latency, read-your-writes bugs.

## Principal interview angles

- “For this API, if two replicas cannot talk, what does the client see?”  
- “Where are you CP vs AP in the same product?”  

Related: [consistency.md](./consistency.md), [replication.md](./replication.md), [interview.md](./interview.md).
