# Hashing

## Problem

Need integrity fingerprints or irreversible password verifiers — **not** reversible secrecy (that’s encryption).

## Mental Model

```text
data → digest (one-way)
password → slow KDF hash (one-way + expensive)
```

## Integrity Hashing

```java
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] digest = md.digest(payload);
```

Use SHA-256/SHA-3 for checksums. Compare digests with constant-time methods when defending against timing leaks on secrets.

## Password Hashing

**Never** store raw passwords or unsalted fast hashes (MD5/SHA-1 of password).

Use slow memory-hard KDFs: **bcrypt / scrypt / Argon2** (or PBKDF2 with high iterations when required). Java 25 also offers a standard [KDF API](../16-java-25-features/features/jep-510-kdf-api.md) for HKDF-style derivation (different use case than password storage).

```java
// Prefer a maintained password encoder (e.g. framework BCrypt)
boolean ok = passwordEncoder.matches(raw, storedHash);
```

## HMAC (keyed hash)

```java
Mac mac = Mac.getInstance("HmacSHA256");
mac.init(secretKey);
byte[] tag = mac.doFinal(message);
```

Authenticity + integrity when parties share a key.

## Failures

Fast hash for passwords; missing salt; truncating hashes; rolling your own “encryption” with hash XOR.

## Trade-offs

Stronger password KDF ⇒ slower logins / more CPU — tune with threat model and rate limits.

### Related

[encryption.md](./encryption.md) · [secure-random.md](./secure-random.md) · [authentication.md](./authentication.md)
