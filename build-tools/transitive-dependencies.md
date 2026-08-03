# Transitive Dependencies

## Mental Model

```text
You depend on A
A depends on B and C
→ B and C appear on your classpath (per scope/configuration rules)
```

## Why it matters

- Security CVEs in transitives  
- Version conflicts  
- Unexpected classes at runtime  

## Inspect

```bash
mvn dependency:tree
./gradlew dependencies
./gradlew dependencyInsight --dependency log4j-core
```

## Control

- BOM / platform alignment  
- Exclusions (surgical)  
- Direct declare to force version (with care)  
- Dependency locks / resolution strategies  

### Related

[dependency-conflicts.md](./dependency-conflicts.md) · [maven-dependency-management.md](./maven-dependency-management.md)
