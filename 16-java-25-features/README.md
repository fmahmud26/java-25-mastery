# Java 25 Features — Definitive Guide

Authoritative index of **JDK 25** (GA 16 September 2025), the Reference Implementation of Java SE 25 ([JSR 400](https://openjdk.org/projects/jdk/25/spec/)).

**Source of truth for this release’s JEPs:** [openjdk.org/projects/jdk/25](https://openjdk.org/projects/jdk/25/) · Mark Reinhold GA announcement (18 JEPs).

**Do not invent features.** Only JEPs listed for JDK 25 appear as “Java 25 features” below. JEPs delivered in 22–24 are covered under [LTS evolution](./lts-evolution.md) / migration notes, not as “new in 25.”

**Standards:** [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md) · [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md)

---

## Feature maturity (read first)

| Status | Meaning | Production? |
|--------|---------|-------------|
| **Final** (standard / product) | Complete; no special flags for language/API; some JVM features are opt-in product flags | Yes (vendor support permitting) |
| **Preview** | Complete design for feedback; may change/remove; needs `--enable-preview` | Avoid as hard dependency |
| **Incubator** | API in `jdk.incubator.*`; module must be added; expected to evolve | Experiments only |
| **Experimental** | JVM/tooling under evaluation; may need unlock or be tagged `@Experimental` | Opt-in, expect change |

Full taxonomy: [feature-status.md](./feature-status.md)

---

## JDK 25 JEPs (complete list — 18)

### Final / product

| JEP | Topic | Guide |
|-----|-------|-------|
| [506](https://openjdk.org/jeps/506) | Scoped Values | [features/jep-506-scoped-values.md](./features/jep-506-scoped-values.md) |
| [510](https://openjdk.org/jeps/510) | Key Derivation Function API | [features/jep-510-kdf-api.md](./features/jep-510-kdf-api.md) |
| [511](https://openjdk.org/jeps/511) | Module Import Declarations | [features/jep-511-module-import-declarations.md](./features/jep-511-module-import-declarations.md) |
| [512](https://openjdk.org/jeps/512) | Compact Source Files and Instance Main Methods | [features/jep-512-compact-source-files.md](./features/jep-512-compact-source-files.md) |
| [513](https://openjdk.org/jeps/513) | Flexible Constructor Bodies | [features/jep-513-flexible-constructor-bodies.md](./features/jep-513-flexible-constructor-bodies.md) |
| [503](https://openjdk.org/jeps/503) | Remove the 32-bit x86 Port | [features/jep-503-remove-32bit-x86.md](./features/jep-503-remove-32bit-x86.md) |
| [514](https://openjdk.org/jeps/514) | Ahead-of-Time Command-Line Ergonomics | [features/jep-514-aot-cli-ergonomics.md](./features/jep-514-aot-cli-ergonomics.md) |
| [515](https://openjdk.org/jeps/515) | Ahead-of-Time Method Profiling | [features/jep-515-aot-method-profiling.md](./features/jep-515-aot-method-profiling.md) |
| [518](https://openjdk.org/jeps/518) | JFR Cooperative Sampling | [features/jep-518-jfr-cooperative-sampling.md](./features/jep-518-jfr-cooperative-sampling.md) |
| [519](https://openjdk.org/jeps/519) | Compact Object Headers | [features/jep-519-compact-object-headers.md](./features/jep-519-compact-object-headers.md) |
| [520](https://openjdk.org/jeps/520) | JFR Method Timing & Tracing | [features/jep-520-jfr-method-timing-tracing.md](./features/jep-520-jfr-method-timing-tracing.md) |
| [521](https://openjdk.org/jeps/521) | Generational Shenandoah | [features/jep-521-generational-shenandoah.md](./features/jep-521-generational-shenandoah.md) |

### Preview

| JEP | Topic | Guide |
|-----|-------|-------|
| [470](https://openjdk.org/jeps/470) | PEM Encodings of Cryptographic Objects | [features/jep-470-pem-encodings.md](./features/jep-470-pem-encodings.md) |
| [502](https://openjdk.org/jeps/502) | Stable Values | [features/jep-502-stable-values.md](./features/jep-502-stable-values.md) |
| [505](https://openjdk.org/jeps/505) | Structured Concurrency (Fifth Preview) | [features/jep-505-structured-concurrency.md](./features/jep-505-structured-concurrency.md) |
| [507](https://openjdk.org/jeps/507) | Primitive Types in Patterns, `instanceof`, and `switch` (Third Preview) | [features/jep-507-primitive-patterns.md](./features/jep-507-primitive-patterns.md) |

### Incubator

| JEP | Topic | Guide |
|-----|-------|-------|
| [508](https://openjdk.org/jeps/508) | Vector API (Tenth Incubator) | [features/jep-508-vector-api.md](./features/jep-508-vector-api.md) |

### Experimental

| JEP | Topic | Guide |
|-----|-------|-------|
| [509](https://openjdk.org/jeps/509) | JFR CPU-Time Profiling | [features/jep-509-jfr-cpu-time-profiling.md](./features/jep-509-jfr-cpu-time-profiling.md) |

Indexes: [finalized-features.md](./finalized-features.md) · [preview-features.md](./preview-features.md) · [experimental-features.md](./experimental-features.md)

---

## Evolution & migration

| Doc | Purpose |
|-----|---------|
| [lts-evolution.md](./lts-evolution.md) | Java **8 → 11 → 17 → 21 → 25** |
| [migration-21-to-25.md](./migration-21-to-25.md) | From previous LTS |
| [migration-17-to-25.md](./migration-17-to-25.md) | Two-LTS jump |
| [compatibility.md](./compatibility.md) | Breakages / removals awareness |
| [jeps-since-21.md](./jeps-since-21.md) | What landed between 21 and 25 (context) |

## Thematic maps

| Doc | Focus |
|-----|-------|
| [language-changes.md](./language-changes.md) | Language JEPs in 25 |
| [api-changes.md](./api-changes.md) | Library JEPs in 25 |
| [jvm-capabilities.md](./jvm-capabilities.md) | HotSpot / JFR / AOT / GC |
| [performance.md](./performance.md) | What to measure (no invented claims) |
| [interview.md](./interview.md) | Interview bank |

---

## Per-feature template (used in `features/`)

Every feature page covers: **JEP · status · purpose · problem · previous approach · new approach · syntax/API · internal behavior · production example · limitations · migration · interview questions**.
