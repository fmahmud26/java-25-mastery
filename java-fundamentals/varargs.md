# Varargs

Variable arity parameters — sugar over arrays at the call site.

## 1. Mental Model

```text
log(String msg, Object... args)
     ↓
caller: log("x={}", a, b)  →  Object[] {a, b} created
```

## 2. Simple Explanation

`T...` means “zero or more `T`,” implemented as a `T[]`. Convenient for APIs like logging and builders; dangerous when overloaded with fixed arity or when the array is stored and mutated.

## 3. Technical Explanation

- Must be the **last** parameter.  
- Only one varargs parameter.  
- Call with list, array, or nothing (`length 0`).  
- Overload resolution prefers fixed arity when ambiguous — source of subtle bugs.  
- Heap pollution risk with generic varargs → often `@SafeVarargs` on non-escaping cases.

## 4. Internal Behavior

Compiler allocates an array at the call site (unless passed explicitly). That array is mutable. Storing it in a field shares a caller-controlled buffer if they passed an array.

## 5. Java 25 Example

```java
void audit(String event, String... attributes) {
    sink.write(event, List.of(attributes)); // copy if retaining
}

audit("PAYMENT_CAPTURED", "paymentId=" + id, "cents=" + cents);
audit("HEARTBEAT"); // empty varargs
```

## 6. Real-World Scenario

**Metrics helper:** `gauge(String name, String... tags)` overloaded with `gauge(String name, List<String> tags)`. Call with one string tag bound to the wrong overload. Collapsed to a single `Tags` type.

## 7. Common Mistake

Overloading varargs with similar fixed methods; retaining the varargs array without copying; `null` passed as single argument meaning “array is null” vs empty.

## 8. Failure Scenario

`NullPointerException` iterating varargs when caller passed `(String[]) null`. Treat null array as empty or reject explicitly.

## 9. Performance Implications

Each call may allocate an array — fine for rare APIs; avoid on ultra-hot paths or accept the cost / provide array overload.

## 10. Interview Questions

- What is `T...`?  
- Why last parameter only?

## 11. Senior-Level Follow-ups

- Heap pollution and `@SafeVarargs`?  
- When ban varargs in a public API?

## 12. Principal Engineer Perspective

Varargs are sugar for **call-site ergonomics**, not a data model. Prefer explicit collections for domain payloads; keep varargs for logging/format-style helpers.

### Related

[methods.md](./methods.md) · [arrays.md](./arrays.md)
