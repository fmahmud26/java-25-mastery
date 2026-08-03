# Interview — Modern Java (8 → 25)

Drill aloud. Prefer **before → after** and **migration** answers.

---

## How has Java evolved from 8 to 25?

| Era | LTS | Headline |
|-----|-----|----------|
| 8 | 8 | Lambdas, streams, Optional, `java.time` |
| 9–11 | 11 | Modules, `var`, HttpClient, collection factories |
| 12–17 | 17 | Switch expr, text blocks, records, sealed, `instanceof` patterns |
| 18–21 | 21 | Pattern switch, record patterns, sequenced collections, virtual threads |
| 22–25 | **25** | `_`, compact source/instance main, module import; primitive patterns **preview** |

**One-liner:** From functional APIs (8) to **data-oriented** language (records/sealed/patterns) and a modular JDK — Java 25 is the LTS to standardize on now.

Detail: [java-evolution.md](./java-evolution.md)

---

## Quick fire

| Topic | Sketch |
|-------|--------|
| Lambda vs anonymous class | SAM + `this` semantics; invokedynamic |
| Functional interface | One abstract method; `@FunctionalInterface` |
| Optional | Return-type absence; not fields/params |
| `var` | Local inference; still static |
| Switch expression | Value + exhaustiveness; no `->` fall-through |
| Text blocks | Readable multi-line; indentation from closing `"""` |
| Records | Immutable carriers; generate equals/etc. |
| Sealed | Closed permits; exhaustive switch |
| Record patterns | Deconstruct in match |
| `List.of` | Immutable; no null |
| Preview | `--enable-preview`; don’t ship casually |

---

## Before / after prompts (practice)

1. Replace Comparator anonymous class → lambda / method ref.  
2. Replace null-returning `find` → `Optional`.  
3. Replace payment DTO POJO → `record`.  
4. Replace `instanceof` chain on events → sealed + pattern switch.  
5. Replace SQL concatenation → text block + `PreparedStatement`.  
6. Replace `Collections.unmodifiableList(Arrays.asList(...))` → `List.of` / `copyOf`.

---

## Migration decision prompts (Principal)

1. Phase plan 11 → 25 for a payments monolith.  
2. What do you adopt first vs defer (JPMS, VT, preview)?  
3. How do you handle Jackson + records?  
4. When keep classic `switch` / loops?  
5. How do you stop silent `default` branches from dropping events?

---

## Feature deep-links

[lambdas](./lambdas.md) · [functional-interfaces](./functional-interfaces.md) · [optional](./optional.md) · [var](./var.md) · [switch-expressions](./switch-expressions.md) · [text-blocks](./text-blocks.md) · [records](./records.md) · [sealed-classes](./sealed-classes.md) · [pattern-matching](./pattern-matching.md) · [record-patterns](./record-patterns.md) · [modern-apis](./modern-apis.md) · [modern-collection-apis](./modern-collection-apis.md) · [modern-coding-style](./modern-coding-style.md) · [module-system](./module-system.md)

### Related

[README.md](./README.md) · [java-evolution.md](./java-evolution.md)
