# javac, java, jshell

Day-to-day **JDK 25** tools: compile, launch, explore.

## 1. Mental Model

```text
javac  → produce .class
java   → run on JVM (class, jar, or source)
jshell → REPL for tiny experiments
```

## 2. Simple Explanation

Use `javac` to compile, `java` to execute, `jshell` to try APIs without a project. Production still uses a proper build; these tools debug and teach.

## 3. Technical Explanation

| Tool | Role |
|------|------|
| `javac` | Frontend compiler; `--release N`, `-d`, `-cp`/`--module-path` |
| `java` | Launcher; classpath/module path; `-jar`; source-launch |
| `jshell` | Read-eval-print; snippets, `/vars`, `/exit` |

**JEP 512:** compact source + instance `main` for scripts/learning.

## 4. Internal Behavior

Source-launch compiles as needed then runs. Instance `main` is used when no suitable `static main` exists. `jshell` wraps snippets in synthetic scaffolding.

## 5. Java 25 Example

```bash
javac --release 25 -d out src/com/acme/billing/Main.java
java -cp out com.acme.billing.Main

# compact tool
# void main() { IO.println("ok"); }
java Tool.java

jshell
jshell> IO.println(Runtime.version())
jshell> /exit
```

## 6. Real-World Scenario

**On-call:** bastion has wrong `java` on `PATH` (17 vs 25). Repro fails until `JAVA_HOME` pinned. Team documents blessed `java`/`javac` for incident playbooks.

## 7. Common Mistake

Using `jshell` or `java File.java` as the only “build” for a service — no tests, no reproducible artifact.

## 8. Failure Scenario

`Could not find or load main class` — wrong FQCN, `-cp`, or package/directory mismatch. Check layout with `tree`/`jar tf`.

## 9. Performance Implications

Tool choice doesn’t optimize app throughput. Wrong JDK on the path wastes hours in incidents.

## 10. Interview Questions

- What are `javac` and `java` for?  
- When is `jshell` appropriate?

## 11. Senior-Level Follow-ups

- Why `--release` over ad-hoc `-source`/`-target` historically?  
- Instance main for fleet services vs CLIs?

## 12. Principal Engineer Perspective

Publish a **blessed toolchain** for build/run/debug. Embrace Java 25 ergonomics for learning/CLIs; keep production entrypoints explicit and CI-pinned.

### Related

[java-compilation.md](./java-compilation.md) · [methods.md](./methods.md) · [classpath.md](./classpath.md)
