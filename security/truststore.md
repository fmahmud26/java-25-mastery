# TrustStore

## Problem

Decide which **certificate authorities / certs** the JVM trusts when validating peers (HTTPS servers, mTLS clients).

## Mental Model

```text
TrustStore = “whose signatures do I accept?”
Default: cacerts (public CAs)
Enterprise: private CA certs added/replaced
```

```java
// Often configured via:
// -Djavax.net.ssl.trustStore=... -Djavax.net.ssl.trustStorePassword=...
// or custom SSLContext TrustManagerFactory
```

## Production Scenario

Internal APIs use corporate CA — containers must include corporate TrustStore or custom `SSLContext`, or handshake fails with PKIX path errors.

## Failures

- Disabling trust validation to “fix” PKIX errors  
- Outdated cacerts missing needed CAs  
- Confusing TrustStore (trust) with KeyStore (identity)  

## Trade-offs

Broad public trust vs private PKI (more control, more ops). Pinning (rare in general web) vs CA trust.

### Related

[keystore.md](./keystore.md) · [tls.md](./tls.md) · [security-failures.md](./security-failures.md)
