# Encryption

## Problem

Keep data **confidential** so unauthorized parties cannot read it — reversible with the correct key.

## Mental Model

```text
plaintext + key → ciphertext
ciphertext + key → plaintext
```

Hashing ≠ encryption. Encoding (Base64) ≠ encryption.

## Families

| Kind | Doc |
|------|-----|
| Symmetric (same key) | [symmetric-encryption.md](./symmetric-encryption.md) |
| Asymmetric (key pair) | [asymmetric-encryption.md](./asymmetric-encryption.md) |

Typical design: **TLS** for data in motion; application-level encryption for sensitive fields at rest; KMS/HSM for master keys.

## Production Scenario

Encrypt PAN / national IDs at rest with DEK (data key) wrapped by KEK in KMS; app never logs plaintext.

## Failures

- ECB mode patterns  
- Reused IV/nonce with the same key  
- Hard-coded keys in source  
- “Encrypt” with Base64  
- Home-grown algorithms  

## Trade-offs

App-level encryption complicates search/indexing; field-level vs disk/volume encryption — defense in depth layers.

### Related

[symmetric-encryption.md](./symmetric-encryption.md) · [asymmetric-encryption.md](./asymmetric-encryption.md) · [secrets-management.md](./secrets-management.md)
