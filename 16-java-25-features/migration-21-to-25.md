# Migration: Java 21 → 25

## Goals

Move an LTS 21 estate to LTS 25 with explicit handling of finals vs preview, and 22–24 intervening changes.

## Checklist

1. **Inventory** — Java version, GC, agents, JNI/FFM, Security Manager usage, 32-bit (must be gone).  
2. **Build** — toolchain 25; fix removals/deprecations from 22–24 (Unsafe memory accessors warnings path, Security Manager, etc.). See [jeps-since-21.md](./jeps-since-21.md).  
3. **Adopt finals carefully** — Scoped Values where ThreadLocal context fits; KDF for new crypto; language ergonomics as needed.  
4. **Do not require preview** — Structured Concurrency, Stable Values, PEM, primitive patterns stay optional.  
5. **Runtime flags** — trial `-XX:+UseCompactObjectHeaders`; AOT cache in CI; Shenandoah gen mode only if already on Shenandoah.  
6. **Verify** — soak tests, GC/JFR baselines, startup with/without AOT.

## Compatibility Hotspots

| Topic | Action |
|-------|--------|
| 32-bit x86 | Unsupported (JEP 503) |
| Security Manager | Already disabled permanently in 24 — ensure no dependency |
| Preview APIs from 21 | Re-check; Structured Concurrency API changed again in 25 |
| Agents / native | Retest on 25 |

### Related

[migration-17-to-25.md](./migration-17-to-25.md) · [compatibility.md](./compatibility.md) · [lts-evolution.md](./lts-evolution.md)
