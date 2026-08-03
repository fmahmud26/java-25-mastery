# Type Erasure

Generics are a **compile-time** feature. Type arguments are erased from ordinary class bytecode so the JVM runs compatible, non-reified types.

## Mental Model

```text
Source:   List<String>   Box<T extends Number>
Bytecode: List (+ casts) Box with Number (bound)
Runtime:  no List<String> class distinct from List<Integer>
```

## Why Java Uses Type Erasure

| Reason | Explanation |
|--------|-------------|
| Migration compatibility | Java 5 could add generics without breaking existing `.class` / JVM consumers — raw `List` kept working |
| One JVM type | Avoid exploding runtime instantiations (`Box_String`, `Box_Integer`, …) like some specialized models |
| Simpler VM story (at the time) | Type checking pushed to `javac`; VM stayed mostly oblivious to type args |

This is a **deliberate trade-off**: great interoperability and smaller runtime model, weaker runtime type information versus reified generics (e.g. C#).

## Simple Example

```java
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
boolean sameRuntimeClass = a.getClass() == b.getClass(); // true — both ArrayList
```

## Advanced Example (what javac inserts)

```java
public final class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T value) { this.value = value; }
}

Box<String> box = new Box<>();
box.set("pay");
String s = box.get();
// roughly: String s = (String) box.get();
```

Bounded:

```java
public static <T extends Number> T pick(T a, T b) { return a; }
// T erased to Number; return cast to T at call site if needed
```

Bridge methods appear when overriding generic methods with refined types — synthetic methods preserve polymorphism after erasure.

## Internal Behavior

| Mechanism | Effect |
|-----------|--------|
| Erase to Object/bound | Fields/signatures |
| Cast insertion | Call sites restore compile-time type |
| Bridge methods | Binary-compatible overrides |
| Unchecked warnings | Programmer asserted something erasure can’t prove |

## Production Use Case

You pass `Class<T>` / Jackson `TypeReference` / gRPC TypeDescriptors because erasure removed nested type args. Frameworks reify **by carrying tokens**, not by JVM magic.

## Common Mistake

```java
if (value instanceof List<String>) { } // illegal / erased
if (value instanceof List<?>) { }      // OK — raw-ish check
```

Relying on `getClass().getTypeParameters()` to recover `String` from `List<String>` instance — you won’t get the argument.

## Interview Trap

“Erasure means generics are only syntactic sugar with no safety” — **False**: safety is compile-time; runtime casts enforce what the compiler proved (unless you lie with unchecked casts/raw types).

“Why can I not overload `void f(List<String>)` and `void f(List<Integer>)`?” — Same erasure → same JVM signature.

## Principal-Level Discussion

Erasure explains most “Java generics feel weaker than language X” complaints. As a Principal, you:

1. Design APIs that don’t need runtime `T` — or require tokens.  
2. Ban raw types / suppressions without justification.  
3. Know heap pollution paths (`ClassCastException` far from the bug).  
4. Don’t promise reified behavior in library docs.

New language features (patterns, etc.) don’t remove erasure for `List<String>`.

### Related

[limitations.md](./limitations.md) · [generic-inheritance.md](./generic-inheritance.md) · [type-parameters.md](./type-parameters.md)
