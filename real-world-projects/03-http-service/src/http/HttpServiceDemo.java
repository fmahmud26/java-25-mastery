package http;

import java.net.URI;
import java.time.Duration;

/**
 * Boots DemoHttpServer, runs a concurrent LoadClient storm, prints metrics, shuts down.
 */
public final class HttpServiceDemo {
    public static void main(String[] args) throws Exception {
        int port = 0; // ephemeral
        int clients = 50;
        int requests = 10;
        int retries = 3;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--port" -> port = Integer.parseInt(args[++i]);
                case "--clients" -> clients = Integer.parseInt(args[++i]);
                case "--requests" -> requests = Integer.parseInt(args[++i]);
                case "--retries" -> retries = Integer.parseInt(args[++i]);
                case "-h", "--help" -> {
                    System.out.println("""
                            Usage: HttpServiceDemo [--port N] [--clients N] [--requests N] [--retries N]
                            Starts a VT HttpServer, then loads it with HttpClient (timeouts + retries).
                            """);
                    return;
                }
                default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
            }
        }

        Metrics metrics = new Metrics();
        try (DemoHttpServer server = new DemoHttpServer(port, metrics);
             LoadClient client = new LoadClient(
                     retries,
                     Duration.ofMillis(500),
                     Duration.ofSeconds(2))) {

            server.start();
            URI base = URI.create("http://127.0.0.1:" + server.port() + "/");

            String health = client.getWithRetry(base.resolve("health"));
            ServiceLog.info("health => " + health.trim());

            System.out.printf("Storm: %d clients × %d requests (virtual threads)%n",
                    clients, requests);
            LoadClient.Result result = client.storm(base, clients, requests);

            System.out.println();
            System.out.println("=== Client results ===");
            System.out.printf("success=%d failed=%d elapsedMs=%d%n",
                    result.success(), result.failed(), result.elapsedMs());
            double total = result.success() + result.failed();
            if (result.elapsedMs() > 0 && total > 0) {
                System.out.printf("approx throughput=%.1f req/s%n",
                        (total * 1000.0) / result.elapsedMs());
            }

            System.out.println();
            System.out.println("=== Server /metrics ===");
            System.out.print(client.getWithRetry(base.resolve("metrics")));
        }
    }

    private HttpServiceDemo() {}
}
