# Asymmetric Encryption

## Problem

Confidentiality (or key exchange) without sharing the same long-term secret — public encrypt / private decrypt (classic RSA mental model); modern systems often use **hybrid** encryption or KEMs.

## Mental Model

```text
Public key  → can encrypt / verify
Private key → can decrypt / sign
```

Bulk data: encrypt a random AES key with asymmetric (or KEM), encrypt payload with AES — **hybrid encryption**.

## Production Scenario

Partners upload files encrypted to your public key; only your HSM-held private key decrypts. Or TLS handshake uses asymmetric crypto to establish symmetric session keys.

## Failures

- Encrypting huge payloads directly with RSA  
- Weak key sizes / outdated padding schemes  
- Private key in app jar  

## Trade-offs

Slower than symmetric; key management and rotation operationally heavy; prefer TLS + KMS for most app data paths.

Java 21+ KEM API and Java 25 KDF support post-quantum / hybrid roadmaps at the platform level — use vetted APIs, don’t roll custom PQC.

### Related

[symmetric-encryption.md](./symmetric-encryption.md) · [digital-signatures.md](./digital-signatures.md) · [tls.md](./tls.md)
