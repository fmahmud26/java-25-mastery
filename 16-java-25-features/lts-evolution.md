# LTS Evolution: Java 8 → 11 → 17 → 21 → 25

Interview-grade arc. Dates are approximate GA years; vendors’ LTS support windows differ — check your vendor.

```text
Java 8 (2014) → 11 (2018) → 17 (2021) → 21 (2023) → 25 (2025)
```

JDK 25 GA: **16 September 2025** ([OpenJDK JDK 25](https://openjdk.org/projects/jdk/25/)).

---

## Java 8 → 11

| Theme | Highlights |
|-------|------------|
| Language | `var` (10); local-variable syntax for lambda (11) |
| APIs | Standardized HTTP Client; richer `String` / `Files` APIs |
| Platform | **JPMS** (9); Java EE / CORBA modules removed from SE; JavaFX removed from JDK |
| Ops | Six-month release train; LTS model becomes the migration backbone |

**Sound bite:** modules + modern HTTP/String; EE out of SE.

---

## Java 11 → 17

| Theme | Highlights |
|-------|------------|
| Language | Switch **expressions**; text blocks; pattern `instanceof`; **records**; **sealed** classes |
| Encapsulation | Stronger module boundaries by default |
| GC | ZGC & Shenandoah production-ready in this era; G1 continues as workhorse |
| Tooling | `jpackage`, ongoing removals of obsolete APIs |

**Sound bite:** data-oriented language features (records/sealed) + encapsulation.

---

## Java 17 → 21

| Theme | Highlights (JDK 21 JEPs — previous LTS) |
|-------|----------------------------------------|
| Concurrency | **Virtual threads** (JEP 444) |
| Language | Pattern matching for `switch`; record patterns |
| Collections | Sequenced collections |
| Security | KEM API (building block toward PQC) |
| Previews then | Structured concurrency, scoped values (still evolving toward 25) |

**Sound bite:** Loom virtual threads + pattern switch — largest concurrency leap since 8.

---

## Java 21 → 25

Between LTS releases, JDK **22–24** delivered major finals (examples): Foreign Function & Memory API, Class-File API, Stream Gatherers, ZGC generational-only path, virtual-thread pinning fixes (JEP 491), Security Manager permanently disabled, etc. See [jeps-since-21.md](./jeps-since-21.md).

**JDK 25 itself** adds the [18 JEPs](https://openjdk.org/projects/jdk/25/) indexed in [README.md](./README.md), notably:

| Theme | JDK 25 |
|-------|--------|
| Language | Module imports; compact source / instance main; flexible constructors |
| Concurrency | **Scoped Values final**; Structured Concurrency still **preview** |
| Security | **KDF API final**; PEM encodings **preview** |
| Runtime | Compact object headers (product, not default); AOT ergonomics/profiling; JFR sampling/timing; generational Shenandoah as product mode option |
| Platform | 32-bit x86 port **removed** |

**Sound bite:** finalize scoped values + simpler Java for small programs; Leyden/AOT ergonomics; Lilliput headers; integrity and observability.

---

## One-page LTS headlines

| LTS | Headline |
|-----|----------|
| **8** | Lambdas, streams, `java.time` |
| **11** | Modules in practice; HTTP Client; `var` |
| **17** | Records, sealed, text blocks, modern patterns |
| **21** | Virtual threads, pattern `switch`, record patterns |
| **25** | Scoped values, compact programs, KDF, compact headers, AOT ergonomics |

### Related

[migration-21-to-25.md](./migration-21-to-25.md) · [migration-17-to-25.md](./migration-17-to-25.md) · [README.md](./README.md)
