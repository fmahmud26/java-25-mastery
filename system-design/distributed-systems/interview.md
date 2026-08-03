# Principal Interview Questions — Distributed Systems

Use as drills. Answers should name **failure mode + trade-off + mitigation**, not definitions only.

## CAP & consistency

1. **If two AZs cannot talk, what happens to `CreatePayment` vs `RecordLike` in your design?**  
   Model: CP-ish refuse/pending for money; AP-ish accept for likes with merge. Product UX stated.

2. **User writes then immediately reads stale data. Which consistency did you violate and how do you fix without making all reads hit the primary?**  
   Model: RYW; session sticky / version token / primary read window.

3. **Is Kafka “strongly consistent”?**  
   Model: Per-partition order; not global; consumers see at-least-once; not linearizable across topics.

## Replication & leader/follower

4. **Async replica promote after primary death — what’s your RPO and how do payments survive?**  
   Model: Possible loss; quorum/sync for money or PENDING+reconcile.

5. **How does a client fence a leader that paused and woke up after election?**  
   Model: Epoch/term on writes; storage rejects stale term.

6. **When are follower reads a footgun?**  
   Model: RYW, monotonic reads, admin “read after migrate.”

## Partitioning & ordering

7. **Pick a partition key for a multiplayer game inventory and for a bank ledger. What’s hard in each?**  
   Model: Keys drive locality; cross-key atomicity lost; hot keys.

8. **Why doesn’t “add consumers” fix hot partition lag?**  
   Model: One key → one partition → one consumer in group.

9. **Do you need global order for chat messages in a room?**  
   Model: Per-room key usually enough; global order kills scale.

## Idempotency & delivery

10. **Draw every crash point between DB write and Kafka publish. How does outbox close them?**  
    Model: Dual-write gap; same txn outbox + drain; consumer dedupe.

11. **Define exactly-once for “send email.”**  
    Model: Effectively once via `notification_id` unique at provider/store; at-least-once pipe OK.

12. **Offset commit before vs after DB write — compare loss vs dupe.**  
    Model: Classic; prefer idempotent DB + commit after, or transactional patterns.

## Retries, backoff, CB, backpressure

13. **Compute retry amplification for 2k nodes, fanout 3, retries 3 during 503.**  
    Model: Huge; budgets + jitter + CB.

14. **When is fail-open vs fail-closed correct for a rate limiter / dependency?**  
    Model: Auth fail-closed; analytics fail-open; payments never invent success.

15. **Consumers die for an hour — what bounds damage?**  
    Model: Retention, lag alerts, ingress shed, priority topics.

## Locks & transactions

16. **Why is Redis `SET NX EX` unsafe for stock decrement alone?**  
    Model: No fencing; use conditional DB update / unique constraint.

17. **Implement checkout across inventory + payment without 2PC.**  
    Model: Saga + idempotent steps + compensations; order state machine.

18. **When is 2PC still justified?**  
    Model: Rare; single enterprise DB resources; accept blocking risk.

## Failure & fault tolerance

19. **List three safe degradations for an e-commerce homepage vs one unsafe.**  
    Model: Skip reco/search/ads; never skip pay authz or invent cart price without revalidate.

20. **Which single component AZ failure takes you down today — and what’s the plan?**  
    Model: Honest dependency graph; multi-AZ state; RTO drill.

---

## Scoring rubric (Principal)

| Strong | Weak |
|--------|------|
| Per-API consistency | “We’re eventually consistent” for everything |
| Names crash windows | Handwaves exactly-once |
| Quantifies retry amplification | “We retry three times” |
| Prefers single-writer / outbox | Leads with distributed locks |
| Degrade safely | Availability over correctness for money |
| Metrics: lag, PENDING age, CB state | “We’ll monitor” |

Practice with [scenarios/](./scenarios/) after answering cold.
