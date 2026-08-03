# Scenario: Consumer Lag and Poison Messages

## Production story

Kafka consumer group lag climbs from 2s → 4 hours. One partition stuck; others healthy. Root cause: a malformed message throws forever; default seek behavior retries same offset.

## What’s failing

At-least-once processing without poison handling; head-of-line blocking per partition.

## Bad responses

- Restart consumers (lag returns)  
- Skip without DLQ/audit  
- Add more consumers (won’t help one stuck partition key)

## Principal response

1. Detect: lag by partition, error rate, repeated offset.  
2. Isolate: after N failures publish to DLQ with payload+headers; advance offset (policy).  
3. Alert humans on DLQ depth.  
4. Fix parser; replay DLQ.  
5. Separate hot tenants if lag is load not poison.  
6. Backpressure ingress if lag SLO burning.

## Trade-offs

Skip poison → brief data loss/delay for that event vs freeze entire partition (often worse).

## Interview probes

- Exactly when do you commit offsets?  
- How do you replay DLQ idempotently?  

Related: [../message-delivery.md](../message-delivery.md), [../ordering.md](../ordering.md), [../backpressure.md](../backpressure.md).
