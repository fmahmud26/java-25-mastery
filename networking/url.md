# URI / URL

`URI` — RFC identifier (preferred in modern APIs). `URL` — older open-connection oriented.

```java
URI uri = URI.create("https://inventory.internal/stock/SKU-1");
HttpRequest.newBuilder(uri).GET().build();
```

Encode path segments properly; don’t string-concat untrusted input into URLs.

### Related

[http-client.md](./http-client.md) · [uri.md](./uri.md)
