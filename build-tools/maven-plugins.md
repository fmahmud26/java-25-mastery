# Maven Plugins

Plugins provide **goals** bound to lifecycle phases.

| Plugin | Typical use |
|--------|-------------|
| `maven-compiler-plugin` | `--release 25`, lint |
| `maven-surefire-plugin` | Unit tests |
| `maven-failsafe-plugin` | Integration tests |
| `maven-shade-plugin` / Spring Boot | Fat jar |
| `maven-enforcer-plugin` | Ban deps, require Java version |
| `jacoco-maven-plugin` | Coverage |

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <release>25</release>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Plugin versions belong in `pluginManagement` of the parent for reproducibility.

### Related

[maven-lifecycle.md](./maven-lifecycle.md) · [reproducible-builds.md](./reproducible-builds.md)
