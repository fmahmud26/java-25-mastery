# Maven Lifecycle

Maven executes an ordered **lifecycle** of **phases**. Plugins bind **goals** to phases.

## Default lifecycle (common)

```text
validate → compile → test → package → verify → install → deploy
```

| Phase | Meaning |
|-------|---------|
| `compile` | Main sources |
| `test` | Unit tests (surefire) |
| `package` | JAR/WAR |
| `verify` | ITs / checks (failsafe often here) |
| `install` | Local `~/.m2/repository` |
| `deploy` | Remote repo |

```bash
mvn clean package    # clean = separate lifecycle
mvn verify           # runs all prior default phases too
mvn -DskipTests package
mvn -DskipITs verify
```

Invoking a phase runs **all preceding** phases in that lifecycle.

## Clean & site

`clean` and `site` are separate lifecycles — not part of default.

## Enterprise debugging

“IT didn’t run” → wrong phase (`package` vs `verify`); failsafe bound to `integration-test`/`verify`.

### Related

[maven-plugins.md](./maven-plugins.md) · [debugging.md](./debugging.md)
