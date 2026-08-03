# Design Trade-offs (Security)

Security choices are rarely free — make them explicit.

| Decision | Gain | Cost |
|----------|------|------|
| MFA everywhere | Account takeover resistance | UX friction; support |
| Short-lived JWTs | Breach window shrinks | Refresh complexity |
| Stateful sessions | Easy revoke | Sticky store / scale |
| mTLS mesh | Strong service identity | Cert ops |
| Field-level encryption | Breach mitigation | Search/reporting harder |
| Strict allowlists | Attack surface ↓ | Product velocity |
| High Argon2 params | Password crack resistance | CPU / login latency |
| Central policy engine | Consistent authz | Platform dependency |

## PE Practice

Record trade-offs in ADRs. “We’ll accept X residual risk because Y compensating control.”

### Related

[principal-decisions.md](./principal-decisions.md) · [authentication.md](./authentication.md) · [encryption.md](./encryption.md)
