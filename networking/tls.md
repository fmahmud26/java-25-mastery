# TLS

## Mental Model

```text
Handshake: agree secrets + authenticate cert chain
Record layer: encrypt application data (HTTP bytes)
```

## What Adds Latency

- Full handshake (CPU + round trips)  
- Cert validation / OCSP/CRL in some setups  
- Mutual TLS extra client cert  

**Connection pooling** amortizes handshake cost across many HTTP calls.

## Java Backend

- Default `HttpsURLConnection` / `HttpClient` use JVM trust store  
- Custom `SSLContext` for private CA or mTLS keystores  
- Protocols/ciphers: prefer modern defaults; don’t weaken casually  

```java
SslContextFactory // server containers
// HttpClient.newBuilder().sslContext(customSslContext)
```

## Failures

Handshake timeout, `PKIX path building failed`, hostname mismatch, protocol version skew — all look like “HTTPS broken.”

### Related

[https.md](./https.md) · [connection-pooling.md](./connection-pooling.md) · [latency.md](./latency.md)
