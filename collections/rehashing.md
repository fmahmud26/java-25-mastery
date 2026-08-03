# Rehashing / Resize

Growing the table and **rebucketizing** entries.

## How It Works (HashMap)

Capacity doubles (power of two). Each node moves to new index (bit of hash decides often stay vs move by oldCap). Expensive: allocate large array + touch all entries.

## Production Failure

Bulk load without capacity hint → repeated resizes → latency spikes + GC. Fix: size hint; bulk load off request path; monitor map size.

## Concurrency Note

HashMap resize during concurrent put is unsafe. CHM has concurrent resize mechanics — still costly under huge growth.

## Interview

- When does HashMap resize?  
- Cost of resize at millions of entries?  
- **Principal:** resize storms under traffic — mitigation?

### Related

[load-factor.md](./load-factor.md) · [hashmap.md](./hashmap.md) · [concurrenthashmap.md](./concurrenthashmap.md)
