# Java 25 — Cheat Sheet

**Sources only:** [../16-java-25-features/README.md](../16-java-25-features/README.md) · [feature-status.md](../16-java-25-features/feature-status.md) · [lts-evolution.md](../16-java-25-features/lts-evolution.md) · [java-interview-questions/java-25](../java-interview-questions/java-25/)

## Maturity (say this aloud)

| Status | Prod? |
|--------|-------|
| Final / product | Yes (some JVM flags still opt-in) |
| Preview | Needs `--enable-preview`; avoid hard deps |
| Incubator | `jdk.incubator.*` — experiments |
| Experimental | Opt-in tooling/JVM |

## JDK 25 JEPs (18) — recall map

**Final / product:** 506 Scoped Values · 510 KDF API · 511 Module import · 512 Compact source / instance main · 513 Flexible constructors · 503 Remove 32-bit x86 · 514 AOT CLI ergonomics · 515 AOT method profiling · 518 JFR cooperative sampling · 519 Compact object headers · 520 JFR method timing/tracing · 521 Generational Shenandoah  

**Preview:** 470 PEM · 502 Stable Values · 505 Structured Concurrency (5th preview) · 507 Primitive patterns (3rd preview)  

**Incubator:** 508 Vector API  

**Experimental:** 509 JFR CPU-time profiling  

→ Full tables + per-JEP guides: [16-java-25-features](../16-java-25-features/)

## Interview triggers

| Prompt | Answer spine |
|--------|----------------|
| “What’s new in 25?” | List from **official 18 JEPs** only; split Final vs Preview |
| Compact headers | Product feature (519); **not** “enabled by default everywhere” without checking flags — see feature guide |
| Structured concurrency | Still **preview** on 25 — don’t claim final |
| Scoped Values | **Final** (506); contrast ThreadLocal — [scoped-values feature](../16-java-25-features/features/jep-506-scoped-values.md) |
| Upgrade 17→25 | [migration guides](../16-java-25-features/migration-17-to-25.md) + Principal Q in bank |

## LTS ladder (context, not “new in 25”)

8 → 11 → 17 → 21 (VT, …) → **25** — details: [lts-evolution.md](../16-java-25-features/lts-evolution.md)

## Do not invent

No claiming JEP 523 / “G1 everywhere” as Java 25 — called out in [garbage-collection](../garbage-collection/README.md).
