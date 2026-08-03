# Track: System Design

## What they test

Clarify → capacity → architecture → deep dive → failures → evolution. Numbers before vendors.

## PCR-OTDR mapping

| Spine | SD move |
|-------|---------|
| Problem | Functional + SLO |
| Context | QPS, size, regions |
| Reasoning | Bottleneck guess |
| Options | Sync/async, SQL/NoSQL, cache |
| Trade-offs | Consistency/latency/cost |
| Decision | Drawn design |
| Result | Metrics, SLOs, DR |

## Practice sources

- [../../system-design](../../system-design/)  
- Depth: [../system-design](../system-design/)  
- Formats: [../formats/mock-interviews/system-design-50.md](../formats/mock-interviews/system-design-50.md) · [../formats/architecture/url-shortener.md](../formats/architecture/url-shortener.md)

## Loop

50-min mock → write capacity math → list top 3 failure modes you under-discussed.
