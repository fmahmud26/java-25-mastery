# NIO.2 (`java.nio.file`)

Java 7+ filesystem API: `Path`, `Files`, `FileSystem`, `WatchService`, `DirectoryStream`, attributes.

## Mental Model

```text
NIO.2 = modern file UX
NIO   = buffers/channels plumbing
Together: Files for convenience; channels when needed
```

## Capabilities

| Feature | API |
|---------|-----|
| Path ops | `Path` |
| CRUD / copy / move | `Files` |
| Tree walk | `Files.walk` / `walkFileTree` |
| Watch directory | `WatchService` |
| Soft/hard links, attrs | `Files.*Attributes*` |

## Production Fit

Ingestion drop-folders, log dirs, backup trees, config reloads — almost always NIO.2 first.

### Related

[path.md](./path.md) · [files.md](./files.md) · [file-watching.md](./file-watching.md) · [nio.md](./nio.md)
