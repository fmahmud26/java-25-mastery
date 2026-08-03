# Class Loaders

Who finds class bytes and defines `Class` objects — identity of a type is **(name, defining loader)**.

## Mental Model

```text
Application  → asks parent → Platform → asks parent → Bootstrap
     ↑                                              (null)
  Custom / TCCL / containers often hang here
```

**Parent delegation:** try parent first; only load if parent cannot. Protects `java.lang.*` integrity.

## Built-in Loaders (Java 9+)

| Loader | Loads | Java view |
|--------|-------|-----------|
| **Bootstrap** | Core (`java.base`, …) | `getClassLoader() == null` |
| **Platform** | Platform modules | `ClassLoader.getPlatformClassLoader()` |
| **Application (system)** | App classpath / module path | `getSystemClassLoader()` |

Detail notes: [bootstrap-classloader.md](./bootstrap-classloader.md) · [platform-classloader.md](./platform-classloader.md) · [application-classloader.md](./application-classloader.md)

## Technical Mechanism

```java
ClassLoader app = ClassLoader.getSystemClassLoader();
ClassLoader platform = ClassLoader.getPlatformClassLoader();
Thread.currentThread().getContextClassLoader(); // TCCL — frameworks use this
```

```java
public class PluginLoader extends URLClassLoader {
    public PluginLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
    }
}
```

## JVM Internals

- Bootstrap is **native** — not a `ClassLoader` instance.  
- Defining loader owns the `Class`; metadata lives until loader is unreachable.  
- **Context class loader (TCCL)** breaks pure delegation for SPI / frameworks (JDBC drivers, etc.) — know it exists.  
- Containers (Tomcat, etc.) historically used **child-first** for webapps — different rules, same pitfalls.

## Production Implications

- App servers / plugin systems: redeploy without discarding old loader ⇒ **metaspace leak**.  
- `ClassCastException: A cannot be cast to A` ⇒ two loaders.  
- Always set TCCL correctly when crossing thread pools with SPI.

## Failure Scenario

Hourly plugin reload creates a new `URLClassLoader` each time; old loaders retained by static caches / threads ⇒ Metaspace OOM after days.

**Fix:** ensure no strong refs to old loader; clear ThreadLocals; don’t cache `Class`/`Method` from dead loaders.

## Interview / PE

Draw delegation. Why is bootstrap `null`? What is TCCL for? How can the “same” class be incompatible?

### Related

[class-loading.md](./class-loading.md) · [metaspace.md](./metaspace.md) · [incidents.md](./incidents.md)
