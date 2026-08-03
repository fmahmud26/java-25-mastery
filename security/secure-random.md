# SecureRandom

## Problem

Generate unpredictable values for security: keys, tokens, IVs/nonces, session IDs.

## Mental Model

```text
java.util.Random / Math.random → NOT for secrets
SecureRandom → cryptographically strong (when used correctly)
```

```java
SecureRandom rng = new SecureRandom();
byte[] token = new byte[32];
rng.nextBytes(token);
```

## Production Scenario

CSRF tokens, API key generation, AES-GCM nonces (unique per key!), password reset tokens.

## Failures

- `new Random(System.currentTimeMillis())` for session IDs  
- Reusing nonces  
- Tiny token space (4-digit “secrets”)  

## Trade-offs

SecureRandom may block briefly on entropy-starved systems — still required for crypto; fix entropy sources in infra rather than switching to insecure RNG.

### Related

[symmetric-encryption.md](./symmetric-encryption.md) · [secrets-management.md](./secrets-management.md) · [secure-coding.md](./secure-coding.md)
