# Method References

Bound method/constructor as a SAM — often clearer than `x -> x.foo()`.

## Mental Model

```text
String::length          unbound instance → Function<String,Integer>
user::email             bound instance   → Supplier<String>
Integer::parseInt       static           → Function<String,Integer>
ArrayList::new          constructor      → Supplier<List<…>>
```

## Imperative vs Functional

```java
List<String> names = new ArrayList<>();
for (User u : users) names.add(u.name());

List<String> names = users.stream().map(User::name).toList();
```

## Production Example

```java
payments.stream()
        .filter(Payment::captured)
        .map(Payment::id)
        .forEach(ledger::postId);          // bound instance

Map<String, User> byId = users.stream()
        .collect(Collectors.toMap(User::id, Function.identity()));

list.sort(Comparator.comparing(Order::createdAt).reversed());
```

## Four Kinds

| Kind | Form |
|------|------|
| Static | `Class::staticMethod` |
| Bound | `instance::method` |
| Unbound | `Class::instanceMethod` (receiver is first arg) |
| Ctor / array | `Class::new`, `Type[]::new` |

## When Better / Worse

| Better | Worse |
|--------|-------|
| Existing method already names intent | Need adapt/null-check/extra arg — use lambda |
| Chain of map/filter | Obscure overload resolution — cast/lambda |

## Performance & Readability

Often as fast as lambdas (same metafactory). Best readability win in the FP toolkit.

## Common Mistake

Wrong overload: `System.out::println` vs ambiguous methods — help compiler with target type.

## Interview / PE

- Four kinds of method refs?  
- Unbound vs bound?  
- **PE:** style guide — method ref required when identical to `x -> x.m()`?

### Related

[lambda-expressions.md](./lambda-expressions.md) · [function.md](./function.md)
