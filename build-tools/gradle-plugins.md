# Gradle Plugins

Plugins add tasks and extensions.

```kotlin
plugins {
    java
    jacoco
    id("com.diffplug.spotless") version "6.25.0"
    id("org.springframework.boot") version "3.4.0"
}
```

Convention plugins (in `buildSrc` or included build) share enterprise standards: Java 25 toolchain, checkstyle, enforcer-like rules.

### Related

[gradle-build.md](./gradle-build.md) · [maven-plugins.md](./maven-plugins.md)
