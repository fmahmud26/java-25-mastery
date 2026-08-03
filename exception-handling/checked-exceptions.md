# Checked Exceptions

`Exception` types that are **not** `RuntimeException`. Compiler requires `catch` or `throws`.

## Mental Model

```text
Checked = “caller must decide: handle or declare”
Common in JDK I/O — IOException, SQLException, InterruptedException
```

## Why They Exist / Why They’re Contested

Force acknowledgment of recoverable environmental failures. In large apps they create **signature noise** and encourage either `throws Exception` or premature catch — hence many teams wrap to unchecked at boundaries.

## Production Scenarios

| Failure | Checked type | Strategy |
|---------|--------------|----------|
| File failure | `IOException` | TWR; translate at app boundary |
| Database | `SQLException` | Catch in persistence adapter → DataAccessException |
| Network API (some libs) | IO-derived | Translate + retry policy |
| Interrupt | `InterruptedException` | Restore interrupt flag; don’t swallow |

## Bad vs Improved

```java
// Bad — leaks JDBC into domain
public Order load(String id) throws SQLException { ... }

// Improved — adapter translates
public Order load(String id) {
    try {
        return jdbc.query(...);
    } catch (SQLException e) {
        throw new OrderAccessException(id, e);
    }
}
```

## Strategy Notes

- Handle or translate **near the resource** (file/DB/client).  
- Don’t declare `throws Exception` on public APIs.  
- `InterruptedException`: re-set interrupt `Thread.currentThread().interrupt()`.

## Principal Discussion

Checked exceptions shine at **library edges** that truly require a decision. Across microservice domain layers, prefer unchecked domain exceptions + clear failure metrics so signatures stay stable.

### Related

[unchecked-exceptions.md](./unchecked-exceptions.md) · [exception-translation.md](./exception-translation.md) · [throws.md](./throws.md)
