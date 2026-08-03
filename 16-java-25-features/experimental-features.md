# Incubator & Experimental in JDK 25

## Incubator

| JEP | Guide |
|-----|-------|
| 508 Vector API (Tenth Incubator) | [features/jep-508-vector-api.md](./features/jep-508-vector-api.md) |

```bash
javac --add-modules jdk.incubator.vector ...
java  --add-modules jdk.incubator.vector ...
```

## Experimental

| JEP | Guide |
|-----|-------|
| 509 JFR CPU-Time Profiling (Experimental) | [features/jep-509-jfr-cpu-time-profiling.md](./features/jep-509-jfr-cpu-time-profiling.md) |

Linux CPU-time JFR events; `@Experimental` event types; per JEP 509, **no** `UnlockExperimentalVMOptions` required for those events.

### Related

[feature-status.md](./feature-status.md) · [preview-features.md](./preview-features.md)
