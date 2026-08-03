# Log Processing

High-volume log/event ingest, buffer, process, and store for search/metrics/analytics.

## Requirements

**Functional:** collect logs from services, parse/enrich, route to sinks (search, metrics, lake), query recent logs, retain tiers.  
**Non-functional:** burst absorb; backpressure; low loss targets; multi-tenant fair use; cost control.  
**Non-goals:** full SIEM product day one; exactly-once analytics everywhere.

## Capacity estimation

```text
5k hosts × 2k lines/s × 200B ≈ 2GB/s ingest (~170 TB/day raw)
Peak ×3 → need buffer
Index only 7 days hot; rest cheap object storage
Cardinality explosion in labels kills metrics — budget series
```

## Architecture

```text
Apps → agent/sidecar → Kafka (or Pub/Sub) topics by service/env
                    → optional edge aggregator
Stream processors (Flink/Spark/consumer apps)
        ├─ parse/filter/PII redact
        ├─ write OpenSearch/Elastic hot
        ├─ write metrics remote_write
        └─ write Parquet to S3 lake
Query: Kibana / SQL on lake / Grafana
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Agents | Tail/buffer locally | Survive network blips |
| Kafka | Central buffer | Decouple producers/sinks |
| Parsers | Structured events | Queryability |
| Indexer | Recent search | Ops debugging |
| Lake writer | Cheap long retention | Compliance/analytics |
| Schema/registry | Evolve fields | Compatibility |

## Data flow

Emit structured JSON → agent batches → Kafka → consumers enrich → fan-out sinks.  
**Sampling** on debug level under pressure; never sample audit/security without policy.

## Data storage

| Tier | Store | Retention |
|------|-------|-----------|
| Buffer | Kafka | days |
| Hot search | OpenSearch | 3–14 days |
| Cold | S3 + Athena/Spark | months–years |
| Metrics | TSDB | per resolution rollup |

## Scaling

- Kafka partitions by `service` or `host` (watch hot services)  
- Autoscale indexers on lag  
- Isolate noisy tenants (quota per team)  
- Tiered storage; drop DEBUG in prod by default  

## Failure modes

| Failure | Mitigation |
|---------|------------|
| Kafka full | Agent local disk buffer; block or drop with metric (policy) |
| Indexer lag | Scale; shed DEBUG; prioritize ERROR |
| Mapping explosion | Strict schema; reject bad fields |
| PII leak into index | Redaction pipeline; access control |
| Exactly-once sink | Idempotent doc ids; lake upsert partitions |

## Observability

- Ingest bytes/QPS, Kafka lag by topic, drop counts, index refresh latency  
- **Meta:** logging pipeline must not depend on itself silently — use separate pager channel  

## Security

- TLS agents; auth to Kafka  
- Redact secrets/PII before index  
- RBAC on indices; immutable audit trail for admin  
- Separate security logs with stricter controls  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Kafka center | Buffer + replay | Direct app→ES — outages lose/amplify |
| Hot+cold tiers | Cost | Index everything forever |
| At-least-once + doc ids | Practical | Global exactly-once — expensive |
| Structured logs required | Query | Free-text only — useless at scale |

## Evolution

1. Agents → managed ingest → ES  
2. Kafka + parsers + S3  
3. Multi-tenant quotas; SLOs per pipeline  
4. Stream anomaly detection; compliance holds  

Related: [message-queues](../distributed-systems/message-queues.md), [throughput](../fundamentals/throughput.md), [observability](../fundamentals/observability.md), [disaster-recovery](../fundamentals/disaster-recovery.md).
