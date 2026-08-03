# Build Performance

## Maven

| Lever | Note |
|-------|------|
| Parallel | `mvn -T 1C package` |
| Avoid clean always | `clean` kills incrementality |
| Surefire fork reuse | Tune forks carefully |
| Local repo SSD | Network to Nexus dominates |
| Skip unused | `-DskipTests` only when appropriate |

## Gradle

| Lever | Note |
|-------|------|
| Configuration cache | Major wins when compatible |
| Build cache | Local + remote CI cache |
| Parallel / workers | `--parallel` |
| Incremental tasks | Correct inputs/outputs |
| Avoid `clean` | Same as Maven |

```bash
./gradlew build --build-cache --parallel
./gradlew --configuration-cache
```

## Enterprise CI

Remote build cache shared across agents; module-affected builds (`-pl -am` / Gradle selection); keep IT images cached (Testcontainers).

### Related

[debugging.md](./debugging.md) · [gradle-tasks.md](./gradle-tasks.md)
