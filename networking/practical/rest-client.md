# Practical: REST Client

Thin typed wrapper over `HttpClient` for JSON REST calls.

## Goal

```java
RestClient api = new RestClient("https://httpbin.org");
String body = api.get("/get");
String posted = api.post("/post", "{\"name\":\"Ada\"}");
```

## Requirements

| Feature | Detail |
|---------|--------|
| Base URI | Resolve relative paths |
| GET/POST | String body for simplicity |
| Headers | `Accept` / `Content-Type: application/json` |
| Errors | Throw on non-2xx with status + body snippet |
| Timeouts | Configurable |

## Sketch

```java
public final class RestClient {
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3)).build();
    private final URI base;

    public RestClient(String base) { this.base = URI.create(base.endsWith("/") ? base : base + "/"); }

    public String get(String path) throws Exception {
        return send(HttpRequest.newBuilder(base.resolve(path.stripLeading()))
                .header("Accept", "application/json").GET().timeout(Duration.ofSeconds(10)).build());
    }

    public String post(String path, String json) throws Exception {
        return send(HttpRequest.newBuilder(base.resolve(path.stripLeading()))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build());
    }

    private String send(HttpRequest req) throws Exception {
        var res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() < 200 || res.statusCode() >= 300) {
            throw new IOException("HTTP " + res.statusCode() + ": " + res.body());
        }
        return res.body();
    }
}
```

## Stretch

- Record DTOs + simple JSON (Jackson or hand-parse)  
- Bearer auth header  
- Retry on 503  

APIs: [../http-client.md](../http-client.md), [../uri.md](../uri.md), [../retries.md](../retries.md).
