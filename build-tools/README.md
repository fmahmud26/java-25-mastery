# Build Tools — Maven & Gradle (Enterprise Java)

Build tools compile, test, package, and publish — and they decide **which jars** reach production. Misconfigured builds cause “works on my machine” and subtle runtime conflicts.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Overview: [maven-vs-gradle](./maven-vs-gradle.md)  
2. Maven: [maven-pom](./maven-pom.md) · [maven-lifecycle](./maven-lifecycle.md) · [maven-dependencies](./maven-dependencies.md) · [maven-dependency-scopes](./maven-dependency-scopes.md) · [maven-dependency-management](./maven-dependency-management.md) · [maven-plugins](./maven-plugins.md) · [maven-profiles](./maven-profiles.md) · [maven-multi-module](./maven-multi-module.md)  
3. Gradle: [gradle-build](./gradle-build.md) · [gradle-settings](./gradle-settings.md) · [gradle-tasks](./gradle-tasks.md) · [gradle-dependencies](./gradle-dependencies.md) · [gradle-plugins](./gradle-plugins.md) · [gradle-multi-module](./gradle-multi-module.md)  
4. Shared: [transitive-dependencies](./transitive-dependencies.md) · [dependency-conflicts](./dependency-conflicts.md) · [reproducible-builds](./reproducible-builds.md) · [build-performance](./build-performance.md)  
5. Practice: [enterprise-projects](./enterprise-projects.md) · [debugging](./debugging.md) · [interview](./interview.md)

## One-line PE rule

**Pin versions via a platform/BOM, inspect the resolved tree before blaming application code, and keep multi-module boundaries aligned with deployment units.**
