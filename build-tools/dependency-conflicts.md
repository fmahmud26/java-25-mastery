# Dependency Conflicts

## Problem

Two versions of the same library resolve onto the classpath — or one wins and breaks binary compatibility.

```text
app → lib-a → guava:31
app → lib-b → guava:32
→ which Guava runs?
```

## Maven mediation

Nearest-wins / first-declaration nuances — **always read `dependency:tree`**, don’t memorize folklore. Prefer explicit `dependencyManagement`.

## Gradle

Conflict resolution + alignment; `dependencyInsight` shows why a version was selected. Use platforms and strict versions for critical libs.

## Enterprise symptoms

`NoSuchMethodError`, `ClassNotFoundException`, “works in IDE differently than CI” (IDE may use different resolution).

## Fixes

1. Align on BOM  
2. Upgrade both parents to compatible set  
3. Exclude transitive only if safe  
4. Shade as last resort (fat jar hell)  

### Related

[debugging.md](./debugging.md) · [transitive-dependencies.md](./transitive-dependencies.md)
