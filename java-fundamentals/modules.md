# Modules (JPMS)

Named modules, explicit dependencies, stronger encapsulation — including Java 25 `import module` (JEP 511).

## 1. Mental Model

```text
module app {
  requires lib;
  exports app.api;     // only this package is public API
}
```

## 2. Simple Explanation

JPMS groups code into modules that declare what they need (`requires`) and what others may use (`exports`). The JDK itself is modular; `jlink` ships only what you need.

## 3. Technical Explanation

| Directive | Role |
|-----------|------|
| `requires` / `requires transitive` | Dependency / re-export |
| `exports` | Compile-time API packages |
| `opens` | Deep reflection |
| `provides` / `uses` | ServiceLoader |
| Unnamed module | Classic classpath code |

## 4. Internal Behavior

Module path resolves readability graph. Split packages across named modules are illegal. Reflection into non-opened packages fails on modern JDKs.

## 5. Java 25 Example

```java
module com.acme.billing {
    requires com.acme.billing.api;
    exports com.acme.billing.api;
}

import module java.base; // JEP 511 — demos/scripts; prefer explicit imports in large codebases
```

## 6. Real-World Scenario

**Platform library:** only `..api` is exported; `..internal` stays hidden. Spring needs `opens` for a package — documented as deliberate reflection debt, reviewed quarterly.

## 7. Common Mistake

Big-bang modularizing a working Spring Boot app for fashion, or exporting every package “to make errors go away.”

## 8. Failure Scenario

`package P is not visible` / split-package errors at startup or `jlink`. Fix exports/requires or redesign packages.

## 9. Performance Implications

`jlink` smaller images help ops. Module checks are not your throughput bottleneck.

## 10. Interview Questions

- What is `module-info.java`?  
- `exports` vs `opens`?

## 11. Senior-Level Follow-ups

- Classpath vs module path trade-offs?  
- How to modularize a 10-year monolith incrementally?

## 12. Principal Engineer Perspective

Use modules when **boundaries and shipping** matter. Don’t pay migration cost without benefit. Treat `opens` as explicit debt.

### Related

[classpath.md](./classpath.md) · [packages.md](./packages.md) · [jdk-jre-jvm.md](./jdk-jre-jvm.md)
