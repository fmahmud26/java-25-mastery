# Message Queues (Distributed Context)

Async buffers for decoupling and smoothing — delivery and ordering rules still apply.

## Failure focus

- Unbounded queue = delayed outage  
- Shared queue for OTP + marketing = priority inversion  
- Ack-before-write = loss; write-before-ack = dupes  
- No DLQ = poison blocks partition  

Deep delivery: [message-delivery.md](./message-delivery.md) · ordering: [ordering.md](./ordering.md) · overload: [scenarios/queue-overload.md](./scenarios/queue-overload.md).

Related: [backpressure.md](./backpressure.md), [idempotency.md](./idempotency.md), [kafka-concepts.md](./kafka-concepts.md).
