# Preview Features in JDK 25

Require `--enable-preview`. May change or be removed. **Not** a stable production contract.

| JEP | Guide |
|-----|-------|
| 470 PEM Encodings of Cryptographic Objects | [features/jep-470-pem-encodings.md](./features/jep-470-pem-encodings.md) |
| 502 Stable Values | [features/jep-502-stable-values.md](./features/jep-502-stable-values.md) |
| 505 Structured Concurrency (Fifth Preview) | [features/jep-505-structured-concurrency.md](./features/jep-505-structured-concurrency.md) |
| 507 Primitive Types in Patterns, instanceof, switch (Third Preview) | [features/jep-507-primitive-patterns.md](./features/jep-507-primitive-patterns.md) |

```bash
javac --release 25 --enable-preview Main.java
java --enable-preview Main
```

### Related

[feature-status.md](./feature-status.md) · [experimental-features.md](./experimental-features.md)
