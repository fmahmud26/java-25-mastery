# HTTPS

HTTP over TLS — confidentiality, integrity, and server authentication (optional client auth).

```text
DNS → TCP connect → TLS handshake → HTTP
```

```java
HttpRequest req = HttpRequest.newBuilder(URI.create("https://payment.internal/charge"))
        .timeout(Duration.ofSeconds(3))
        .header("Authorization", "Bearer " + token)
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
```

## Production Concerns

| Topic | Detail |
|-------|--------|
| Cert expiry | Outage classic |
| Hostname verify | Prevents naive MITM |
| Trust store | Corporate CAs / private PKI |
| mTLS | Service identity in mesh/banking |
| SNI | Virtual hosts on shared IP |

Deep TLS: [tls.md](./tls.md).

### Related

[http.md](./http.md) · [http-client.md](./http-client.md) · [connection-failure.md](./connection-failure.md)
