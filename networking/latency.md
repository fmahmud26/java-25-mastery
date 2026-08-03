# Latency (Network)

## Mental Model

```text
Total RPC latency ≈
  DNS (cache miss) + TCP handshake + TLS handshake
  + server queue/work + response transfer
  + retries × above
```

Pooled warm connections skip most of handshake cost.

## Service-to-Service

Checkout p99 = local work + inventory hop + payment hop. One slow dependency dominates.

## Measure

Client-side histograms per dependency; trace spans; compare connect time vs TTFB vs body.

## Reduce (after measuring)

- Pool/reuse connections  
- Tighten payloads  
- Timeouts + cancel  
- Cache GETs where safe  
- Regional locality  

Not “add retries” — retries often **increase** latency under failure.

### Related

[tls.md](./tls.md) · [timeouts.md](./timeouts.md) · [connection-pooling.md](./connection-pooling.md)
