# Primitive Types

Built-in value types: exact sizes, no object header, no `null`.

## 1. Mental Model

```text
int amountCents;     // 32-bit value on stack / in field
Integer boxed;       // reference → heap object (or cached)
```

## 2. Simple Explanation

Primitives (`byte`…`boolean`) hold values directly. They are not objects and cannot be `null`. Use them for numbers, flags, and performance-sensitive fields — **not** for money as `double`.

## 3. Technical Explanation

| Type | Bits | Notes |
|------|------|-------|
| `byte` `short` `int` `long` | 8–64 | Two’s complement |
| `float` `double` | 32/64 | IEEE-754 — **not** exact decimals |
| `char` | 16 | UTF-16 code unit |
| `boolean` | JVM-defined | Prefer for flags |

Autoboxing wraps to `Integer` etc.; unboxing can NPE.

## 4. Internal Behavior

Locals often live in stack frames / registers. Fields pack into object layout. Boxing allocates (except cache ranges like `Integer` −128…127). Arithmetic wraps on overflow for ints; floats have NaN/±∞.

## 5. Java 25 Example

```java
long amountCents = 19_99L;          // money as integer minor units
boolean settled = true;
double ratio = 0.1 + 0.2;           // NOT money — floating error
```

## 6. Real-World Scenario

**Checkout totals:** team stored prices in `double`; pennies drifted after tax. Migrated to `long` cents (or `BigDecimal` where needed). Audit found historical invoice mismatches.

## 7. Common Mistake

`double` for currency; comparing floats with `==`; ignoring int overflow in counters.

## 8. Failure Scenario

Symptom: “totals off by 0.01.” Cause: binary floating money. Fix: integer minor units / decimal types. Prevent: domain money type + review checklist.

## 9. Performance Implications

Primitives avoid allocation and indirection. Boxing in hot loops creates GC pressure. Prefer primitive collections or careful design when scale is huge.

## 10. Interview Questions

- Primitive vs wrapper?  
- Why not `double` for money?

## 11. Senior-Level Follow-ups

- When `BigDecimal` vs `long` cents?  
- Cost of autoboxing in streams?

## 12. Principal Engineer Perspective

Encode domain quantities in types that match **exactness** needs. Ban floating money in code review; measure boxing only when profiles show it.

### Related

[reference-types.md](./reference-types.md) · [operators.md](./operators.md) · [variables.md](./variables.md)
