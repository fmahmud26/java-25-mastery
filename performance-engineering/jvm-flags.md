# JVM Flags (Performance Discipline)

Flags are experiments, not personality.

## Rules

1. Baseline without the flag.  
2. Change one flag (or one related set you can justify).  
3. Same workload; record command line in the report.  
4. Revert if secondary metrics regress.  

## Common knobs (examples — not prescriptions)

| Flag area | When you might touch |
|-----------|----------------------|
| `-Xms/-Xmx` | Sizing experiment |
| GC select | After proof current GC can’t meet tails |
| JFR start | Observability |
| Compact headers (25) | Heap footprint bakeoff |

Avoid cargo-cult 20-flag GC strings copied from blogs.

### Related

[scientific-method.md](./scientific-method.md) · [benchmarking.md](./benchmarking.md)
