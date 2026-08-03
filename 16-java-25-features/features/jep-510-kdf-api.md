# JEP 510 — Key Derivation Function API

| | |
|--|--|
| **JEP** | [510](https://openjdk.org/jeps/510) |
| **Status** | **Final** (SE API) — JDK 25 |
| **History** | Preview JEP 478 (JDK 24) → **510 final** (no API change) |

## Purpose

Standard `javax.crypto` API for **Key Derivation Functions** (derive key material from secret + salt/info).

## Problem Solved

KDFs (HKDF, future Argon2, etc.) did not fit cleanly in `KeyGenerator` / `SecretKeyFactory`. Needed for KEM/HPKE/TLS 1.3-style protocols and PKCS#11 KDF support.

## Previous Approach

Ad-hoc/internal HKDF, overloading `SecretKeyFactory`/`KeyGenerator`, third-party crypto only.

## New Approach

`javax.crypto.KDF` + SPI; JDK includes HKDF with `HKDFParameterSpec`.

## Syntax / API

```java
KDF hkdf = KDF.getInstance("HKDF-SHA256");

AlgorithmParameterSpec params =
    HKDFParameterSpec.ofExtract()
                     .addIKM(initialKeyMaterial)
                     .addSalt(salt)
                     .thenExpand(info, 32);

SecretKey key = hkdf.deriveKey("AES", params);
// or deriveData(spec) for raw bytes
```

## Internal Behavior

Providers implement `KDFSpi`. HKDF extract/expand modes via parameter specs. PBKDF1/2 remain on `SecretKeyFactory` (non-goal to move them now).

## Production Example

Derive session keys in a hybrid KEM handshake; refactor TLS 1.3/DHKEM stacks to shared KDF API; PKCS#11-backed HKDF.

## Limitations

- Initial JDK algorithm focus: **HKDF** (Argon2 intended later per JEP future work).  
- Not a password-hashing migration guide by itself.  
- Correctness still depends on parameter choices (salt, info, lengths).

## Migration Considerations

New code should prefer `KDF` for HKDF. Leave existing PBKDF2 via `SecretKeyFactory` unless there is a reason to revisit. Pairs with KEM (JDK 21) toward HPKE/PQC readiness.

## Interview Questions

1. Why not KeyGenerator for KDFs?  
2. HKDF extract vs expand?  
3. Relation to KEM / post-quantum roadmap?  

### Related

[jep-470-pem-encodings.md](./jep-470-pem-encodings.md) · [../api-changes.md](../api-changes.md)
