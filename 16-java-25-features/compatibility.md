# Compatibility Notes — Toward JDK 25

## Removals / platform

- **32-bit x86 port removed** (JEP 503) — hard stop for that arch.  
- Security Manager permanently disabled path completed in JDK 24 — apps must not rely on it.  

## Integrity / native

Expect continued pressure on unsafe / JNI unrestricted use (22–24 JEPs). Retest agents, ByteBuddy, native libs on 25.

## Preview drift

Structured Concurrency APIs changed across previews (constructors → factories in JEP 505). Code written against 21 preview may not compile on 25 preview without edits.

## GC / headers

Compact headers and generational Shenandoah are **opt-in** product features — enabling them is a behavior change to validate, not an automatic upgrade side effect.

## Verification

```text
Compile on 25 → unit/integration → soak with JFR → chaos (kill/restart) → AOT training optional
```

### Related

[migration-21-to-25.md](./migration-21-to-25.md) · [features/jep-503-remove-32bit-x86.md](./features/jep-503-remove-32bit-x86.md)
