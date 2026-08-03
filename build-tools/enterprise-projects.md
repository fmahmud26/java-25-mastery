# Realistic Enterprise Projects

## 1) Multi-module Spring Boot monorepo (Maven)

```text
platform-parent (BOM import Spring + internal)
retail-parent
  ├── catalog-api
  ├── catalog-service
  ├── checkout-service
  └── e2e-tests
```

- Parent enforces Java 25, Checkstyle, enforcer “no snapshot deps in release”  
- Services inherit without declaring Spring versions  
- `-pl checkout-service -am verify` on PR  

## 2) Gradle multi-project with version catalog

Shared `libs.versions.toml`; `orders-app` depends on `orders-web` via `implementation(project(...))`; `api` only on published API module.

## 3) Shared library published to Artifactory

`orders-api` deployed on release tag; consumers import BOM; conflict when two services bring different api major versions — solve with platform alignment.

## 4) Legacy WAR + provided scope

Servlet API `provided`; fat-jar plugin must not bundle container APIs — classic classpath debugging.

### Related

[maven-multi-module.md](./maven-multi-module.md) · [dependency-conflicts.md](./dependency-conflicts.md)
