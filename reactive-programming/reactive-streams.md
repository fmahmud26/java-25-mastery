# Reactive Streams Contract

The **Reactive Streams** specification (also exposed as `java.util.concurrent.Flow` since Java 9) is a minimal SPI for async stream processing with **non-blocking backpressure**.

## Mental Model

```text
Publisher.subscribe(Subscriber)
  → Subscriber.onSubscribe(Subscription)
  → Subscriber requests n via Subscription.request(n)
  → Publisher may emit ≤ n items via onNext
  → terminal: onComplete | onError (exactly one)
```

No request → no obligation to push (except optional buffering policy). This is the opposite of “firehose into an unbounded queue.”

## Core Types (JDK Flow)

| Type | Role |
|------|------|
| `Flow.Publisher<T>` | Source of items |
| `Flow.Subscriber<T>` | Consumer: `onSubscribe` / `onNext` / `onError` / `onComplete` |
| `Flow.Subscription` | Demand + cancel: `request(long)`, `cancel()` |
| `Flow.Processor<T,R>` | Both Subscriber and Publisher (transform stage) |

```java
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

void demo() {
    try (var pub = new SubmissionPublisher<String>()) {
        pub.subscribe(new Flow.Subscriber<>() {
            private Flow.Subscription sub;
            @Override public void onSubscribe(Flow.Subscription s) {
                this.sub = s;
                s.request(1);
            }
            @Override public void onNext(String item) {
                handle(item);
                sub.request(1); // pull next only after work
            }
            @Override public void onError(Throwable t) { /* log */ }
            @Override public void onComplete() { /* done */ }
        });
        pub.submit("order-42");
    }
}
```

`SubmissionPublisher` is a JDK teaching/bridge tool — production stacks usually use Reactor/RxJava for operators.

## Rules That Interviewers Probe

1. **Serial notifications** — for a given Subscription, `onNext`/`onError`/`onComplete` must not overlap.  
2. **Demand** — total `onNext` count ≤ sum of `request` (until cancel).  
3. **Cancel** — best-effort stop; no further signals required after cancel.  
4. **Errors** — `onError` is terminal; do not call `onNext` after.  

Violations cause subtle races, lost cancel, or memory growth.

## How It Works Internally (L1→L3)

| Level | View |
|-------|------|
| L1 | Consumer pulls credit; producer respects credit |
| L2 | Operators compose Publishers; each stage tracks outstanding demand |
| L3 | Libraries use queues, drain loops, and atomic demand counters; fusion may collapse stages to cut allocations |

## Production Scenario — bridge from JDK to HTTP stream

A service exposes SSE: upstream DB cursor is slow. Subscriber requests 32 events; publisher pauses the cursor when demand is zero — disk and memory stay bounded.

## When Not to Use Flow Directly

Hand-rolling `Publisher`/`Subscriber` for business APIs is error-prone. Prefer a battle-tested library; use `Flow` when integrating components that already speak Reactive Streams.

## Trade-offs

| Strength | Interoperable SPI across libraries |
|----------|-------------------------------------|
| Cost | Operator libraries add cognitive load; stack traces jump threads |
| Alternative | VT + bounded queues for request/response; Kafka lag for distributed backpressure |

## Principal Perspective

Reactive Streams solve **coordination of asynchronous producers and consumers**. They do not make CPU faster, hide DB capacity, or replace timeouts/SLOs.

### Related

[backpressure.md](./backpressure.md) · [operators-and-pipelines.md](./operators-and-pipelines.md) · [../system-design/distributed-systems/backpressure.md](../system-design/distributed-systems/backpressure.md)
