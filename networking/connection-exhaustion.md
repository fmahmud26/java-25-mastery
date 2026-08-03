# Connection Exhaustion

## Problem

Too many concurrent TCP connections (or waits for pooled ones) — client or server runs out of capacity.

## Client Side

- New `HttpClient` per request → sockets explode  
- Pool max too high vs peer  
- Timeouts missing → connections stuck  

Symptoms: connect delays, `NoRouteToHost`, ephemeral port exhaustion, pool acquire timeouts.

## Server Side

- Accept queue full  
- Thread/connection limits  
- LB connection caps  

## Service-to-Service

Payment outage + retries + no pool cap → order service opens thousands of connections → collateral damage to other deps on same host.

## Fix

Shared pooled client; per-host caps; timeouts; bulkheads; shed load.

### Related

[connection-pooling.md](./connection-pooling.md) · [retry-storms.md](./retry-storms.md)
