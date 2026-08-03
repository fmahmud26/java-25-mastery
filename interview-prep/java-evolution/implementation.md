# Java Evolution — Implementation

Idiomatic Java 25 snippets interviewers expect you to write fluently.

```java
// Records + sealed + pattern switch
public sealed interface Result permits Ok, Err {}
public record Ok(String value) implements Result {}
public record Err(String message) implements Result {}

String describe(Result r) {
    return switch (r) {
        case Ok(var v) -> "ok:" + v;
        case Err(var m) -> "err:" + m;
    };
}

// Virtual threads for blocking fan-out
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    var futures = ids.stream()
            .map(id -> exec.submit(() -> load(id)))
            .toList();
    for (var f : futures) f.get();
}

// Sequenced collections
var map = new LinkedHashMap<String, Integer>();
map.putFirst("a", 1);
```

## Selection cheat sheet

| Need | Feature |
|------|---------|
| Data carrier | `record` |
| Closed variants | `sealed` + switch |
| Concise locals | `var` (locals only) |
| Multiline SQL/JSON | text blocks |
| Massive blocking concurrency | virtual threads |
| Async composition | `CompletableFuture` (still valid) |

Related: [records.md](../../modern-java/records.md), [pattern-matching-for-switch.md](../../modern-java/pattern-matching-for-switch.md), [finalized-features.md](../../16-java-25-features/finalized-features.md).
