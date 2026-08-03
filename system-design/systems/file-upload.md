# File Upload

Large-object ingest with direct-to-object-store uploads, scanning, and processing.

## Requirements

**Functional:** request upload, upload bytes (multipart), complete, download/ACL, delete; optional virus scan & transcode.  
**Non-functional:** multi-GB files; resumable; don’t proxy all bytes through app; durable object storage; abuse limits.  
**Non-goals:** full Google Drive collaboration product.

## Capacity estimation

```text
1M uploads/day, avg 5MB → 5TB/day ingress (~480 Mb/s avg; peaks higher)
10% videos avg 100MB dominate bytes
Metadata rows 1M/day tiny vs objects
Download traffic often ≫ upload (CDN)
```

## Architecture

```text
Client → API: create upload session (authz, MIME, size limit)
      ← presigned URL(s) / multipart upload ids
Client → Object Store (S3) direct PUT parts
Client → API: complete upload
API → verify → metadata DB → Kafka: ObjectUploaded
Workers → AV scan → quarantine or mark READY → thumbnail/transcode
Download → CDN / signed GET URLs
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Upload API | Sessions, policy, complete | Control plane only |
| Object store | Bytes | Durability/cost |
| Metadata DB | Owner, key, state, hash | Source of truth for apps |
| Scan/transcode workers | Async heavy CPU | Keep API thin |
| CDN | Downloads | Latency/egress |

## Data flow

1. Authorize limits (type/size/quota)  
2. Issue presigned multipart uploads  
3. Client uploads parts; retries per part  
4. Complete: verify etag/size; write metadata INIT→STORED  
5. Async scan: FAIL→quarantine; PASS→READY  
6. Consumers process derivatives  

## Data storage

| Data | Store |
|------|-------|
| Objects | S3 (versioning optional) |
| Metadata | SQL `objectId`, `userId`, `state`, `checksum` |
| Upload sessions | Redis/SQL with TTL |
| Events | Kafka |

## Scaling

- Bytes never through app tier (horizontal control plane easy)  
- Workers scale on queue lag  
- Partition processing by `objectId`  
- Lifecycle policies to cold storage  

## Failure modes

| Failure | Mitigation |
|---------|------------|
| Client dies mid-upload | Session TTL; multipart abort lifecycle |
| Complete before all parts | Reject; idempotent complete |
| AV service down | Leave SCANNING; don’t mark READY |
| Malware detected | Quarantine bucket; block download |
| Presign leak | Short expiry; bound method/key; authz on complete |
| Hot download | CDN + cache headers |

## Observability

- Upload complete rate, part failure, scan lag/duration, quarantine rate  
- Object store error rates; egress  
- Alert: scan lag SLO for user-visible READY  

## Security

- Authz on create/complete/download  
- Content-type allowlist; max size; malware scan  
- Encryption at rest (SSE); optional client encryption  
- Signed URLs short-lived; virus found ⇒ no public ACL  
- Quota per user to stop abuse  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Presigned direct upload | Scale & cost | Stream all via app — bottleneck |
| Async AV | UX + isolation | Sync scan on complete — timeouts |
| States on metadata | Safe publishing | Trust S3 existence alone |

## Evolution

1. Single PUT presign + metadata  
2. Multipart + resume  
3. AV + virus quarantine + CDN  
4. Multi-region replication; customer-managed keys  

Related: [scalability](../fundamentals/scalability.md), [message-queues](../distributed-systems/message-queues.md), [security via reliability mindset](../fundamentals/reliability.md).
