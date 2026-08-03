# TCP/IP Basics

Layered mental model for interviews and outages.

```text
App (HTTP, gRPC, custom)
  ↓
TCP (stream)  /  UDP (datagram)  /  QUIC (HTTP/3)
  ↓
IP (routing)
  ↓
Link
```

| Term | Meaning |
|------|---------|
| IP | Host address |
| Port | Process endpoint |
| Latency | Time delay |
| Bandwidth | Throughput capacity |
| RTT | Round-trip time — dominates small RPCs |

Deep TCP: [tcp.md](./tcp.md). Related: [dns.md](./dns.md), [http.md](./http.md).
