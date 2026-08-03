# Virtual Threads vs Reactive Programming

Deepen the comparison for Java 25 architecture reviews.

## Why Reactive Existed

Scarce platform threads + desire for high concurrency + backpressure for streams.

## What VT Changes

For **request/response blocking I/O**, VT often removes the need to go reactive. For **streaming pipelines** (fan-out to slow consumers, operators, fusion), reactive still shines.

## Side-by-Side

| Concern | VT | Reactive |
|---------|----|----------|
| Readability | High | Lower |
| Stack traces | Clear | Switches |
| JDBC | Native | `publishOn` / wrappers |
| Backpressure | DIY | Built-in |
| Learning curve | Low | High |
| Ecosystem fit | Servlet/Spring MVC | WebFlux |

## Production Decision

- **CRUD + DB + HTTP integrations:** VT  
- **SSE/infinite streams/complex operators:** Reactive  
- **Already invested WebFlux + skilled team:** Keep; adopt VT for sidecar blocking workers if needed  

## Failure: False Dichotomy

Teams rewrite everything to VT and lose backpressure; or keep reactive wrappers solely for thread limits VT already fixed.

### Related

[vt-vs-platform-completablefuture-reactive.md](./vt-vs-platform-completablefuture-reactive.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md)
