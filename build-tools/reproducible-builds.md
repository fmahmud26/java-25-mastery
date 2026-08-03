# Reproducible Builds

## Goal

Same source + same tool versions → **bit-identical** or reliably equivalent artifacts across machines/CI.

## Practices

| Practice | Detail |
|----------|--------|
| Pin plugin versions | `pluginManagement` / version catalog |
| Pin dependency versions | BOM + lockfiles (Gradle) |
| Toolchain | JDK 25 via toolchain, not ambient `JAVA_HOME` drift |
| Reproducible jar options | Maven `project.build.outputTimestamp`; Gradle reproducibility flags |
| Avoid timestamps in jar | Unless intentional |
| Private mirror | Nexus/Artifactory — don’t rely on flaky Central mid-build |

```xml
<project.build.outputTimestamp>2025-01-01T00:00:00Z</project.build.outputTimestamp>
```

## PE

Release pipelines fail if resolution mutates; commit lockfiles where used; document required JDK.

### Related

[build-performance.md](./build-performance.md) · [maven-plugins.md](./maven-plugins.md)
