# File (`java.io.File`)

Legacy filesystem handle. Know it for old APIs; **new code should use `Path` + `Files`**.

## Mental Model

```text
File ≈ path string wrapper with boolean-returning ops (poor errors)
Path/Files ≈ modern API with exceptions + attributes + walk/watch
```

## Java 25 Example

```java
File legacy = new File("data", "payments.csv");
if (!legacy.exists()) {
    // boolean — no reason code
}
Path modern = legacy.toPath();
Files.createDirectories(modern.getParent());
```

## Production Notes

Adapters wrapping old libraries may still return `File` — convert with `toPath()` immediately at the boundary.

## Failure Scenario

`mkdirs()` returns `false` — disk full? permission? exists as file? You don’t know. NIO.2 throws `IOException` with cause.

### Related

[path.md](./path.md) · [files.md](./files.md)
