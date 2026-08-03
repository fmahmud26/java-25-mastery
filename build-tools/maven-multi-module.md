# Maven Multi-Module

Reactor build: parent aggregates modules; Maven builds in dependency order.

```text
orders-parent
├── orders-api          (jar: DTOs)
├── orders-domain
├── orders-persistence
├── orders-web
└── orders-app          (spring boot, depends on web)
```

```xml
<!-- parent -->
<packaging>pom</packaging>
<modules>
  <module>orders-api</module>
  <module>orders-domain</module>
  ...
</modules>
```

```bash
mvn -pl orders-web -am package   # also make dependents
mvn -pl orders-web -amd package  # also make dependants
```

## Design

- API module shared with other services carefully (versioning)  
- Domain without Spring if possible  
- App module = composition root  

### Related

[enterprise-projects.md](./enterprise-projects.md) · [gradle-multi-module.md](./gradle-multi-module.md)
