# Java Fundamentals — Interview Drill

Use aloud. Prefer **scenario + trade-off** answers. Depth lives in linked topic files.

## Platform & lifecycle

| Prompt | Anchor |
|--------|--------|
| JDK vs JRE vs JVM on Java 25? | [jdk-jre-jvm.md](./jdk-jre-jvm.md) |
| What does `javac` emit? What does the JVM do next? | [java-compilation.md](./java-compilation.md) |
| Sketch load → verify → execute → JIT | [java-compilation.md](./java-compilation.md) |
| When use `jshell` vs a real build? | [javac-java-jshell.md](./javac-java-jshell.md) |
| CNFE vs `NoClassDefFoundError`? | [classpath.md](./classpath.md) |
| `exports` vs `opens`? | [modules.md](./modules.md) |

## Encapsulation

| Prompt | Anchor |
|--------|--------|
| Package-private vs `protected`? | [access-modifiers.md](./access-modifiers.md) · [packages.md](./packages.md) |
| Public class in non-exported package? | [modules.md](./modules.md) |

## Types & variables

| Prompt | Anchor |
|--------|--------|
| Why not `double` for money? | [primitive-types.md](./primitive-types.md) |
| Reference vs primitive; `==` vs `equals` for String? | [reference-types.md](./reference-types.md) |
| Definite assignment? Effectively final? | [variables.md](./variables.md) |
| Array vs ArrayList; ArrayStoreException? | [arrays.md](./arrays.md) |

## Behavior

| Prompt | Anchor |
|--------|--------|
| `&&` vs `&`; int overflow? | [operators.md](./operators.md) |
| Switch expressions + sealed types? | [control-flow.md](./control-flow.md) |
| Design a safe retry loop | [loops.md](./loops.md) |
| Pass-by-value; overload vs override? | [methods.md](./methods.md) |
| Varargs pitfalls / overload traps? | [varargs.md](./varargs.md) |

## static & final

| Prompt | Anchor |
|--------|--------|
| Mutable static failure modes? | [static.md](./static.md) |
| `final` reference vs immutable object? | [final.md](./final.md) |

## Principal prompts (answer with production story)

1. Standardize JDK 25 build vs prod runtime across 40 services.  
2. Diagnose “works in IDE, CNFE in container.”  
3. Customer sees another customer’s cart — where do you look first?  
4. Payment events silently dropped after a new subtype — prevention design?  
5. Partner API banned your IPs — what loop policy was wrong?

### Related

[README.md](./README.md)
