package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tiny HTTP API served with one virtual thread per request.
 */
public final class DemoHttpServer implements AutoCloseable {
    private final HttpServer server;
    private final ExecutorService executor;
    private final Metrics metrics;

    public DemoHttpServer(int port, Metrics metrics) throws IOException {
        this.metrics = metrics;
        this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        server.setExecutor(executor);

        server.createContext("/health", this::health);
        server.createContext("/echo", this::echo);
        server.createContext("/work", this::work);
        server.createContext("/metrics", this::metricsHandler);
    }

    public void start() {
        server.start();
        ServiceLog.info("HttpServer listening on http://127.0.0.1:" + server.getAddress().getPort());
    }

    public int port() {
        return server.getAddress().getPort();
    }

    private void health(HttpExchange ex) throws IOException {
        respond(ex, 200, "ok\n");
    }

    private void echo(HttpExchange ex) throws IOException {
        String msg = queryParam(ex, "msg").orElse("hello");
        respond(ex, 200, "echo: " + msg + "\n");
    }

    private void work(HttpExchange ex) throws IOException {
        int ms = queryParam(ex, "ms").map(Integer::parseInt).orElse(5);
        ms = Math.clamp(ms, 0, 200);
        try {
            Thread.sleep(ms); // virtual thread: cheap blocking
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            respond(ex, 500, "interrupted\n");
            return;
        }
        respond(ex, 200, "worked " + ms + "ms on " + Thread.currentThread() + "\n");
    }

    private void metricsHandler(HttpExchange ex) throws IOException {
        respond(ex, 200, metrics.snapshot());
    }

    private void respond(HttpExchange ex, int status, String body) throws IOException {
        metrics.onStart();
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        try {
            ex.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            ex.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(bytes);
            }
            if (status >= 400) {
                metrics.onError();
            } else {
                metrics.onSuccess(bytes.length);
            }
        } catch (IOException e) {
            metrics.onError();
            throw e;
        }
    }

    private static java.util.Optional<String> queryParam(HttpExchange ex, String name) {
        String raw = ex.getRequestURI().getRawQuery();
        if (raw == null || raw.isBlank()) {
            return java.util.Optional.empty();
        }
        for (String part : raw.split("&")) {
            int eq = part.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = URLDecoder.decode(part.substring(0, eq), StandardCharsets.UTF_8);
            if (key.equals(name)) {
                return java.util.Optional.of(
                        URLDecoder.decode(part.substring(eq + 1), StandardCharsets.UTF_8));
            }
        }
        return java.util.Optional.empty();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ServiceLog.info("HttpServer stopped");
    }
}
