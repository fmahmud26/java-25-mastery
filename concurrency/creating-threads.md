# Creating Threads

## Mental Model

Prefer **submit work to an executor** over managing raw `Thread` lifecycles. Raw threads for demos/special cases only.

## Internal Mechanics

`start()` registers with scheduler; `run()` is just a method call on the current thread. Uncaught exceptions hit the thread’s exception handler then kill that thread only.

## Code

```java
// Avoid in services
new Thread(job).start();

// Prefer
try (var exec = Executors.newFixedThreadPool(16, Thread.ofPlatform().factory())) {
    exec.execute(job);
    Future<Result> f = exec.submit(callable);
}

// Java 25 style factories
Thread.ofPlatform().name("order-", 0).start(job);
```

## Production Scenario — orders

Order enrichment workers via named platform factory → readable dumps (`order-3`).

## Failure Scenario

Thread leak (`new Thread` per request unbounded). Daemon misuse → JVM exit before flush.

## Debugging Strategy

Dump shows anonymous `Thread-42` → missing naming convention.

## Performance

Create cost amortized by pools.

## Trade-offs

Raw Thread = control + footguns. Executor = lifecycle, queueing, metrics hooks.

## Interview Questions

- Why not call `run()`?  
- Daemon vs non-daemon?

## Principal-Level Discussion

Standardize factories (name, uncaught handler, MDC inheritance policy). Ban unstructured `new Thread` in review for app code.

### Related

[platform-threads.md](./platform-threads.md) · [executor-service.md](./executor-service.md) · [runnable.md](./runnable.md)
