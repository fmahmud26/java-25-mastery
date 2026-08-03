# Digital Signatures

## Problem

Prove **integrity** and **authenticity** of a message/artifact — signer’s private key produces a signature; verifiers use the public key.

## Mental Model

```text
sign(private, message) → signature
verify(public, message, signature) → true/false
```

Different from encryption (though related math). JWT “signed tokens” use this idea (HMAC or asymmetric).

## Production Scenario

**Software/release signing;** webhook signatures from Stripe-like PSPs; document signing; JAR signing in enterprise supply chains.

## Failures

- Verifying without checking key identity / cert chain  
- Accepting `alg=none` style token footguns  
- Signing mutable ambiguous payloads (JSON key order) — sign canonical bytes  

## Trade-offs

Asymmetric signatures: easier distribute verify keys; HMAC: simpler but shared secret sprawl.

### Related

[asymmetric-encryption.md](./asymmetric-encryption.md) · [authentication.md](./authentication.md) · [hashing.md](./hashing.md)
