# Scenario: Dual-Write Gap (DB then Kafka)

## Production story

Order service `INSERT order` commits; then `kafka.send` fails (broker blip). Order exists; fulfillment never notified. Or reverse order: event sent, DB rolls back → ghost fulfillment.

## What’s failing

Two systems cannot be updated atomically without a protocol. Naive dual-write.

## Bad responses

- “Retry send in finally” without outbox (still crash windows)  
- Ignore rare gaps  
- 2PC between DB and Kafka as first choice  

## Principal response

**Transactional outbox:**  
1. In same DB txn: write order + outbox row.  
2. Publisher polls/CDC outbox → Kafka.  
3. Consumers idempotent on `order_id`.  
4. Metrics on outbox lag age.  

Alternative: CDC from order table directly (Debezium) if pattern fits.

## Trade-offs

Extra table/process vs silent permanent inconsistency. Outbox wins for PE.

## Interview probes

- Draw crash points with and without outbox.  
- How do you dedupe when publisher succeeds twice?  

Related: [../distributed-transactions.md](../distributed-transactions.md), [../message-delivery.md](../message-delivery.md), [../idempotency.md](../idempotency.md).
