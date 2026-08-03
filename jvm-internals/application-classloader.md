# Application ClassLoader

Also called the **system** class loader — loads your application types from the classpath and/or module path.

## Mental Model

```text
Where com.myapp.* usually comes from.
Parent: platform → bootstrap.
```

## Technical Mechanism

```java
ClassLoader app = ClassLoader.getSystemClassLoader();
Thread.currentThread().getContextClassLoader(); // often same; frameworks may change
```

## Delegation

```text
loadClass(name):
  if already loaded → return
  try parent.loadClass(name)   // platform → bootstrap
  else findClass(name)         // classpath / module
```

## JVM Internals

Custom loaders (plugins, hot-reload, app servers) typically use the application loader as parent (or as sibling hierarchy with careful isolation).

```java
public final class PluginLoader extends URLClassLoader {
    public PluginLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
    }
}
```

## Production Implications

- Fat JARs / shaded dependencies: version conflicts surface as linkage errors.  
- `-cp` vs `-p` / modules: wrong path → CNFE at runtime.  
- TCCL: libraries loading SPI resources use TCCL — set it when submitting work to executors.

## Failure Scenario

Worker thread inherits bootstrap/platform TCCL from a misconfigured pool → `ServiceLoader` cannot see app JDBC driver.

**Fix:** set TCCL to app loader around SPI calls; restore in `finally`.

## Interview / PE

System vs platform vs bootstrap? When write a custom loader? Child-first vs parent-first trade-offs?

### Related

[class-loaders.md](./class-loaders.md) · [class-loading.md](./class-loading.md) · [incidents.md](./incidents.md)
