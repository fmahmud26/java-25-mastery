# Connection Pooling (HTTP)

## Mental Model

```text
HTTP keep-alive / HTTP/2 session reuse
  ⇒ amortize TCP + TLS handshakes
  ⇒ limited concurrent connections per host
```

Without reuse: every call pays DNS + TCP + TLS — latency and port exhaustion risk.

## Java HttpClient

`HttpClient` maintains connection pools internally. Share the client. Don’t create a new `HttpClient` per request.

Other stacks (Apache HttpClient, OkHttp, WebClient) expose explicit max-per-route / max-total — set them.

## Service-to-Service

Order → Payment at 2k RPS: pool too small → wait for free connection → p99↑. Pool too large → peer overloaded / ephemeral port pressure.

## Exhaustion

See [connection-exhaustion.md](./connection-exhaustion.md).

## PE Rules

- Cap connections per dependency  
- Monitor active/pending/wait time  
- Separate clients/pools per downstream (bulkhead)  

### Related

[http-client.md](./http-client.md) · [tls.md](./tls.md) · [latency.md](./latency.md)
