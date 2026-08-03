# Factory (Factory Method / parameterized factory)

## Problem

Call sites need products (`Report`, `Notifier`) but should not hard-code every concrete class and construction rule.

## Forces

- New product types will appear (OCP)  
- Construction logic may need config/env  
- Callers want a stable abstraction (DIP)  
- Avoid a god `switch` copied everywhere  

## Naive solution

```java
if (type.equals("pdf")) return new PdfReport(logo);
else if (type.equals("csv")) return new CsvReport(delimiter);
// duplicated in 6 services
```

Adding Excel means editing every switch; tests couple to concretes.

## Pattern

Centralize creation behind a factory method/interface so callers depend on `Report`, not `PdfReport`.

## Implementation

```java
public sealed interface Report permits PdfReport, CsvReport {
    byte[] render(OrderReportData data);
}

public interface ReportFactory {
    Report create(ReportFormat format);
}

public final class DefaultReportFactory implements ReportFactory {
    @Override
    public Report create(ReportFormat format) {
        return switch (format) {
            case PDF -> new PdfReport(branding);
            case CSV -> new CsvReport(',');
        };
    }
}

// caller
Report report = factory.create(format);
```

Factory *method* variant: subclasses override `create()` for framework hooks.

## Trade-offs

| + | − |
|---|---|
| One place for construction | Factory can become a mega-switch |
| Easier testing with fake factory | Extra type for simple cases |
| Aligns with DIP | Name collision with “Abstract Factory” |

## When to use

Multiple product types; creation rules non-trivial; callers must stay stable.

## When NOT to use

Single concrete forever; `new` is clear and stable — YAGNI.

## Production example

**Commerce reporting:** export order summaries as PDF/CSV/XLSX chosen by merchant settings via `ReportFactory`.

## Interview question

*How does a Factory help Open/Closed? When is a simple `switch` inside one factory better than a class hierarchy of factories?*

**SOLID/LLD:** DIP + OCP; LLD “create notifier by channel.”

### Related

[abstract-factory.md](./abstract-factory.md) · [strategy.md](./strategy.md)
