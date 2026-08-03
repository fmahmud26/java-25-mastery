# Security Failures (Defensive Lessons)

Describe **what goes wrong** and **how to design against it** — not how to attack.

| Failure | Design lesson |
|---------|----------------|
| Broken authentication | Use IdP/frameworks; MFA for privileged; harden sessions |
| Broken authorization / IDOR | Authorize on resource + action every path |
| Secrets in repo | Secret manager; pre-commit scanning; rotate on leak |
| Weak password storage | Slow KDF; unique salt; never reversible “encryption” of passwords |
| TLS validation disabled | Fix trust stores; never `TrustAll` in prod |
| Over-privileged DB/service accounts | Least privilege; separate creds per service |
| Verbose errors to clients | Hide internals; correlate with secure logs |
| Unsafe deserialization | Don’t deserialize untrusted Java blobs |
| Missing security headers / CSRF on cookie apps | Framework defaults + review |
| Dependency CVEs | SCA in CI; patch SLAs |

## Incident posture

Detect (auth anomalies, 403 spikes) → contain (revoke tokens/keys) → eradicate (patch) → recover → postmortem with design change.

### Related

[secure-coding.md](./secure-coding.md) · [scenarios.md](./scenarios.md) · [trade-offs.md](./trade-offs.md)
