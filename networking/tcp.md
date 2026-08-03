# TCP

## Mental Model

```text
TCP = reliable, ordered byte stream between two sockets
Connection: SYN → SYN-ACK → ACK (handshake)
Data: segments + ACKs + retransmit
Close: FIN/ACK (or RST on abort)
```

HTTP/1.1 and HTTP/2 (typical Java `HttpClient`) ride on TCP. HTTP/3/QUIC is UDP-based — different stack.

## Mechanism (what Java sees)

| Concept | Meaning |
|---------|---------|
| Socket | Local endpoint bound to address/port |
| Connect | Client TCP handshake to server |
| Listen/accept | Server accepts new connections |
| Backlog | Pending connections queue |
| Nagle / TCP_NODELAY | Small-write latency trade-offs |

## Service-to-Service

Checkout → Payment service: each HTTP call usually reuses a pooled TCP connection (keep-alive). New connection = handshake (+ TLS) latency.

## Failures

| Symptom | TCP angle |
|---------|-----------|
| Connection refused | Nothing listening / wrong port |
| Connection timed out | Packet loss, firewall, blackhole |
| Reset | Peer closed/aborted |
| Slow | Retransmits, congestion, bufferbloat |

## Java Backend

`Socket` / `ServerSocket` for custom protocols; `HttpClient` owns TCP under HTTPS/HTTP. Tune connect timeout separately from read/request timeout.

### Related

[socket.md](./socket.md) · [latency.md](./latency.md) · [connection-failure.md](./connection-failure.md)
