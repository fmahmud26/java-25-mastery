# Debugging Build Scenarios

## 1) `NoSuchMethodError` at runtime

**Hypothesis:** dependency conflict.  
**Debug:** `mvn dependency:tree` / `gradlew dependencyInsight`. Align BOM. Confirm IDE uses same tool.

## 2) Tests pass locally, fail in CI

**Checks:** JDK version/toolchain; `-am` modules not built; flaky order; missing Testcontainers Docker; profile not activated (`-Pci`).

## 3) “Wrong jar” / stale class

**Checks:** forgot reinstall of sibling module; used IDE out-of-date artifact; need `mvn install` / rebuild from root.

## 4) Dependency not found

**Checks:** repo URL/credentials; mirror down; version typo; packaging `pom` vs `jar`; corporate blocking Central.

## 5) Build slow

**Checks:** `clean` every time; no parallel; cold dependency download; Gradle configuration cache off; huge surefire forks.

## 6) Profile confusion

Maven `-Pintegration` vs Spring `spring.profiles.active` — different systems.

## 7) Multi-module reactor order

Compile failure in A because B not built — run from root or `-am`.

### Commands cheat sheet

```bash
mvn -X ...                         # debug
mvn dependency:tree -Dverbose
./gradlew dependencies --configuration runtimeClasspath
./gradlew build --stacktrace --info
```

### Related

[dependency-conflicts.md](./dependency-conflicts.md) · [build-performance.md](./build-performance.md)
