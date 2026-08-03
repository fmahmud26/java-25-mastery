# Class Loading

Classes become usable through a defined lifecycle — not “magic on classpath.”

## Mental Model

```text
First active use of a class
    → Loading   (find bytes, create Class)
    → Linking   (verify, prepare, resolve*)
    → Initialization (<clinit>)
    → Using
    → Unloading (when loader can die)
* resolution may be lazy / on first use
```

## Technical Mechanism

| Phase | What happens |
|-------|----------------|
| **Loading** | Locate `.class` / module bytes; define `Class` via a loader |
| **Verification** | Bytecode + structural safety |
| **Preparation** | Static fields get default values (0 / null / false) |
| **Resolution** | Symbolic refs → concrete methods/fields/classes |
| **Initialization** | Run static initializers / static field assignments under lock |

```java
Class<?> c = Class.forName("com.example.App"); // load+link+init (by default)
Class.forName("com.example.App", false, loader); // can skip init
ClassLoader cl = c.getClassLoader();
```

## JVM Internals

- **Lazy loading** by default — reduces startup until something needs the type.  
- Initialization is **synchronized** per class — deadlocks possible with circular static init (rare but classic interview trap).  
- Same binary name loaded by **two loaders** ⇒ two distinct `Class` objects (`ClassCastException` hell).  
- Modules (JPMS) add readability/export checks on top of classic loaders.

## Production Implications

- Slow startup: excessive class load / large classpath / scanning (Spring). Log: `-Xlog:class+load=info`.  
- Memory: each loaded class costs **metaspace**; leaky custom loaders ⇒ Metaspace OOM.  
- Hot reload / plugin systems must manage loader GC carefully.

## Incident — class-loading issue

Symptoms: `ClassNotFoundException`, `NoClassDefFoundError`, `LinkageError`, `ClassCastException` across “same” type, or metaspace climb after redeploys.

Diagnosis: which loader failed? parent delegation broken? duplicate JARs? See [incidents.md](./incidents.md).

## Common Mistakes

| Mistake | Result |
|---------|--------|
| Child-first loading without care | Core class spoofing / inconsistency |
| Static init does heavy I/O | Latency / deadlock risk |
| Expect unload without GC’ing loader | Metaspace leak |

## Interview / PE

Walk Loading→Init. When does `<clinit>` run? Why is `NoClassDefFoundError` different from `ClassNotFoundException`? (CNF: explicit load failed; NCDFE: class was present at compile, failed at init/link later.)

### Related

[class-loaders.md](./class-loaders.md) · [metaspace.md](./metaspace.md) · [bootstrap-classloader.md](./bootstrap-classloader.md)
