# Platform ClassLoader

Loads **platform modules** that are not loaded by the bootstrap loader — the modern stand-in for the old extension loader idea.

## Mental Model

```text
Bootstrap ← parent of ← Platform ← parent of ← Application
```

## Technical Mechanism

```java
ClassLoader platform = ClassLoader.getPlatformClassLoader();
platform.getParent(); // bootstrap (null)
```

On JPMS JDKs, platform modules (`java.sql`, `java.net.http`, many `jdk.*`, etc. depending on image) resolve through this layer when not bootstrap.

## JVM Internals

Separates “JDK platform API” from application classpath pollution. Application loader delegates here before loading app types.

## Production Implications

Missing platform modules in custom runtime images (`jlink`) → `ClassNotFoundException` for APIs you assumed were always present. Build images with the modules you need.

## Interview / PE

Relationship to historical extension classpath? Parent of application loader?

### Related

[bootstrap-classloader.md](./bootstrap-classloader.md) · [application-classloader.md](./application-classloader.md) · [class-loaders.md](./class-loaders.md)
