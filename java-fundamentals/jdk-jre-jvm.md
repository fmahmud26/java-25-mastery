# JDK vs JRE vs JVM

How a **Java 25** service is built, run, and shipped.

## 1. Mental Model

```text
JDK 25 = tools + runnable Java platform
         └── Runtime (libraries + launcher)
                └── JVM (HotSpot) executes bytecode
```

## 2. Simple Explanation

The **JVM** runs `.class` bytecode. The **runtime** is JVM + SE libraries (historically “JRE”). The **JDK** adds `javac`, `jshell`, `jlink`, etc. Install **JDK 25** to develop; ship a **runtime image**, not a 2010-era separate JRE download.

## 3. Technical Explanation

| Piece | Responsibility |
|-------|----------------|
| JVM | Load/verify bytecode, interpret + JIT, heap/GC, threads |
| Runtime | Enough platform to **run** an app |
| JDK | Runtime + **build/diagnostic** tools |

`jlink` builds a minimal custom runtime from modules.

## 4. Internal Behavior

`java` starts a JVM process. Class loaders pull bytecode from module/classpath. HotSpot mixes interpretation and JIT. Native code (FFM/JNI) breaks pure portability.

## 5. Java 25 Example

```bash
java -version          # expect 25.x
javac --release 25 App.java
# Production-oriented: jlink a module set into runtime-image/
```

## 6. Real-World Scenario

**Payments API (illustrative):** CI uses `temurin:25-jdk`. Prod image is `jlink`’d with app modules only. On-call debug uses a **debug** image variant that still has `jcmd`/JFR.

## 7. Common Mistake

Saying “install JRE on the build agent” or putting a full JDK in every prod image “just in case” with no diagnostics policy.

## 8. Failure Scenario

`UnsupportedClassVersionError` or missing `jcmd` during an OOM. **Cause:** compile/runtime skew or stripped image. **Fix:** align `--release` and base image; keep a debug image. **Prevent:** CI matrix + documented runtime standard.

## 9. Performance Implications

Smaller `jlink` images → faster pulls/start (ops), not faster business logic. Wrong JVM version can block deployment entirely.

## 10. Interview Questions

- JDK vs JRE vs JVM?  
- Why can the same `.class` run on Linux and Windows?

## 11. Senior-Level Follow-ups

- How do you standardize runtimes across 50 services?  
- Full JDK base vs `jlink` — CVE surface, size, operability?

## 12. Principal Engineer Perspective

Standardize **JDK 25 for build**, pin **runtime major** in images, prefer minimal runtimes, keep **one operable debug path**. “JRE” is a *role*, not a legacy product name.

### Related

[java-compilation.md](./java-compilation.md) · [javac-java-jshell.md](./javac-java-jshell.md) · [../jvm-internals](../jvm-internals/)
