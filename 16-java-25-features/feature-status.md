# Feature Status Taxonomy (Java / JDK)

How to read “Final / Preview / Incubator / Experimental” correctly in interviews and ADRs.

## Final (Standard SE or Product JDK feature)

| Kind | Meaning |
|------|---------|
| **SE Final** | Part of the Java SE Platform specification (language or standard API). No `--enable-preview`. |
| **Product HotSpot feature** | Delivered as a supported JDK/HotSpot capability; may still be **off by default** (e.g. compact object headers via `-XX:+UseCompactObjectHeaders`). |

**Production:** Allowed. Prefer explicit flags documented in runbooks when opt-in.

## Preview

- Specified for developer feedback; **may change or be removed**.  
- Compile/run with `--enable-preview` (and matching `--release` / source level as required).  
- **Do not** build irreversible production contracts on preview APIs without an escape plan.

Examples in JDK 25: JEP 470, 502, 505, 507.

## Incubator

- Lives in `jdk.incubator.*` modules.  
- Must add modules (`--add-modules`).  
- Expected to change package/API before finalization.  

Example in JDK 25: JEP 508 Vector API (tenth incubator).

## Experimental

- Under evaluation; quality/stability bar differs from product features.  
- May require `-XX:+UnlockExperimentalVMOptions` **or** be exposed as `@Experimental` JFR events (see JEP 509 — CPU-time events do not require the unlock flag).  
- Treat as opt-in diagnostics / early access.

Example in JDK 25: JEP 509.

## Interview one-liner

> Preview needs `--enable-preview` and can change; incubator is a separate module; experimental is JVM/tooling trial; final SE APIs are production-safe; some final *HotSpot* features are still flag-gated.

### Related

[README.md](./README.md) · [preview-features.md](./preview-features.md) · [experimental-features.md](./experimental-features.md)
