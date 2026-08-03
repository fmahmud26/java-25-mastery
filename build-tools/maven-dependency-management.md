# Maven Dependency Management

Pin versions **centrally** without adding the dependency to every module.

```xml
<!-- parent or BOM -->
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.fasterxml.jackson</groupId>
      <artifactId>jackson-bom</artifactId>
      <version>2.17.2</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <dependency>
      <groupId>com.example</groupId>
      <artifactId>orders-api</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

Child declares deps **without** `<version>` — inherits managed version.

## Spring Boot

`spring-boot-dependencies` / starter parent is a BOM — enterprise standard for aligning Spring + third parties.

## PE Rule

One platform BOM / parent for the org; services don’t invent random versions.

### Related

[dependency-conflicts.md](./dependency-conflicts.md) · [maven-multi-module.md](./maven-multi-module.md)
