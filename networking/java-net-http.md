# java.net.http (API map)

Companion to [http-client.md](./http-client.md).

| Type | Role |
|------|------|
| `HttpClient` | Shared client, connect timeout, version, executor, SSL |
| `HttpRequest` | Method, URI, headers, body publisher, request timeout |
| `HttpResponse` | Status, headers, body |
| `BodyHandlers` / `BodyPublishers` | String, bytes, file, streaming |

Prefer this over legacy `HttpURLConnection` for new code.

### Related

[http-client.md](./http-client.md) · [https.md](./https.md)
