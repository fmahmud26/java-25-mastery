# volatile

Ensures **visibility** and prevents certain reorderings for a single field. Does **not** make compound actions atomic.

## Mental Model

```text
volatile write ──HB──► subsequent volatile read of same field
Good: flags, safe published immutable refs
Bad:  volatile int count; count++
```

## Internal Mechanics

Volatile read/write are synchronization actions. No mutual exclusion. A volatile read is guaranteed to see the latest write to that variable that happens-before it (JMM rules). Compilers insert appropriate barriers; do not hand-wave “flushes cache.”

Modern alternative surface: [VarHandle](./varhandles.md) volatile / acquire / release modes for the same ideas with finer control.

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

## Production Scenario — shutdown latch

Workers spin/poll `volatile boolean shutdown` or better: interrupt + queue poison; volatile flag alone is a simple kill switch for cooperative exit.

## Failure Scenario

`volatile int tickets; tickets--` under concurrency → lost updates. Double-checked locking without volatile on instance field.

## Debugging Strategy

Race on counters despite volatile → need `Atomic*`. Visibility bugs on non-volatile flags. `jcstress`-style litmus for publication patterns when building libraries.

## Performance

Cheaper than locks for simple flags; still barriers. Don’t spray volatile on every field.

## Trade-offs

| Tool | Use |
|------|-----|
| `volatile` | Flags, safe published refs |
| `AtomicInteger` / `LongAdder` | Counters |
| `synchronized` / locks | Multi-field invariants |
| VarHandle acquire/release | Library-level finer ordering |

## Interview Questions

- volatile vs synchronized?  
- Is volatile atomic for ++?  
- Safe publication with volatile?  
- volatile vs AtomicReference for a config blob?  

## Principal-Level Discussion

Volatile is a precision tool. Most counters → LongAdder/Atomic; multi-field → locks/CHM. Prefer library concurrency utilities over volatile folklore.

### Related

[visibility.md](./visibility.md) · [happens-before.md](./happens-before.md) · [atomic-variables.md](./atomic-variables.md) · [varhandles.md](./varhandles.md)
