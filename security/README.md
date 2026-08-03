# Security — Java / Application Guide (Defensive)

Security is a **system property**: identity, access, confidentiality, integrity, and safe code — not a single library call.

**Scope:** defensive design for Java backends. **Out of scope:** exploit recipes, attack tooling, or bypass instructions.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Identity: [authentication](./authentication.md) · [authorization](./authorization.md)  
2. Crypto primitives: [hashing](./hashing.md) · [encryption](./encryption.md) · [symmetric-encryption](./symmetric-encryption.md) · [asymmetric-encryption](./asymmetric-encryption.md) · [digital-signatures](./digital-signatures.md) · [secure-random](./secure-random.md)  
3. Transport & trust: [tls](./tls.md) · [keystore](./keystore.md) · [truststore](./truststore.md)  
4. Ops: [secrets](./secrets-management.md) · [secure-coding](./secure-coding.md)  
5. Practice: [scenarios](./scenarios.md) · [security-failures](./security-failures.md) · [trade-offs](./trade-offs.md) · [principal-decisions](./principal-decisions.md) · [interview](./interview.md)

## One-line PE rule

**Prefer proven frameworks and protocols; keep secrets out of code; fail closed; assume partial compromise when designing blast radius.**
