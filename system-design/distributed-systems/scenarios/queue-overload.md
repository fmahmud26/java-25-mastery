# Scenario: Queue Overload Without Backpressure

## Production story

Log pipeline Kafka disk fills; producers block or drop unpredictably; online services that embed log producers stall request threads. Alternatively: unbounded in-memory queue in app → OOM kill loop.

## What’s failing

No admission control; logging on critical path; lack of shed policy.

## Bad responses

- Increase disk forever  
- Block request thread on log send  
- Silent drop with no metric  

## Principal response

1. Separate critical request path from telemetry (async, drop-under-pressure with **counted** drops).  
2. Bound buffers; clear reject/drop policy.  
3. Kafka quotas per producer; alert disk % and under-replicated partitions.  
4. Priority: transactional topics ≠ debug logs.  
5. Load-shed marketing/analytics ingest first.

## Trade-offs

Lose some debug logs during incident vs lose checkout. Always protect core journey.

## Interview probes

- What is your drop metric and alert?  
- Where does backpressure surface to the user vs internally?  

Related: [../backpressure.md](../backpressure.md), [../fault-tolerance.md](../fault-tolerance.md), [../message-queues.md](../message-queues.md).
