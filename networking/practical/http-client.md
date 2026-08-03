# Practical: HTTP Client

Build a small CLI that fetches a URL and prints status + body (or headers only).

## Goal

```bash
java HttpGet.java https://example.com
java HttpGet.java --headers-only https://example.com
```

## Requirements

- Use `java.net.http.HttpClient` (shared client)
- Connect + request timeouts
- Print status code, selected headers, body
- Exit non-zero on status ≥ 400

## Sketch

```java
void main(String[] args) throws Exception {
    URI uri = URI.create(args[0]);
    HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    HttpRequest req = HttpRequest.newBuilder(uri)
            .timeout(Duration.ofSeconds(15))
            .GET()
            .build();

    HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
    IO.println(res.statusCode());
    IO.println(res.body());
}
```

## Stretch

- Save body to file  
- Custom `User-Agent`  
- HEAD method  

APIs: [../http-client.md](../http-client.md), [../timeouts.md](../timeouts.md).
