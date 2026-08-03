# Interview — Java 25 Features

Authoritative release list: [JDK 25](https://openjdk.org/projects/jdk/25/) (18 JEPs). Don’t invent features.

---

## Status literacy (must-have)

| Status | Enable how? | Prod? |
|--------|-------------|-------|
| Final SE | Normal | Yes |
| Product HotSpot opt-in | Flag (e.g. compact headers) | Yes after bakeoff |
| Preview | `--enable-preview` | Avoid hard deps |
| Incubator | `--add-modules jdk.incubator.*` | No |
| Experimental | Per feature (JFR `@Experimental` etc.) | Opt-in |

---

## “What’s new in Java 25?” (crisp answer)

**Language finals:** module imports; compact source/instance main; flexible constructors.  
**API finals:** Scoped Values; KDF API.  
**Runtime:** AOT ergonomics/profiling; JFR sampling/timing; compact headers (opt-in); generational Shenandoah mode (opt-in); 32-bit x86 removed.  
**Still preview:** structured concurrency; stable values; PEM; primitive patterns.  
**Incubator:** Vector API. **Experimental:** JFR CPU-time profiling (Linux).

---

## LTS arc sound bites

| LTS | Line |
|-----|------|
| 8 | Lambdas/streams/time |
| 11 | Modules + HTTP client + var |
| 17 | Records/sealed/text blocks |
| 21 | Virtual threads + pattern switch |
| 25 | Scoped values + simpler source + AOT/headers polish |

---

## Deep follow-ups

1. ScopedValue vs ThreadLocal?  
2. Why Structured Concurrency still preview? What changed in JEP 505?  
3. Compact headers default in 25? (No.)  
4. Generational Shenandoah default mode? (No — mode opt-in; collector still opt-in.)  
5. AOTCacheOutput vs two-step AOT?  
6. Final vs preview vs incubator vs experimental?  
7. What’s new *since 21* vs *in 25*? ([jeps-since-21.md](./jeps-since-21.md))

---

## Trap questions

| Trap | Correct |
|------|---------|
| “Valhalla is in 25” | Not as a JDK 25 JEP on the project page — don’t invent |
| “Structured concurrency final” | Still preview (505) |
| “ZGC added in 25” | ZGC existed earlier; 25’s GC JEP is generational Shenandoah productization |
| “Compact headers on by default” | Product feature, not default (519) |

**PE line:** Quote JEPs; separate final from preview; measure opt-in runtime features.

### Related

[README.md](./README.md) · [lts-evolution.md](./lts-evolution.md) · [feature-status.md](./feature-status.md)
