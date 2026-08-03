# Practical: Log Analyzer

Large log processing — ERROR rates, service filters, first match latency.

## Goal

```bash
java LogAnalyzer.java --in app.log --contains ERROR --service payment
```

## Approach

```java
try (Stream<String> lines = Files.lines(path, UTF_8)) {
    Map<String, Long> byService = lines
            .filter(l -> l.contains("ERROR"))
            .filter(l -> service == null || l.contains(service))
            .collect(Collectors.groupingBy(LogAnalyzer::serviceOf, Collectors.counting()));
}
```

Optional: stop at first match with `findFirst` for on-call.

## Memory

Stream lines; avoid `readAllLines`. Cap absurdly long lines if threat model requires.

## Failures

Unclosed `Files.lines` · wrong charset · analyzing actively rotated log — prefer inode-stable copy or `logrotate` copytruncate awareness.

See [../readers.md](../readers.md) · [../large-files-and-memory.md](../large-files-and-memory.md)
