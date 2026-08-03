# SQL Injection

## Problem

Attacker supplies input that changes SQL **structure**, not just values.

```text
name = x' OR '1'='1
→ WHERE name = 'x' OR '1'='1'   -- bypass
```

## Naive (broken)

```java
st.executeQuery("select * from users where name = '" + name + "'");
```

## Defense

```java
ps = conn.prepareStatement("select * from users where name = ?");
ps.setString(1, name);
```

| Rule | Detail |
|------|--------|
| Bind values | Always `PreparedStatement` / ORM params |
| Identifiers | Whitelist columns/tables — cannot `?` bind identifiers |
| Least privilege | DB user cannot drop tables casually |

## Production

Payment/order search endpoints are classic injection targets. Code review: ban Statement+concat; SAST; pentest.

## Debug

Enable DB logging of SQL text — if user strings appear **inside** SQL text, you’re concatenating.

### Related

[preparedstatement.md](./preparedstatement.md) · [statement.md](./statement.md)
