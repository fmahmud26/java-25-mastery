# Bootstrap ClassLoader

Loads the foundational JDK classes (`Object`, `String`, `Class`, … from `java.base` and related bootstrap modules).

## Mental Model

```text
No parent. Native implementation. Appears as null in Java APIs.
```

## Technical Mechanism

```java
String.class.getClassLoader(); // null → bootstrap (convention)
Object.class.getClassLoader(); // null
```

| Trait | Detail |
|-------|--------|
| Implementation | Native (HotSpot) |
| Parent | None |
| Visibility | `null` from Java |
| Source | JDK runtime image / modules (`jrt:/`) — not legacy `rt.jar` |

## JVM Internals

Bootstrap defines the types every other loader must see consistently. Parent delegation ensures user code cannot redefine `java.lang.String`.

## Production Implications

Rarely “broken” itself; failures here usually mean corrupted JDK install or illegal reflective access / agent rewriting core classes.

## Interview / PE

Why `null`? Can application code be bootstrap-loaded? (Not via normal classpath.)

### Related

[class-loaders.md](./class-loaders.md) · [platform-classloader.md](./platform-classloader.md)
