# Debugging JDBC Issues

## Toolkit

| Tool | Use |
|------|-----|
| Pool metrics / JMX | Active, idle, pending, timeouts |
| Thread dump | Blocked on `getConnection` or locks |
| GC/app logs | Correlation only |
| DB `pg_stat_activity` / InnoDB status | Lock waits, deadlocks |
| Driver / statement logging | SQL + binds (careful: PII) |
| Exception chain | `SQLException.getNextException()` / batch |

## Playbooks

### Pool hang

1. Thread dump → wait on HikariPool  
2. Active connections ≈ max?  
3. Leak detection / who holds connections  
4. Slow query log  

### Wrong money / stock

1. Trace transaction boundaries in code  
2. Confirm auto-commit  
3. Check conditional update rowcounts  
4. Reproduce under concurrent test  

### Injection suspicion

1. Find Statement + string concat  
2. Log SQL text for payload characters  

### Deadlock

1. Capture DB deadlock report  
2. Document lock order in both code paths  
3. Add idempotent retry  

### Related

[connection-leaks.md](./connection-leaks.md) · [deadlocks.md](./deadlocks.md) · [principal-decisions.md](./principal-decisions.md)
