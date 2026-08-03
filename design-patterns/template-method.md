# Template Method

## Problem

A multi-step algorithm has a fixed skeleton (extract → transform → load) but individual steps vary by subtype.

## Forces

- Enforce step order  
- Share workflow without duplicating orchestration  
- Allow step customization  
- Inheritance coupling vs composition  

## Naive solution

Copy-paste the pipeline in each importer; drift in error handling/order.

## Pattern

Abstract class defines `final` template method calling overridable hooks/steps.

## Implementation

```java
public abstract class ReportExportJob {
    public final byte[] run(ReportRequest req) {
        var raw = load(req);
        var model = transform(raw);
        return render(model);
    }
    protected abstract RawData load(ReportRequest req);
    protected abstract ReportModel transform(RawData raw);
    protected abstract byte[] render(ReportModel model);
}

public final class PdfExportJob extends ReportExportJob {
    protected RawData load(ReportRequest req) { return repo.fetch(req); }
    protected ReportModel transform(RawData raw) { return mapper.toModel(raw); }
    protected byte[] render(ReportModel model) { return pdf.render(model); }
}
```

Prefer composition (pipeline of strategies) when inheritance gets awkward.

## Trade-offs

| + | − |
|---|---|
| Guaranteed structure | Inheritance fragility |
| DRY orchestration | Hard to reuse hooks elsewhere |
| Simple for stable pipelines | Testing abstract bases can be clumsy |

## When to use

ETL jobs, framework callbacks (`HttpServlet` historical), shared test fixtures.

## When NOT to use

Highly varying workflows — use Strategy/pipeline. Avoid if you only need one step customized among unrelated classes.

## Production example

**Finance:** nightly settlement job template — load ledger, compute, export — bank-specific subclasses differ in `load`/`export`.

## Interview question

*Template Method vs Strategy? Hollywood principle? When prefer pipelines over inheritance?*

**SOLID/LLD:** OCP via hooks; watch LSP for subclasses.

### Related

[strategy.md](./strategy.md) · [factory.md](./factory.md)
