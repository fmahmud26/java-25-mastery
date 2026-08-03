# Maven Dependencies

Declare libraries resolved from repositories (Maven Central, Nexus/Artifactory).

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

Versions often omitted when managed by parent/BOM ([maven-dependency-management.md](./maven-dependency-management.md)).

```bash
mvn dependency:tree
mvn dependency:tree -Dincludes=*jackson*
```

## Transitive

Each dependency brings its own deps — see [transitive-dependencies.md](./transitive-dependencies.md). Conflicts: [dependency-conflicts.md](./dependency-conflicts.md).

### Related

[maven-dependency-scopes.md](./maven-dependency-scopes.md) · [enterprise-projects.md](./enterprise-projects.md)
