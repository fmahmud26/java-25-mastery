# Secure Coding (Java)

## Problem

Prevent common application defects that become vulnerabilities — without needing offensive detail.

## High-Value Rules

| Area | Defensive practice |
|------|--------------------|
| Injection | Parameterized SQL/JDBC; never concatenate untrusted input into queries/commands |
| XSS (if HTML) | Encode output; frameworks’ escaping; CSP |
| AuthZ | Check resource ownership every time ([authorization](./authorization.md)) |
| Deserialization | Avoid Java native serialization of untrusted data; prefer JSON with allowlisting |
| SSRF | Validate/allowlist outbound URLs |
| Path traversal | Resolve and constrain paths under a root |
| Logging | No passwords, tokens, PANs, session IDs |
| Dependencies | SCA scanning; pin versions; remove unused |
| Error handling | Generic client errors; detailed logs internal only |
| File upload | Size/type limits; store outside web root |

## Production Scenario

Order search API uses `PreparedStatement`; admin export requires role + audit; stack traces never returned to browsers.

## Failures

See [security-failures.md](./security-failures.md).

## Trade-offs

Strict allowlists slow product iteration; defense-in-depth costs UX — document accepted risk.

### Related

[authentication.md](./authentication.md) · [sql injection concepts via parameterized access](./secure-coding.md) · [trade-offs.md](./trade-offs.md)
