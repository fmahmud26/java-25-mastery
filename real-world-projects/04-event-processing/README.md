# 04 — Idempotent Event Pipeline

**Tier:** Async core · **Demonstrates:** concurrency, backpressure, persistence port, graceful shutdown

## Problem

Build an in-process event pipeline: producers enqueue domain events, consumers process concurrently, results persist through a repository port — with **idempotent** handling and clean shutdown (no lost-acks in-process).

## Requirements

**Functional**
- Produce events with ids  
- Consume with worker VT/pool  
- Persist processed outcome  
- Duplicate event ids → single effect  

**Non-functional**
- Bounded queue (backpressure)  
- Graceful shutdown: drain or reject new, finish in-flight  
- Observable counts: produced, processed, duplicates, failed  

## Architecture

```text
EventProducer → BlockingQueue (bounded)
                  → EventConsumer(s) → EventProcessor
                         → EventRepository (port)
                              ↑ InMemoryEventRepository
```

Clean architecture: processor depends on `EventRepository`, not the map.

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| `ArrayBlockingQueue` | Clear backpressure | Unbounded LinkedList queue |
| VT consumers | Blocking process simulation | Unlimited platform threads |
| Repository interface | Swap JDBC later (06–08) | Persist inside processor |
| Idempotency via id set/upsert | At-least-once safe | Process every delivery blindly |

## Design decisions

- **Bounded queue** — `put` blocks producer (backpressure) or `offer` + metric.  
- **Idempotency** at repository: `saveIfAbsent(eventId)`.  
- **Shutdown hook** / interrupt: stop producers, drain queue, await consumers.  
- In-memory repo is a **stand-in** for Postgres unique constraint.

## Implementation plan

1. Event + ProcessedEvent records  
2. EventRepository + in-memory impl  
3. Processor with failure injection hook  
4. Producer/consumer + pipeline wiring  
5. Shutdown sequence tests  

## Failure scenarios

| Failure | Behavior |
|---------|----------|
| Queue full | Producer blocks/slows — demonstrate backpressure |
| Processor throws | Count fail; decide retry-in-place vs DLQ list |
| Duplicate id | Second save no-ops; duplicate metric++ |
| Shutdown mid-flight | In-flight finish; no new accepts |

## Testing strategy

- Idempotency: offer same id twice → one row  
- Backpressure: slow consumer → producer blocks within timeout  
- Shutdown: no events stuck unacked in memory after await  
- Concurrency: many producers/consumers invariant on counts  

## Performance considerations

- Queue size vs latency trade-off  
- Batching inserts when moving to JDBC  
- Avoid per-event logging  

## Scaling strategy

- Replace queue with Kafka; repo with JDBC unique(event_id) — see [08](../08-notification-outbox/)  
- Partition workers by key for ordering  

## Interview discussion

“This is the in-process skeleton of at-least-once + idempotent consumers with backpressure. Production swaps the queue and repository; the contracts stay.”

**Follow-ups:** Ack before vs after persist? How does this map to Kafka offsets?

## Run

```bash
chmod +x run.sh && ./run.sh
```
