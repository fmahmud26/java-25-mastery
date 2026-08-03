# volatile

Ensures **visibility** and prevents certain reorderings for a single field. Does **not** make compound actions atomic.

## Mental Model

```text
volatile write ──HB──► subsequent volatile read of same field
Good: flags, safe published immutable refs
Bad:  volatile int count; count++
```

## Internal Mechanics

Volatile read/write are synchronization actions. No mutual exclusion. Reads always see latest write to that variable (per JMM rules).

## Code

```java
private volatile Config config;

void reload(Config next) {
    config = next; // publish immutable config
}

Config current() {
    return config; // sees fully constructed next if properly built before assign
}

private volatile boolean shutdown;
```

## Production Scenario — caches

Swap immutable cache snapshot via `volatile` reference or `AtomicReference`.

## Failure Scenario

`volatile int tickets; tickets--` under concurrency → lost updates. Double-checked locking without volatile on instance field.

## Debugging Strategy

Race on counters despite volatile → need Atomic*. Visibility bugs on non-volatile flags.

## Performance

Cheaper than locks for simple flags; still barriers. Don’t spray volatile.

## Trade-offs

Volatile vs AtomicInteger vs synchronized.

## Interview Questions

- volatile vs synchronized?  
- Is volatile atomic for ++?  
- Safe publication with volatile?

## Principal-Level Discussion

Volatile is a precision tool. Most counters → LongAdder/Atomic; multi-field → locks/CHM.

### Related

[visibility.md](./visibility.md) · [happens-before.md](./happens-before.md) · [atomic-variables.md](./atomic-variables.md)
