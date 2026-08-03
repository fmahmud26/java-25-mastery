# Connection Failure

## Forms

| Failure | Typical cause |
|---------|----------------|
| `UnknownHostException` | DNS |
| Connect timeout | Network path / peer down / firewall |
| Connection refused | No listener |
| TLS handshake failure | Cert/trust/protocol |
| Connection reset | Peer crash / LB idle kill |

## Java

Surfaced as `HttpConnectTimeoutException`, `ConnectException`, `SSLHandshakeException`, etc. Wrap with dependency name in logs/metrics.

## Response

- Fail fast to caller with 503/timeout  
- Retry only if policy allows ([retries.md](./retries.md))  
- Circuit-break when error rate high  

### Related

[dns.md](./dns.md) · [tcp.md](./tcp.md) · [tls.md](./tls.md)
