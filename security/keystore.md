# KeyStore

## Problem

Safely store **private keys** and certificates the JVM/process uses for identity (server TLS, client mTLS, code signing).

## Mental Model

```text
KeyStore = protected database of keys + cert chains
  (PKCS12 common; JKS legacy)
```

```java
KeyStore ks = KeyStore.getInstance("PKCS12");
try (InputStream in = Files.newInputStream(path)) {
    ks.load(in, password);
}
// PrivateKeyEntry for client/server identity
```

## Production Scenario

Payment service loads PKCS12 for mTLS to bank gateway from a mounted secret volume (password from secret manager — not in Git).

## Failures

- Keystore in Git  
- Same password everywhere  
- Using TrustStore when you meant KeyStore (or vice versa)  

## Trade-offs

File keystore vs HSM/KMS-backed keys (stronger, more ops). Rotation needs zero-downtime dual-cert strategy.

### Related

[truststore.md](./truststore.md) · [tls.md](./tls.md) · [secrets-management.md](./secrets-management.md)
