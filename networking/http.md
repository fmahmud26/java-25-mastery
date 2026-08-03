# HTTP

## Mental Model

```text
Request line + headers + optional body
Response status + headers + body
Application protocol usually over TCP (+ TLS for HTTPS)
```

| Version | Notes for Java backends |
|---------|-------------------------|
| HTTP/1.1 | One request at a time per connection (pipelining rare); keep-alive pooling critical |
| HTTP/2 | Multiplexed streams on one connection; `java.net.http` supports |
| HTTP/3 | QUIC/UDP — not the default classic HttpClient story |

## Mechanism

```http
POST /payments/charge HTTP/1.1
Host: payment.internal
Content-Type: application/json
Idempotency-Key: ...

{"orderId":"...","amount":1000}
```

Status classes: 2xx success, 4xx client, 5xx server. `429` / `503` often retryable with care.

## Service-to-Service

Order service → Inventory `GET /stock/{sku}` → Payment `POST /charge`. Each hop needs timeouts, auth headers, and correlation IDs.

## Java

[http-client.md](./http-client.md) — standard client. Servlets/containers on the server side.

### Related

[https.md](./https.md) · [timeouts.md](./timeouts.md) · [retries.md](./retries.md)
