# Gradle Build Script

`build.gradle.kts` (preferred) or Groovy DSL defines plugins, dependencies, tasks.

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.4.0"
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

Settings file defines included builds/modules: [gradle-settings.md](./gradle-settings.md).

### Related

[gradle-dependencies.md](./gradle-dependencies.md) · [gradle-tasks.md](./gradle-tasks.md)
