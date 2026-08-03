# Interview — JDBC

Walk **architecture → safety → transactions → pools → incidents**.

---

### Architecture?

App → JDBC API → Driver → DB. Prefer `DataSource` + pool. Connection → Statement → ResultSet; try-with-resources.

---

### SQL injection?

Concatenated input changes SQL. Defense: `PreparedStatement` binds. Identifiers need allowlists.

---

### PreparedStatement vs Statement?

Parameterized, safer, batchable, plan-friendly. Statement only for fixed SQL.

---

### Transactions?

`setAutoCommit(false)` → work → `commit` / `rollback`. Keep short; no remote I/O inside.

---

### Isolation?

RC → RR → Serializable trade concurrency vs anomalies. Dirty / non-repeatable / phantom / lost update. DB-specific.

---

### Pooling?

Reuse connections; `close` returns to pool. Size to DB. Exhaustion ≠ “need more threads.”

---

### Scenarios to narrate

Payment outbox; inventory conditional update; order header+lines batch; deadlock lock order; leak via missing try-with.

---

### PE one-liner

Bind parameters, close connections, short transactions, pools matched to the database.

### Related

[README.md](./README.md) · [principal-decisions.md](./principal-decisions.md) · [scenarios.md](./scenarios.md)
