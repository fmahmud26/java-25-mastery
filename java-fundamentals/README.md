# Java Fundamentals

Platform, tooling, types, and language basics for **JDK 25** — Principal-oriented notes (not beginner tutorials).

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

Each topic file uses a compact **12-section** template: mental model → explanations → internals → Java 25 example → production scenario → mistakes/failures → performance → interview → senior follow-ups → PE perspective.

## Study path

1. Platform: [jdk-jre-jvm](./jdk-jre-jvm.md) → [java-compilation](./java-compilation.md) (bytecode + lifecycle) → [javac-java-jshell](./javac-java-jshell.md) → [classpath](./classpath.md) → [modules](./modules.md)  
2. Encapsulation: [packages](./packages.md) → [access-modifiers](./access-modifiers.md)  
3. Data: [primitive-types](./primitive-types.md) → [reference-types](./reference-types.md) → [variables](./variables.md) → [arrays](./arrays.md)  
4. Behavior: [operators](./operators.md) → [control-flow](./control-flow.md) → [loops](./loops.md) → [methods](./methods.md) → [varargs](./varargs.md)  
5. Sharing: [static](./static.md) → [final](./final.md)  
6. Drill: [interview.md](./interview.md)

## Topics

| File | Focus |
|------|--------|
| [jdk-jre-jvm.md](./jdk-jre-jvm.md) | JDK / runtime / JVM |
| [java-compilation.md](./java-compilation.md) | Compile, bytecode, execution lifecycle |
| [javac-java-jshell.md](./javac-java-jshell.md) | javac, java, jshell |
| [classpath.md](./classpath.md) | Finding classes |
| [modules.md](./modules.md) | JPMS |
| [packages.md](./packages.md) | Namespaces |
| [access-modifiers.md](./access-modifiers.md) | Visibility |
| [primitive-types.md](./primitive-types.md) | Value types |
| [reference-types.md](./reference-types.md) | Objects, aliases, String |
| [variables.md](./variables.md) | Scope & lifetime |
| [arrays.md](./arrays.md) | Fixed sequences |
| [operators.md](./operators.md) | Expressions |
| [control-flow.md](./control-flow.md) | Branching |
| [loops.md](./loops.md) | Iteration & termination |
| [methods.md](./methods.md) | Methods & parameters |
| [varargs.md](./varargs.md) | Variable arity |
| [static.md](./static.md) | Class-level members |
| [final.md](./final.md) | Bindings & freeze |
| [interview.md](./interview.md) | Drill index |

## Production themes in this folder

Runtime packaging · classpath/module failures · money as integers · aliasing · busy loops · mutable statics · overload traps · exhaustive domain switches

### Next folders

[oop](../oop/) · [collections](../collections/) · [jvm-internals](../jvm-internals/)
