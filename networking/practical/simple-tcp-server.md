# Practical: Simple TCP Server

Echo or line-based TCP server using `ServerSocket` + virtual threads.

## Goal

```bash
java EchoServer.java 9090
# another terminal
nc localhost 9090
```

## Requirements

- Listen on port
- `accept` loop
- Virtual thread per client
- Echo lines until client disconnects
- Clean shutdown on Ctrl+C (optional)

## Sketch

```java
void main(String[] args) throws Exception {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 9090;
    try (ServerSocket server = new ServerSocket(port)) {
        IO.println("listening on " + port);
        while (true) {
            Socket client = server.accept();
            Thread.startVirtualThread(() -> echo(client));
        }
    }
}

void echo(Socket socket) {
    try (socket;
         var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
        String line;
        while ((line = in.readLine()) != null) {
            out.write(line);
            out.newLine();
            out.flush();
        }
    } catch (IOException e) {
        IO.println("client error: " + e.getMessage());
    }
}
```

## Stretch

- Broadcast chat room  
- Simple HTTP subset parser on the socket  
- Metrics: active connections counter  

APIs: [../serversocket.md](../serversocket.md), [../socket.md](../socket.md).
