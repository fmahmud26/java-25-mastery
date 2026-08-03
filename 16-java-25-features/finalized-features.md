# Finalized / Product Features in JDK 25

No `--enable-preview` required for language/API finals. Some HotSpot features are **product but opt-in**.

## Language (SE Final)

| JEP | Guide |
|-----|-------|
| 511 Module Import Declarations | [features/jep-511-module-import-declarations.md](./features/jep-511-module-import-declarations.md) |
| 512 Compact Source Files & Instance Main | [features/jep-512-compact-source-files.md](./features/jep-512-compact-source-files.md) |
| 513 Flexible Constructor Bodies | [features/jep-513-flexible-constructor-bodies.md](./features/jep-513-flexible-constructor-bodies.md) |

## Libraries (SE Final)

| JEP | Guide |
|-----|-------|
| 506 Scoped Values | [features/jep-506-scoped-values.md](./features/jep-506-scoped-values.md) |
| 510 Key Derivation Function API | [features/jep-510-kdf-api.md](./features/jep-510-kdf-api.md) |

## HotSpot / Tools / Platform (Product)

| JEP | Notes | Guide |
|-----|-------|-------|
| 503 Remove 32-bit x86 Port | Hard removal | [features/jep-503-remove-32bit-x86.md](./features/jep-503-remove-32bit-x86.md) |
| 514 AOT CLI Ergonomics | Leyden UX | [features/jep-514-aot-cli-ergonomics.md](./features/jep-514-aot-cli-ergonomics.md) |
| 515 AOT Method Profiling | Leyden profiles | [features/jep-515-aot-method-profiling.md](./features/jep-515-aot-method-profiling.md) |
| 518 JFR Cooperative Sampling | JFR foundation | [features/jep-518-jfr-cooperative-sampling.md](./features/jep-518-jfr-cooperative-sampling.md) |
| 519 Compact Object Headers | Product; **not default** | [features/jep-519-compact-object-headers.md](./features/jep-519-compact-object-headers.md) |
| 520 JFR Method Timing & Tracing | JFR | [features/jep-520-jfr-method-timing-tracing.md](./features/jep-520-jfr-method-timing-tracing.md) |
| 521 Generational Shenandoah | Product **mode**; Shenandoah default mode still non-gen | [features/jep-521-generational-shenandoah.md](./features/jep-521-generational-shenandoah.md) |

### Related

[preview-features.md](./preview-features.md) · [feature-status.md](./feature-status.md) · [README.md](./README.md)
