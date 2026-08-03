# JEPs Since JDK 21 (Context for Java 25)

JDK 25’s **own** feature list is the [18 JEPs on the JDK 25 project page](https://openjdk.org/projects/jdk/25/). Between LTS 21 and 25, many other JEPs shipped in 22–24. This page is **context for migration interviews**, not “new in 25.”

Authoritative index: [JEPs integrated since JDK 21](https://openjdk.org/projects/jdk/25/jeps-since-jdk-21).

## High-signal finals between 21 and 25 (examples)

| Area | Examples (release in parentheses per OpenJDK index) |
|------|------------------------------------------------------|
| FFM | Foreign Function & Memory API final (22) |
| Class-File | Class-File API (24) |
| Streams | Stream Gatherers (24) |
| Language | Unnamed variables & patterns (22) |
| VT | Synchronize virtual threads without pinning (24) |
| ZGC | Generational mode default (23); remove non-generational (24) |
| Security | Security Manager permanently disabled (24); ML-DSA / ML-KEM (24) |
| AOT | Ahead-of-Time Class Loading & Linking (24) |
| Docs | Markdown documentation comments (23) |

When interviewing on “What’s new in 25?”, answer with the **18 JEPs**. When interviewing on “What’s new since 21 LTS?”, include 22–24 + 25.

### Related

[lts-evolution.md](./lts-evolution.md) · [migration-21-to-25.md](./migration-21-to-25.md) · [README.md](./README.md)
