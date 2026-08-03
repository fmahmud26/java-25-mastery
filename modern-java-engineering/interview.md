# Interview — Modern Java Engineering

## Format

Before → After → Trade-off → When you’d choose differently.

---

### Immutability?

Safer sharing; records + copyOf; trade alloc vs races. JPA at edges.

---

### Optional?

Return maybe-one; don’t use as fields everywhere; prefer parsing to non-null types.

---

### Records / sealed?

Values + closed hierarchies; exhaustive switch; not for open SPI.

---

### API design?

Commands, small surface, no mutable leaks, evolve carefully.

---

### Logging vs observability?

Logs narrate; metrics/traces quantify. Correlation ids. No secrets.

---

### Error handling?

Typed business outcomes; don’t swallow; map to stable API errors.

---

### PE prompt

“How do you keep a checkout service maintainable for 5 years?” → boundaries, tests, observability, typed domain, complexity budget.

### Related

[README.md](./README.md) · [principal-decisions.md](./principal-decisions.md) · [trade-offs.md](./trade-offs.md)
