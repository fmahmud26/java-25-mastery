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

Other stacks (Apache HttpClient, OkHttp, WebClient) expose explicit max-per-route / max-total — set them and monitor wait time.

## Service-to-Service

Order → Payment at 2k RPS: pool too small → wait for free connection → p99↑. Pool too large → peer overloaded / ephemeral port pressure.

## Production Scenario — silent wait

Latency rises; CPU low; thread dumps show wait on connection acquire. Pool max=10; traffic 200 concurrent.

**Fix:** raise with peer capacity evidence; or shed load; never “max=Integer.MAX_VALUE.”

## Exhaustion

See [connection-exhaustion.md](./connection-exhaustion.md). Also: half-closed connections, idle eviction, DNS change while pooled — reconnection behavior matters during failover.

## PE Rules

- Cap connections per dependency  
- Monitor active/pending/wait time  
- Separate clients/pools per downstream (bulkhead)  
- Size from dependency capacity × safety factor, not from “we have VT so unlimited”  

## When Not to Oversize

Peer DB/PSP quotas; local ephemeral port limits; TLS session cache thrash under too many unique connections.

### Related

[http-client.md](./http-client.md) · [tls.md](./tls.md) · [latency.md](./latency.md) · [principal-decisions.md](./principal-decisions.md)
