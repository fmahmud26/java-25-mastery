# Principal Engineer Decisions (Security)

## 1) Identity is a platform concern

Prefer org SSO/OIDC over per-app password DBs when possible. Apps consume identity; don’t reinvent.

## 2) Default deny authorization

Every new endpoint declares required permissions; tests for IDOR on `{id}` resources.

## 3) Secrets never in Git

CI scanners gate merges; runtime injection only; rotation drills yearly.

## 4) Crypto is borrowed, not invented

TLS everywhere; KMS for keys; vetted libraries for passwords and tokens. No custom ciphers.

## 5) Blast radius

Separate credentials per service/env; least privilege DB roles; segment admin planes.

## 6) Secure SDLC

Threat model for money/PII flows; dependency SCA; periodic pentest — fix classes of bugs, not only instances.

## Anti-decisions

- `TrustAllCertificates` “temporarily”  
- JWT in localStorage without XSS story  
- Logging Authorization headers  
- Same encryption key for all tenants forever  

### Related

[trade-offs.md](./trade-offs.md) · [secrets-management.md](./secrets-management.md) · [interview.md](./interview.md)
