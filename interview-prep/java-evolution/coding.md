# Java Evolution — Coding

Refactor “Java 8 style” into modern idioms under interview pressure.

| Before (8-ish) | After (25) |
|----------------|------------|
| POJO + getters/setters | `record` when immutable data |
| `instanceof` + cast | Pattern `instanceof` / switch |
| Nested callbacks | VT + blocking clarity *or* CF pipeline |
| Utility “tuple” classes | Records / sealed results |
| Long switch fall-through | Switch expression arrows |

```java
// Modernize validation result
public sealed interface Parse permits Parse.Success, Parse.Failure {
    record Success(Order order) implements Parse {}
    record Failure(String reason) implements Parse {}
}

Money total(Parse p) {
    return switch (p) {
        case Parse.Success(var o) -> o.total();
        case Parse.Failure(var r) -> throw new IllegalArgumentException(r);
    };
}
```

**Talk track:** “Same behavior, less boilerplate, exhaustiveness, clearer data model.”

Practice chapters: [../../modern-java](../../modern-java/), [../../16-java-25-features/language-changes.md](../../16-java-25-features/language-changes.md).
