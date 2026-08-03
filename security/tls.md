# TLS (Application View)

## Problem

Protect data **in transit** — confidentiality, integrity, server authentication (optional client auth).

## Mental Model

```text
TCP connect → TLS handshake (certs, keys) → encrypted application bytes (HTTPS)
```

## Production Scenario

All service-to-service HTTPS; public API TLS 1.2+; optional **mTLS** between payment and ledger.

## Java

- `https://` via `HttpClient` uses JVM trust defaults  
- Custom trust/key for private PKI / mTLS → [truststore](./truststore.md) / [keystore](./keystore.md)  
- Terminate TLS at LB or in-process — know where identity is verified  

## Failures

Expired certs; wrong SAN/hostname; trusting all certs in “temporary” code; mixing HTTP internal “because VPC is safe” without threat model.

## Trade-offs

mTLS strong identity vs operational cert rotation cost; TLS terminate at mesh sidecar vs app.

### Related

[keystore.md](./keystore.md) · [truststore.md](./truststore.md) · [authentication.md](./authentication.md)
