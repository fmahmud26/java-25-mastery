# Readers (`Reader` / `BufferedReader`)

Character input with charset decoding — line-oriented logs and CSV text.

## Mental Model

```text
bytes → CharsetDecoder → chars → readLine()
Files.newBufferedReader(path, UTF_8) preferred
```

## Java 25 Examples

```java
try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
    String line;
    while ((line = br.readLine()) != null) {
        if (line.contains("ERROR")) {
            sink.accept(line);
        }
    }
}

// Stream API — must close the stream (closes reader)
try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
    lines.filter(l -> l.contains("paymentId=")).forEach(this::index);
}
```

## Production — large log processing

Line loop or `Files.lines` + filter; never `readAllLines` on multi-GB logs. Handle malformed UTF-8 with coding error actions if needed.

## Failure Scenario

Wrong charset → mojibake or exceptions. Platform default charset in old `FileReader` — always pass `UTF_8` explicitly.

### Related

[writers.md](./writers.md) · [buffered-io.md](./buffered-io.md) · [practical/log-analyzer.md](./practical/log-analyzer.md)
