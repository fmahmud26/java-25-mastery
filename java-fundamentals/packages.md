# Packages

Namespaces that organize types and interact with access control.

## 1. Mental Model

```text
com.acme.billing.api.InvoiceDto
└── folder: com/acme/billing/api/InvoiceDto.java
```

## 2. Simple Explanation

A package is a namespace. Directory layout must match the package name. Package-private types are the default encapsulation boundary before `public`/`module exports`.

## 3. Technical Explanation

- `package` declaration must match path under source root.  
- Imports: single-type, `*`, `static`.  
- Unrelated to Maven `groupId` except by convention.  
- JPMS exports **packages**, not individual classes.

## 4. Internal Behavior

FQCN = package + simple name. Class loaders resolve by binary name. Package-private visibility is same-package only (not “same module” alone).

## 5. Java 25 Example

```java
package com.acme.billing.api;

public final class InvoiceDto {
    private final String invoiceId;
    public InvoiceDto(String invoiceId) { this.invoiceId = invoiceId; }
    public String invoiceId() { return invoiceId; }
}
```

## 6. Real-World Scenario

**Billing service:** `api` for DTOs/ports, `domain` for rules, `infra` for JDBC/Kafka. Controllers never import `infra` internals — keeps swap of DB driver local.

## 7. Common Mistake

Putting everything in the default package, or `com.acme.*` dumping grounds that kill encapsulation.

## 8. Failure Scenario

`package does not match directory` / wrong FQCN at launch. Fix layout or package statement; verify with `javap`/`jar tf`.

## 9. Performance Implications

None meaningful at runtime. Naming affects maintainability and module design.

## 10. Interview Questions

- Why packages?  
- How does package-private work?

## 11. Senior-Level Follow-ups

- How do you structure packages in a hexagonal service?  
- Package vs module boundary?

## 12. Principal Engineer Perspective

Packages are **product architecture in naming**. Prefer `api` / `domain` / `infra` over technical layers that invite spaghetti imports.

### Related

[access-modifiers.md](./access-modifiers.md) · [modules.md](./modules.md) · [classpath.md](./classpath.md)
