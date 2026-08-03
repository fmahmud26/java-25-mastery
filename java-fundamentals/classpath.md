# Classpath

Where the JVM and `javac` find application classes and JARs.

## 1. Mental Model

```text
-cp entry₁ : entry₂ : entry₃
        ↓ first match wins
   load com.acme.BillingService
```

## 2. Simple Explanation

Classpath is a search list of directories and JARs. Build tools hide it daily; you still debug it when classes go missing in prod.

## 3. Technical Explanation

| Entry | Meaning |
|-------|---------|
| Directory | Root of `com/acme/...class` tree |
| JAR | Archived classes |
| `*` | JARs in a directory (not recursive) |

Linux/macOS: `:`; Windows: `;`. Module path is separate ([modules.md](./modules.md)).

## 4. Internal Behavior

Application class loader searches classpath order. Duplicate FQCNs → **silent shadowing**. Named modules don’t use the classic flat classpath the same way.

## 5. Java 25 Example

```bash
java -cp "out:lib/billing-api.jar:lib/*" com.acme.billing.BillingApplication
```

## 6. Real-World Scenario

**Billing deploy:** CI fat JAR omitted a transitive JDBC driver present in the IDE. Prod throws `ClassNotFoundException` at pool init. Fix artifact assembly; add smoke test of the **shipped** JAR.

## 7. Common Mistake

Relying on a global `CLASSPATH` env var; assuming “it compiled” means “it’s in the runtime artifact.”

## 8. Failure Scenario

| Error | Typical meaning |
|-------|-----------------|
| `ClassNotFoundException` | Name not found on runtime path |
| `NoClassDefFoundError` | Link/init failure or missing at runtime — read **cause** |

Investigate with `jar tf`, compare build vs run deps.

## 9. Performance Implications

Huge fat JARs slow download/start slightly; wrong class version causes functional failure, not “slowness.”

## 10. Interview Questions

- What is the classpath?  
- Why does order matter?

## 11. Senior-Level Follow-ups

- CNFE vs NoClassDefFoundError?  
- How do you debug classpath hell in a fat JAR?

## 12. Principal Engineer Perspective

Classpath is **deployed configuration**. Prefer reproducible build output over tribal `-cp` scripts; modularize/`jlink` when boundaries pay off.

### Related

[modules.md](./modules.md) · [java-compilation.md](./java-compilation.md) · [packages.md](./packages.md)
