# Modern APIs (JDK 8 → 25)

APIs that changed everyday backend coding — beyond language syntax.

## Problem Before

`Date`/`Calendar` mutability, Apache HttpClient as default, no immutable collection literals, awkward `String` ops, reflection-heavy FFI.

## The Feature Set (selected)

| API | Since | Replaces / improves |
|-----|-------|---------------------|
| `java.time` | 8 | `Date`, `Calendar` |
| `CompletableFuture` | 8 | ad-hoc callbacks (still useful with VT) |
| `HttpClient` | 11 | legacy `HttpURLConnection` / external clients for many cases |
| `String` helpers (`isBlank`, `strip`, `lines`, `repeat`) | 11+ | manual trim/loops |
| `Files.readString` / `writeString` | 11 | boilerplate streams |
| Collection factories | 9 | `Collections.unmodifiable*` ceremonies |
| Sequenced collections | 21 | ad-hoc first/last access |
| FFM (`java.lang.foreign`) | 22 final | much JNI for native calls |
| Compact source / `IO` | 25 | ceremony for scripts (JEP 512) |

## How It Works

Adopt APIs independently of JPMS. Prefer standard library when it meets SLOs; keep specialized clients (WebClient, JDBC) where needed.

## Before → After

```java
// Time
// Before: System.currentTimeMillis() / new Date()
Instant paidAt = Instant.now();
ZonedDateTime local = paidAt.atZone(ZoneId.of("Asia/Dhaka"));

// HTTP
var client = HttpClient.newHttpClient();
var req = HttpRequest.newBuilder(URI.create("https://psp/capture"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
var res = client.send(req, HttpResponse.BodyHandlers.ofString());

// Strings
if (input.isBlank()) throw new IllegalArgumentException("input");
```

## Production Usage

- `java.time` everywhere for new code; isolate legacy `Date` at boundaries  
- `HttpClient` for simple outbound calls; reactive stacks may still use WebClient  
- `Files.readString` for config/tests carefully (size limits)

## Trade-offs

| Pros | Cons |
|------|------|
| Fewer dependencies | `HttpClient` less feature-rich than some libs |
| Consistent JDK behavior | Migration from `Date` is wide |
| Sequenced APIs clarify order | Need 21+ runtime |

## When NOT to Use

- Replacing battle-tested HTTP stacks mid-incident  
- `java.time` parsing without zone discipline  
- Reading huge files with `readString`

## Migration Notes

1. Ban new `Date` in review.  
2. Introduce `HttpClient` for greenfield adapters.  
3. Align language level and runtime (25).

## Interview Questions

- Why was `java.time` introduced?  
- HttpClient vs WebClient — when each?  
- What are sequenced collections?  
- What Java 25 API ergonomics help scripts/learning?

### Related

[modern-collection-apis.md](./modern-collection-apis.md) · [module-system.md](./module-system.md) · [java-evolution.md](./java-evolution.md)
