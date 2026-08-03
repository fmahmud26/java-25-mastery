# Authentication

## Problem

Prove *who* is calling — establish a **principal** (user, service, device).

## Mental Model

```text
Claimed identity + authenticator
        ↓ verify
Authenticated subject in SecurityContext / token
```

## Mechanisms (Java backends)

| Mechanism | Production notes |
|-----------|------------------|
| Password | Store only [slow hashes](./hashing.md); rate-limit; MFA |
| Session cookie | HttpOnly, Secure, SameSite; server-side session store |
| Bearer / JWT | Validate sig, iss, aud, exp; short TTL; refresh carefully |
| OAuth2 / OIDC | Delegate to IdP; don’t invent SSO |
| mTLS | Service identity via client certs ([tls](./tls.md)) |
| API keys | Hashed at rest; scoped; rotatable |

```java
// conceptual — use Spring Security / similar
if (encoder.matches(rawPassword, user.passwordHash())) {
    // establish session or issue token — never log rawPassword
}
```

## Production Scenario

**Banking login:** password + MFA → session; step-up auth for wire transfers.

## Failures

- Credential stuffing without lockout/MFA  
- Tokens in logs/URLs  
- “Auth” = presence of a header without verification  
- Long-lived JWTs without revocation strategy  

## Trade-offs

Usability vs friction (MFA); stateful sessions vs stateless JWT (revocation harder).

## When NOT to DIY

Don’t write custom crypto login protocols — use maintained stacks.

### Related

[authorization.md](./authorization.md) · [secure-coding.md](./secure-coding.md) · [scenarios.md](./scenarios.md)
