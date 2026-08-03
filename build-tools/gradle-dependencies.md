# Gradle Dependencies

Configurations map to classpaths: `implementation`, `api`, `runtimeOnly`, `compileOnly`, `testImplementation`.

```kotlin
dependencies {
    api(project(":orders-api"))                 # exported to consumers
    implementation(project(":orders-domain"))   # not leaked
    implementation(libs.jackson.databind)
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.junit.jupiter:junit-jupiter")
}
```

## Platforms / BOM

```kotlin
dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.0"))
    implementation("org.springframework.boot:spring-boot-starter-web") // version from BOM
}
```

```bash
./gradlew dependencies
./gradlew :orders-app:dependencyInsight --dependency guava
```

### Related

[transitive-dependencies.md](./transitive-dependencies.md) · [dependency-conflicts.md](./dependency-conflicts.md)
