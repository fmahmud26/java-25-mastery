# Maven vs Gradle

| Dimension | Maven | Gradle |
|-----------|-------|--------|
| Model | Convention + declarative POM | Task graph + Groovy/Kotlin DSL |
| Lifecycle | Fixed phases | Flexible tasks (plugins add conventions) |
| Config | XML `pom.xml` | `build.gradle(.kts)` + `settings.gradle` |
| Performance | Improving; less incremental by default | Strong incremental + configuration cache |
| Enterprise | Ubiquitous; BOM/parent POMs mature | Growing; version catalogs + platforms |

Both solve: compile, test, package, resolve dependencies, run plugins. Choice is often org standard — learn **both** for interviews.

### Related

[maven-lifecycle.md](./maven-lifecycle.md) · [gradle-tasks.md](./gradle-tasks.md)
