# Text Blocks

**Introduced:** preview 13–14 · **Final:** Java 15 · **Java 25:** standard

## Problem Before

Multi-line SQL/JSON needed ugly `\n` concatenation or external files for tiny snippets:

```java
String json = "{\n" +
    "  \"paymentId\": \"" + id + "\",\n" +
    "  \"cents\": " + cents + "\n" +
    "}";
```

## The Feature

`"""..."""` multi-line string literals with incidental indentation stripped based on closing delimiter position.

## How It Works

- Content from opening `"""` newline to closing `"""`  
- Indentation common to lines (relative to closing `"""`) removed  
- `\s` keeps trailing spaces; `\` can continue lines  
- Use `.formatted(...)` / concatenation for interpolation (no string templates as final everyday feature — don’t invent)

## Before → After

```java
String sql = """
        SELECT id, status, amount_cents
        FROM payments
        WHERE customer_id = ?
          AND status = 'AUTHORIZED'
        """;

String json = """
        {
          "paymentId": "%s",
          "cents": %d
        }
        """.formatted(id, cents);
```

## Production Usage

- SQL in DAOs/tests, JSON fixtures, HTML email snippets, multi-line error help  
- Prefer parameterized SQL still — text blocks don’t replace `PreparedStatement`  
- Keep secrets out of literals

## Trade-offs

| Pros | Cons |
|------|------|
| Readable diffs | Huge blocks in code vs dedicated files |
| Fewer escape errors | Indentation surprises if closing `"""` misaligned |

## When NOT to Use

- Enormous payloads → resources/files  
- Dynamic structure → JSON library (`ObjectMapper`), not string build  
- One-line strings — ordinary `"..."` is fine

## Migration Notes

Replace concatenated multi-line strings first in tests and SQL. Align closing `"""` with left content edge intentionally.

## Interview Questions

- How is incidental indentation determined?  
- Text block vs `.formatted` vs JSON library?  
- What is `\s` for?

### Related

[var.md](./var.md) · [modern-coding-style.md](./modern-coding-style.md)
