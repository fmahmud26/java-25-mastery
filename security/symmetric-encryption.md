# Symmetric Encryption

## Problem

Fast confidentiality when sender and receiver share a secret key.

## Mental Model

```text
AES-GCM(key, nonce, plaintext, aad) → ciphertext + tag
```

Prefer **authenticated encryption** (AES-GCM) so ciphertext cannot be silently tampered with.

## Java Sketch (conceptual)

Use `Cipher.getInstance` with a modern transformation (e.g. AES/GCM/NoPadding), a unique nonce per encryption under a key, and `SecretKey` from a KMS or `KeyGenerator` — never invent protocols. Prefer libraries/KMS wrappers maintained by security teams.

## Production Scenario

**PII column encryption:** random DEK per row or envelope encryption; DEK encrypted by KMS master key.

## Failures

- Nonce reuse in GCM  
- CBC without HMAC (padding oracle class of issues)  
- Key in config repo  

## Trade-offs

Key distribution hard at scale → often combine with asymmetric wrap or KMS. Symmetric is fast for bulk data.

### Related

[asymmetric-encryption.md](./asymmetric-encryption.md) · [encryption.md](./encryption.md) · [secure-random.md](./secure-random.md)
