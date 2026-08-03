# Isolation Anomalies

What goes wrong when concurrency meets weak isolation — know the **names** and when they hurt payments/inventory.

| Anomaly | Idea |
|---------|------|
| **Dirty read** | Read another tx’s uncommitted data |
| **Non-repeatable read** | Re-read row, value changed by committed tx |
| **Phantom read** | Re-query range, new rows appear |
| **Lost update** | Two read-modify-write overwrite each other |
| **Write skew** | Each tx checks a constraint; together they break it (classic) |

## JDBC Levels (intent)

Higher isolation reduces anomalies; exact guarantees are **DB-specific**. Postgres RC vs MySQL RR are not identical stories.

## Business Impact

| Domain | Dangerous anomaly |
|--------|-------------------|
| **Payment** | Double-charge / lost update on wallet balance |
| **Inventory** | Oversell (lost update / write skew) |
| **Order** | Duplicate fulfillment from racing status checks |

## Defenses Beyond Isolation

- `UPDATE … WHERE version = ?` optimistic locking  
- `UPDATE stock SET qty = qty - ? WHERE qty >= ?`  
- Unique constraints / idempotency keys  
- `SELECT … FOR UPDATE` when appropriate (keep short)  

### Related

[isolation-levels.md](./isolation-levels.md) · [scenarios.md](./scenarios.md)
