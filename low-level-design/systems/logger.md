# Logger

**Assumption:** application logging library/facade used in-process (think principled `Logger` API + appenders), not ELK ops design.

## Requirements

- Levels: TRACE…ERROR with threshold filtering  
- Multiple appenders (console, file, remote)  
- Structured context (key-value / MDC-like)  
- Async append optional with backpressure policy  
- Thread-safe hot path  
- Configurable without code change (or via DI)  

**Non-goals:** redesigning OpenTelemetry entire observability platform (can mention correlation ids).

## Use cases

1. `log.info("msg", kv…) `  
2. Add/remove appenders at runtime (ops)  
3. Change level per logger name  
4. Flush on shutdown  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `LogEvent` | Immutable timestamp, level, logger name, message, context snapshot |
| `Logger` | Named; level gate before work |
| `Appender` | Side-effect sink |
| `Filter` | Drop/accept |

**Why immutable events:** async handoff without races on mutable builder.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `LoggerFactory` | Create/find loggers | Naming hierarchy |
| `Logger` | Level check + build event | Hot API |
| `AsyncDispatcher` | Queue to appenders | Decouple I/O |
| `Appender` impls | Write | Variation |
| `Layout` / encoder | Format | Separate from write |

## Interfaces

| Port | Why |
|------|-----|
| `Appender` | `append(LogEvent)` |
| `Layout` | Serialize |
| `Clock` | Test timestamps |
| `ErrorHandler` | Appender failure policy |

## Relationships

```text
LoggerFactory → Logger → (Filter) → Appender|AsyncDispatcher → Appenders → Layout
```

## SOLID

- **OCP:** new appender without editing Logger  
- **SRP:** format ≠ I/O ≠ level filter  
- **DIP:** app code depends on `Logger` abstraction  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Facade | Logger API | Simple for apps |
| Strategy | Layout, filters | |
| Decorator | Composite appender / metrics | |
| Producer-consumer | Async queue | |
| Singleton-ish | Factory registry | One config tree — **via DI preferred** |

## Thread safety

- `LogEvent` immutable  
- Concurrent appenders: each appender synchronized **or** single async consumer thread  
- MDC: **ThreadLocal** copy into event at log site (why: async thread loses caller TLs)  

## Error handling

| Failure | Behavior |
|---------|----------|
| Appender I/O error | ErrorHandler — discard, fallback console, or block |
| Queue full (async) | Policy: block, drop, or discard oldest — **must choose explicitly** |
| Logging in appender error path | Guard re-entrancy |

## Extensibility

| Change | Touch |
|--------|-------|
| JSON layout | New `Layout` |
| Sampling filter | New `Filter` |
| OpenTelemetry bridge | New appender |

## Testing

- ListAppender captures events in unit tests  
- Level filtering doesn’t call appender  
- Async: interruptible flush with deterministic clock  
- Context snapshot independent of later MDC clear  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Immutable events | Async safety | Mutable log builder shared |
| Explicit full-queue policy | Outages bite otherwise | Unbounded queue — memory death |
| Level gate first | Perf | Always format then drop |
| Factory + DI config | Testability | Hidden global mutable statics only |

**Staff phrasing:** “Logging is a concurrent pipeline; the interesting design is backpressure and context capture, not println wrappers.”
