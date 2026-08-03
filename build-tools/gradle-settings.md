# Gradle Settings

`settings.gradle.kts` names the build and includes multi-projects.

```kotlin
rootProject.name = "orders"
include("orders-api", "orders-domain", "orders-web", "orders-app")
```

Version catalogs (`gradle/libs.versions.toml`) centralize coordinates — enterprise-friendly.

```toml
[versions]
jackson = "2.17.2"
[libraries]
jackson-databind = { module = "com.fasterxml.jackson.core:jackson-databind", version.ref = "jackson" }
```

### Related

[gradle-multi-module.md](./gradle-multi-module.md) · [gradle-dependencies.md](./gradle-dependencies.md)
