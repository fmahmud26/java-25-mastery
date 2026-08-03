# Module System (JPMS) — Evolution View

**Introduced:** Java 9 · **Java 25:** JDK is modular; apps may opt in; **module import** (JEP 511)

Fundamentals: [../java-fundamentals/modules.md](../java-fundamentals/modules.md).

## Problem Before

Classpath was a flat bag of JARs — split packages, weak encapsulation, reflective poke-anywhere, huge runtime images.

## The Feature

Named modules with `module-info.java`: `requires`, `exports`, `opens`, `provides`/`uses`. Strong encapsulation. `jlink` custom runtimes. Java 25: `import module`.

## How It Works

Module path resolves a graph. Unnamed module = classic classpath code. Automatic modules bridge JARs without descriptors.

## Before → After

```java
// module-info.java
module com.acme.payments {
    requires com.acme.payments.api;
    requires java.net.http;
    exports com.acme.payments.api;
    opens com.acme.payments.adapter.jpa to hibernate.core;
}
```

```java
// Java 25 — scripts/demos
import module java.base;
import module java.net.http;
```

```bash
jlink --add-modules com.acme.payments --output runtime-pay
```

## Production Usage

- Libraries/platforms seeking API boundaries  
- Slim container images via `jlink`  
- Many Spring apps still classpath — OK; still benefit from modular JDK

## Trade-offs

| Pros | Cons |
|------|------|
| Clear API surface | Migration cost / framework `opens` |
| Smaller runtimes | Split package pain |
| Better integrity | Build tooling complexity |

## When NOT to Use

- Big-bang modularization of a working monolith for fashion  
- Exporting every package “to make it compile”  
- `import module` as substitute for clear imports in large codebases

## Migration Notes

1. Stay on classpath while moving language level to 25.  
2. Modularize libraries at edges first.  
3. Document every `opens` as debt.  
4. Use `jlink` when image size/CVE surface matters.

## Interview Questions

- Classpath vs module path?  
- `exports` vs `opens`?  
- What is the unnamed module?  
- Why did Java 25 add `import module`?

### Related

[modern-apis.md](./modern-apis.md) · [java-evolution.md](./java-evolution.md) · [../java-fundamentals/classpath.md](../java-fundamentals/classpath.md)
