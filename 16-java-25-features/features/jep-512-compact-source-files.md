# JEP 512 — Compact Source Files and Instance Main Methods

| | |
|--|--|
| **JEP** | [512](https://openjdk.org/jeps/512) |
| **Status** | **Final** (SE language) — JDK 25 |
| **History** | Preview path 445 → 463 → 477 → 495 → **512 final** (renamed from “simple” to “compact”; minor IO changes) |

## Purpose

Let beginners and authors of small programs write Java with less ceremony — still the same language and toolchain, not a dialect.

## Problem Solved

Classic Hello World forces `class`, `public`, `static`, `String[] args`, `System.out` before variables/control flow make sense.

## Previous Approach

Full class with `public static void main(String[] args)`.

## New Approach

Compact source files with instance main methods; implicit class; convenient console IO via `java.lang.IO`.

## Syntax / API

Conceptual final shape (see JEP 512 for exact rules):

```java
void main() {
    IO.println("Hello, World!");
}
```

Finalization adjustments (from JEP text):

- `IO` is in **`java.lang`** (implicitly available).  
- `IO` static methods are **not** implicitly statically imported — call `IO.println(...)` or import explicitly.  
- `IO` implementation based on `System.out` / `System.in`.

Compact sources behave as if `import module java.base` is present (with JEP 511).

## Internal Behavior

Compiler/launcher still map to classes/methods; on-ramp features desugar into ordinary Java program structure. Same `javac` / `java` tools.

## Production Example

Scripts, kata, CLIs, teaching. Production services still use normal modules/packages; migrating compact → ordinary class is intentional growth path.

## Limitations

- Not a replacement for packaging/modularity in large apps.  
- Instance vs static main selection rules must be learned when growing the program.  
- Don’t confuse with a new language dialect — interviewers watch for that mistake.

## Migration Considerations

Adopt freely for small programs on JDK 25+. For shared libraries, prefer explicit classes. Educate teams on `IO` calling convention after finalization changes.

## Interview Questions

1. Is this a separate dialect?  
2. What is an instance main?  
3. Where does `IO` live and how is it invoked?  
4. Relationship to module imports?

### Related

[jep-511-module-import-declarations.md](./jep-511-module-import-declarations.md)
