# Interview — Maven & Gradle

---

### Maven lifecycle?

Default phases: `validate → compile → test → package → verify → install → deploy`. Phase runs predecessors. `clean` separate. Plugins bind goals to phases.

---

### Gradle tasks vs Maven phases?

Gradle = task graph (flexible). Maven = fixed lifecycle. Both compile/test/package via conventions.

---

### Transitive dependencies?

Deps of your deps appear on classpath (per scope). Inspect with `dependency:tree` / `dependencies`.

---

### Dependency conflicts?

Multiple versions compete → `NoSuchMethodError`. Fix with BOM/platform, explicit management, insight tools. Avoid random excludes.

---

### dependencyManagement vs dependencies?

Management pins versions; dependencies actually add artifacts. BOM `import` scope.

---

### Multi-module?

Parent aggregates; build reactor order; `-pl -am` / Gradle project paths. Align modules with deployable boundaries.

---

### Profiles?

Maven conditional config (`-P`). Not Spring profiles.

---

### Reproducible builds?

Pin plugins/deps/JDK; output timestamps; private mirrors; lockfiles.

---

### Performance?

Avoid unnecessary clean; parallel; Gradle build/configuration cache; remote cache in CI.

**PE line:** platform BOM, inspect resolved tree, module boundaries = deployment boundaries.

### Related

[README.md](./README.md) · [debugging.md](./debugging.md) · [enterprise-projects.md](./enterprise-projects.md)
