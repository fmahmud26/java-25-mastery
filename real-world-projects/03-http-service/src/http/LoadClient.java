package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concurrent client with timeouts and simple retry/backoff.
 */
public final class LoadClient implements AutoCloseable {
    private final HttpClient client;
    private final ExecutorService executor;
    private final int maxAttempts;
    private final Duration requestTimeout;

    public LoadClient(int maxAttempts, Duration connectTimeout, Duration requestTimeout) {
        this.maxAttempts = maxAttempts;
        this.requestTimeout = requestTimeout;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        this.client = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(connectTimeout)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public record Result(int success, int failed, long elapsedMs) {}

    public Result storm(URI base, int clients, int requestsPerClient) throws InterruptedException {
        AtomicInteger ok = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        long start = System.nanoTime();

        List<CompletableFuture<Void>> all = new ArrayList<>(clients);
        for (int c = 0; c < clients; c++) {
            final int clientId = c;
            all.add(CompletableFuture.runAsync(() -> {
                for (int r = 0; r < requestsPerClient; r++) {
                    URI target = base.resolve("/work?ms=2&c=" + clientId + "&r=" + r);
                    try {
                        String body = getWithRetry(target);
                        if (body != null && body.startsWith("worked")) {
                            ok.incrementAndGet();
                        } else {
                            fail.incrementAndGet();
                        }
                    } catch (Exception e) {
                        fail.incrementAndGet();
                    }
                }
            }, executor));
        }
        CompletableFuture.allOf(all.toArray(CompletableFuture[]::new)).join();
        long ms = (System.nanoTime() - start) / 1_000_000L;
        return new Result(ok.get(), fail.get(), ms);
    }

    public String getWithRetry(URI uri) throws IOException, InterruptedException {
        IOException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                HttpRequest req = HttpRequest.newBuilder(uri)
                        .timeout(requestTimeout)
                        .GET()
                        .build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() >= 500) {
                    throw new IOException("server " + resp.statusCode());
                }
                if (resp.statusCode() >= 400) {
                    throw new IOException("client error " + resp.statusCode());
                }
                return resp.body();
            } catch (IOException e) {
                last = e;
                ServiceLog.warn("attempt " + attempt + "/" + maxAttempts + " failed for " + uri
                        + " — " + e.getMessage());
                if (attempt < maxAttempts) {
                    Thread.sleep(25L * attempt); // linear backoff
                }
            }
        }
        throw last != null ? last : new IOException("retries exhausted");
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
