# Practical: Concurrent URL Checker

Check many URLs concurrently; report status / latency / failures.

## Goal

```bash
java UrlChecker.java urls.txt
# urls.txt — one URL per line
```

Output:

```text
200  142ms  https://example.com
ERR  timeout https://slow.example
```

## Requirements

- Virtual thread per URL (`Executors.newVirtualThreadPerTaskExecutor`)
- Shared `HttpClient`
- Per-request timeout
- Summarize ok / fail counts
- Limit concurrency if needed (`Semaphore`)

## Sketch

```java
void main(String[] args) throws Exception {
    List<URI> urls = Files.readAllLines(Path.of(args[0])).stream()
            .filter(s -> !s.isBlank()).map(URI::create).toList();

    HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2)).build();

    try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = urls.stream()
                .map(u -> exec.submit(() -> check(http, u)))
                .toList();
        for (var f : futures) IO.println(f.get());
    }
}

String check(HttpClient http, URI uri) {
    long t0 = System.nanoTime();
    try {
        var req = HttpRequest.newBuilder(uri)
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5)).build();
        var res = http.send(req, HttpResponse.BodyHandlers.discarding());
        long ms = (System.nanoTime() - t0) / 1_000_000;
        return res.statusCode() + "  " + ms + "ms  " + uri;
    } catch (Exception e) {
        return "ERR  " + e.getClass().getSimpleName() + "  " + uri;
    }
}
```

Some servers reject HEAD — fall back to GET with discarding body handler.

APIs: [../http-client.md](../http-client.md), [../virtual-threads](../../virtual-threads/executors-new-virtual-thread-per-task-executor.md).
