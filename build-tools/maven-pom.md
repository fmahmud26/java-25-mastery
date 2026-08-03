# Maven POM

`pom.xml` is the project object model: coordinates, packaging, deps, plugins, modules.

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>orders-service</artifactId>
  <version>1.4.2</version>
  <packaging>jar</packaging>
  <!-- parent, properties, dependencies, build, profiles, modules -->
</project>
```

| Element | Role |
|---------|------|
| GAV | `groupId:artifactId:version` identity |
| `parent` | Inherit dependencyManagement/plugins |
| `properties` | Version variables |
| `dependencyManagement` | Pin versions without forcing deps |
| `build/plugins` | Bind goals to lifecycle |

Enterprise: parent POM owned by platform team; services inherit Java version, enforcer rules, plugin versions.

### Related

[maven-lifecycle.md](./maven-lifecycle.md) · [maven-dependency-management.md](./maven-dependency-management.md)
