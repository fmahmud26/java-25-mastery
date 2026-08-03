# Maven Profiles

Conditional configuration for env-specific builds.

```xml
<profiles>
  <profile>
    <id>integration</id>
    <properties>
      <skipITs>false</skipITs>
    </properties>
  </profile>
  <profile>
    <id>release</id>
    <build>...</build>
  </profile>
</profiles>
```

```bash
mvn verify -Pintegration
mvn -Dspring.profiles.active=local  # app profile ≠ Maven profile
```

## Enterprise uses

- Open Testcontainers ITs only on CI profile  
- Different repos / signing on `release`  
- Optional modules  

## Pitfalls

Overusing profiles for what should be runtime config; undocumented required profiles (“must use `-Pci`”).

### Related

[maven-lifecycle.md](./maven-lifecycle.md) · [enterprise-projects.md](./enterprise-projects.md)
