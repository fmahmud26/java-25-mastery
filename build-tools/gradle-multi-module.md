# Gradle Multi-Module

```text
orders/
  settings.gradle.kts
  build.gradle.kts          (optional root)
  orders-api/
  orders-domain/
  orders-persistence/
  orders-web/
  orders-app/
```

```kotlin
// orders-app/build.gradle.kts
dependencies {
    implementation(project(":orders-web"))
}
```

```bash
./gradlew :orders-app:bootJar
./gradlew build --parallel
```

`api` vs `implementation` controls whether transitive project deps leak — critical for encapsulation.

### Related

[maven-multi-module.md](./maven-multi-module.md) · [enterprise-projects.md](./enterprise-projects.md)
