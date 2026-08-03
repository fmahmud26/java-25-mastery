# Operators

Expressions for arithmetic, comparison, logic, bitwise, and assignment — including precedence and short-circuit.

## 1. Mental Model

```text
a && b()     // if a is false, b() never runs
i++          // use then increment (easy off-by-one in indexes)
```

## 2. Simple Explanation

Operators combine values. Know precedence, associativity, overflow, and that `&&`/`||` short-circuit. Prefer clear parentheses over “clever” precedence tricks.

## 3. Technical Explanation

| Family | Examples | Notes |
|--------|----------|-------|
| Arithmetic | `+ - * / %` | Int overflow wraps; `/0` throws for ints |
| Relational | `== != < >` | Reference `==` is identity |
| Logical | `&&` `||` `!` | Short-circuit |
| Bitwise | `& \| ^ ~ << >> >>>` | Prefer for flags/masks, not casual logic |
| Assignment | `= +=` | Right-associative |
| Ternary | `? :` | Expression form of if |
| `instanceof` | pattern matching | Prefer modern patterns |

## 4. Internal Behavior

Bytecode: `iadd`, `if_icmpeq`, etc. Short-circuit becomes conditional jumps. Compound assign may imply casting. Floating compares treat NaN specially.

## 5. Java 25 Example

```java
boolean eligible = customerActive && balanceCents >= minCents && !fraudFlag;
int next = page * pageSize;          // watch overflow for large page
String label = settled ? "PAID" : "OPEN";
if (payload instanceof RefundRequest rr && rr.amountCents() > 0) { ... }
```

## 6. Real-World Scenario

**Fraud check:** `isVip | scoreHigh()` used bitwise `|` so `scoreHigh()` always ran (expensive remote call). Switched to `||`. Latency and vendor cost dropped.

## 7. Common Mistake

`=` vs `==` in conditions; `&`/`|` instead of `&&`/`||`; floating `==`; unexpected precedence (`a << 2 + 1`).

## 8. Failure Scenario

Integer overflow in `page * size` → negative offset → SQL error or empty page. Use `Math.multiplyExact` or long + bounds checks.

## 9. Performance Implications

Short-circuit avoids work. Bit tricks rarely beat clear code unless proven hot. Autoboxing with operators can hide allocations.

## 10. Interview Questions

- `&&` vs `&`?  
- What happens on `int` overflow?

## 11. Senior-Level Follow-ups

- Safe paging arithmetic at scale?  
- When are bitwise flags justified?

## 12. Principal Engineer Perspective

Optimize for **correctness and readability**. Ban clever operator golf in payment/auth paths; add exact math where overflow is a business hazard.

### Related

[primitive-types.md](./primitive-types.md) · [control-flow.md](./control-flow.md) · [loops.md](./loops.md)
