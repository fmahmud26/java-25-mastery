# Exchanger

Two threads meet and swap objects — niche rendezvous.

## Mental Model

```text
exchanger.exchange(a) ↔ other.exchange(b)
```

## Code

```java
Exchanger<Buffer> exchanger = new Exchanger<>();
// filler / drainer pipeline (classic textbook)
```

## Production

Rare vs BlockingQueue. Prefer queues for pipelines.

## Interview

What is Exchanger? When queue instead?

### Related

[blockingqueue.md](./blockingqueue.md)
