# ServerSocket

```java
try (ServerSocket server = new ServerSocket(8080)) {
    server.setSoTimeout(0); // accept blocking
    while (true) {
        Socket client = server.accept();
        // hand off to executor — never process all work on accept thread
        executor.execute(() -> handle(client));
    }
}
```

Backlog matters under connection bursts. Prefer battle-tested HTTP servers (Jetty/Netty/Tomcat) for production HTTP.

### Related

[socket.md](./socket.md) · [tcp.md](./tcp.md)
