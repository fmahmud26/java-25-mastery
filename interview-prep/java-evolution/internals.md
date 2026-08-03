# Java Evolution — Internals

What changed under the hood that seniors should name.

## Platform shifts

| Area | Evolution note |
|------|----------------|
| Language → bytecode | Lambdas = invokedynamic + bootstrap; records = final class + components |
| Modules (9+) | Strong encapsulation of JDK internals; `--add-opens` migration pain |
| G1 default | Long path from Parallel/CMS era → G1/ZGC options on modern JDKs |
| Loom | Virtual threads: mount/unmount on carriers; pinning caveats matured across 21→25 |
| Pattern matching | Compiler exhaustiveness for sealed; safer refactors |

## Compatibility mental model

- **Source / binary / behavioral** compatibility are different (serialization, `hashCode` of records, deprecated removals).
- Illegal reflective access tightened over releases — libraries must adapt.
- Preview APIs need `--enable-preview`; don’t ship previews as “done” without policy.

## LTS vs non-LTS

- Enterprises jump LTS→LTS (17→21→25) more than every six-month release.
- Intermediate releases still matter for early feature bake time.

Related: [compatibility.md](../../16-java-25-features/compatibility.md), [jvm-capabilities.md](../../16-java-25-features/jvm-capabilities.md), [module-system.md](../../modern-java/module-system.md).
