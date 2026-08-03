# Sockets (`java.net.Socket`)

## Mental Model

```text
Socket = programming API over TCP (typically)
Client: new Socket() → connect(host, port) → streams
Server: ServerSocket.accept() → per-client Socket
```

## Mechanism

```java
try (Socket s = new Socket()) {
    s.connect(new InetSocketAddress(host, port), 3_000); // connect timeout ms
    s.setSoTimeout(5_000); // read timeout ms
    var out = s.getOutputStream();
    var in = s.getInputStream();
    // custom protocol framing...
}
```

| API | Role |
|-----|------|
| `connect(..., timeout)` | TCP connect deadline |
| `setSoTimeout` | Read deadline |
| `setTcpNoDelay(true)` | Lower latency for small RPCs |
| `setKeepAlive` | OS-level keepalive (not HTTP keep-alive) |

## Service-to-Service

Most backends use HTTP clients (which use sockets internally). Raw sockets appear in legacy binary protocols, health checks, or teaching labs — see [practical/simple-tcp-server.md](./practical/simple-tcp-server.md).

## Pitfalls

- Blocking reads without timeout → stuck threads  
- One thread per socket without pools → exhaustion  
- Ignoring half-close / leftover bytes in framing  

### Related

[serversocket.md](./serversocket.md) · [tcp.md](./tcp.md) · [timeouts.md](./timeouts.md)
