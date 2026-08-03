# Gradle Tasks (Lifecycle Analogue)

Gradle has a **task graph**, not Maven’s fixed phases. Plugins register tasks (`compileJava`, `test`, `jar`, `bootJar`).

```bash
./gradlew tasks
./gradlew build          # often: check + assemble
./gradlew test
./gradlew :orders-web:build
./gradlew clean build
```

| Concept | Meaning |
|---------|---------|
| Task inputs/outputs | Enable incremental builds |
| `build` | Convention lifecycle task |
| Task dependencies | `test` depends on `compileTestJava`, etc. |

```bash
./gradlew build --dry-run   # see task plan
```

### Related

[maven-lifecycle.md](./maven-lifecycle.md) · [build-performance.md](./build-performance.md)
