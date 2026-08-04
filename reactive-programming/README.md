# Reactive Programming — Principal Guide (Java 25)

Asynchronous, non-blocking pipelines with **explicit backpressure**. On Java 25, this is an **architecture choice**, not a default: virtual threads often cover request/response blocking I/O; reactive remains strong for **streaming**, operator composition, and demand-driven flow.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
Publisher  --request(n)-->  Subscription  --onNext/onError/onComplete-->  Subscriber
                ↑ backpressure credit                    │
                └────────────────────────────────────────┘
```

Reactive Streams (JDK `java.util.concurrent.Flow`, plus ecosystem Reactor / RxJava / WebFlux) define the contract. Libraries implement operators, schedulers, and fusion on top.

## Study path

1. Contract: [reactive-streams](./reactive-streams.md) · [backpressure](./backpressure.md)  
2. Pipelines: [operators-and-pipelines](./operators-and-pipelines.md)  
3. Architecture: [vs-virtual-threads](./vs-virtual-threads.md) · VT notes [virtual-threads-vs-reactive](../virtual-threads/virtual-threads-vs-reactive-programming.md)  
4. Ops: [production-pitfalls](./production-pitfalls.md) · [interview](./interview.md)  

## One-line PE rule

**Use reactive when you need streaming backpressure and operator pipelines; use virtual threads when you need readable blocking I/O at high concurrency — do not rewrite working systems for fashion.**

## Java 25 accuracy

| Topic | Fact |
|-------|------|
| JDK surface | `java.util.concurrent.Flow` (Publisher, Subscriber, Subscription, Processor) — Reactive Streams SPI, not a full operator library |
| Ecosystem | Project Reactor, RxJava, Vert.x, Spring WebFlux — **not** part of the JDK |
| VT interaction | Blocking JDBC/HTTP still needs offload (`publishOn` / bounded elastic) in classic reactive stacks; VT can host those workers |
| Structured concurrency | Preview (JEP 505) is a separate fan-out model — do not conflate with Reactive Streams |

## Related chapters

[virtual-threads/](../virtual-threads/) · [concurrency/](../concurrency/) · [system-design/distributed-systems/backpressure.md](../system-design/distributed-systems/backpressure.md) · [io-nio/asynchronous-io.md](../io-nio/asynchronous-io.md)
