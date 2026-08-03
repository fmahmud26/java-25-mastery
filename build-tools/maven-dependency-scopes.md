# Maven Dependency Scopes

| Scope | Compile | Runtime | Test | Transitive to dependents |
|-------|---------|---------|------|---------------------------|
| `compile` (default) | ✓ | ✓ | ✓ | ✓ |
| `provided` | ✓ | — | ✓ | — (container provides) |
| `runtime` | — | ✓ | ✓ | ✓ |
| `test` | — | — | ✓ | — |
| `import` | BOM only in `dependencyManagement` | | | |

```xml
<scope>provided</scope>  <!-- servlet API on app server -->
<scope>test</scope>      <!-- JUnit -->
```

Wrong scope → bloated WAR or `ClassNotFoundException` at runtime.

### Related

[maven-dependencies.md](./maven-dependencies.md) · [dependency-conflicts.md](./dependency-conflicts.md)
