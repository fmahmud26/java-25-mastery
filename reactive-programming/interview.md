# Reactive Programming — Interview Drill

## Junior → Mid

**Q: What problem does Reactive Streams solve?**  
A: Async producer/consumer coordination with **demand-driven** backpressure so buffers need not grow unbounded.

**Q: Name the four Flow types.**  
A: Publisher, Subscriber, Subscription, Processor.

**Q: What does `request(n)` mean?**  
A: Subscriber grants credit for up to `n` more `onNext` signals.

**Q: Does `java.util.concurrent.Flow` give you `map`/`flatMap`?**  
A: No — SPI only. Operators come from libraries (Reactor, RxJava, …).

## Senior

**Q: VT vs reactive for a JDBC-heavy CRUD API?**  
A: Prefer VT (or platform pools). Reactive forces offloading blocking I/O and usually adds little for request/response.

**Q: When keep WebFlux?**  
A: Streaming/SSE, existing investment + skill, operator/backpressure needs inside the pipeline.

**Q: Why is unbounded `flatMap` dangerous?**  
A: Unbounded in-flight inners exhaust pools and amplify downstream load.

**Q: Event-loop blocked — how do you prove it?**  
A: Thread dumps / JFR: event-loop threads in JDBC or locks; latency correlates; other connections idle.

## Staff / Principal

**Q: Design backpressure for 50k SSE clients.**  
A: Per-connection demand window; conflate/drop for laggards; metric drops; isolate slow clients; bound heap per connection; load-shed at ingress.

**Q: Migrate a stable WebFlux service to VT?**  
A: Need ROI: complexity reduction, hiring, debuggability. Do not migrate for fashion. Hybrid: VT workers for blocking adapters is often enough.

**Q: How does reactive backpressure relate to Kafka lag?**  
A: Same *idea* (slow consumer signals), different *layer*. Pipeline demand ≠ cluster lag — you often need both.

**Q: Failure mode you refuse in design review?**  
A: Unbounded buffers, retries without budget, blocking on event-loop, missing cancel propagation.

## Follow-ups Interviewers Love

- Difference between `concatMap` and `flatMap`?  
- Hot vs cold publisher?  
- Where do you put timeouts — client, operator, both?  
- How do you preserve trace context across `publishOn`?  
- Can VT replace Reactive Streams? (No — different problem.)  

## Principal Decision Prompt

“At 100× traffic, where does excess work go — wait, drop, or reject — and which dashboard shows that within five minutes?”

### Related

[README.md](./README.md) · [vs-virtual-threads.md](./vs-virtual-threads.md) · [production-pitfalls.md](./production-pitfalls.md)
