# Load Balancing

Distribute traffic across healthy instances. State **L4 vs L7**, **algorithm**, **health checks**, **sticky sessions**.

## L4 vs L7

| | L4 (TCP/UDP) | L7 (HTTP) |
|--|--------------|-----------|
| Sees | IP/port | Path, headers, cookies |
| Perf | Very fast | More CPU |
| Use | Generic TCP, DB proxies | HTTP APIs, canary by path/header |

## Algorithms

| Algo | Why | Watch |
|------|-----|-------|
| Round robin | Simple | Uneven work if requests vary |
| Least connections | Better for long requests | Need accurate conn count |
| Power of two choices | Good tradeoff | |
| Consistent hash | Cache locality / sticky rooms | Remapping on node change |
| Weighted | Heterogeneous fleets | |

## Health and drain

- Shallow health (process up) vs deep (can reach DB) — deep checks can cascade fail  
- Connection draining on deploy  
- Outlier ejection (remove slow instances temporarily)  

## Global load balancing

DNS geo / anycast → regional LB → local. Failover policies interact with caching TTL of DNS (slow failback).

## Sticky sessions

Prefer externalizing session state. If stickiness required (WebSocket), consistent hash with care on node loss.

Related: [availability.md](./availability.md), [scalability.md](./scalability.md), [latency.md](./latency.md).
