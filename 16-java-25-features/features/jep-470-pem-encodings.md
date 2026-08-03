# JEP 470 — PEM Encodings of Cryptographic Objects (Preview)

| | |
|--|--|
| **JEP** | [470](https://openjdk.org/jeps/470) |
| **Status** | **Preview** — JDK 25 |

## Purpose

Standard preview API support for **PEM** (Privacy-Enhanced Mail) textual encodings of cryptographic objects (keys, certs, etc.).

## Problem Solved

PEM is ubiquitous in ops (TLS keys, CSRs), but Java historically needed third-party libs or ad-hoc Base64/`CERTIFICATE` parsing.

## Previous Approach

BouncyCastle / manual decode of `-----BEGIN ...-----` blocks / custom utilities.

## New Approach

Preview APIs to encode/decode PEM objects in the JDK security libraries (see [JEP 470](https://openjdk.org/jeps/470) for class names and supported object types in the preview).

```bash
javac --enable-preview --release 25 ...
java --enable-preview ...
```

## Syntax / API

Preview — consult JEP 470 + JDK 25 javadoc; do not hard-code invented type names in production.

## Internal Behavior

Parses PEM headers/footers and DER payloads into JDK crypto objects; encodes in reverse.

## Production Example

Experiment: load a PEM private key for a lab TLS tool. Production: wait for final or isolate behind an adapter.

## Limitations

- Preview.  
- Not all ecosystem PEM dialects/extensions may be covered initially.  
- Security-sensitive parsing — validate trust paths separately.

## Migration Considerations

Adapter pattern over preview API. Keep BouncyCastle until final if already committed.

## Interview Questions

1. What is PEM?  
2. Why preview in the JDK?  
3. Production stance on preview crypto APIs?

### Related

[jep-510-kdf-api.md](./jep-510-kdf-api.md) · [../preview-features.md](../preview-features.md)
