# Interview — Java / Application Security

Defensive framing only.

---

### Authentication vs authorization?

Authn = who. Authz = what they’re allowed to do.

---

### Password storage?

Slow KDF (bcrypt/scrypt/Argon2), unique salt, never plaintext or fast hash alone.

---

### Hash vs encrypt?

Hash one-way (integrity/passwords). Encrypt reversible with key (confidentiality).

---

### Symmetric vs asymmetric?

Same key vs key pair; hybrid for bulk data; TLS uses both roles in handshake.

---

### Signatures?

Integrity + authenticity with private key; verify with public.

---

### KeyStore vs TrustStore?

KeyStore: my keys/identity. TrustStore: which CAs/peers I trust.

---

### SecureRandom?

Required for tokens/keys/nonces — not `java.util.Random`.

---

### Secure coding staples?

Parameterized queries; authz on resources; no secrets in code; safe deserialization; least privilege.

---

### Trade-off prompt

“Why not encrypt every DB column?” → search, key management, performance; prefer TLS + selective field encryption + KMS.

**PE line:** frameworks, least privilege, secret hygiene, no DIY crypto.

### Related

[README.md](./README.md) · [security-failures.md](./security-failures.md) · [principal-decisions.md](./principal-decisions.md)
