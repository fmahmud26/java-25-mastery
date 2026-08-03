# Intrinsic Locks

Another name for the monitor lock used by `synchronized` — every object has one.

## Mental Model

```text
intrinsic lock ≡ synchronized lock ≡ monitor lock
```

## Internal Mechanics

Same as [synchronized.md](./synchronized.md) / [monitor.md](./monitor.md). Class locks for `static synchronized`.

## Code

```java
synchronized (Account.class) { /* static mutual exclusion */ }
```

## Production / Failure / Debug / Perf / Trade-offs

See synchronized & contention. Avoid locking on interned Strings / boxed Integers / public types.

## Interview Questions

- What is an intrinsic lock?  
- Danger of synchronizing on Boolean/Integer?

## Principal-Level Discussion

Lock identity hygiene: private final `Object lock = new Object()`.

### Related

[synchronized.md](./synchronized.md) · [monitor.md](./monitor.md)
