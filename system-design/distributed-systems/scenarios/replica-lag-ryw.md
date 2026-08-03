# Scenario: Replica Lag Breaks Read-Your-Writes

## Production story

User updates address; redirect to profile page reads async replica 1.5s behind → old address; user submits again; intermittent duplicates/support load. Happens more under deploy load when lag spikes.

## What’s failing

Read scaling via async replicas without session consistency.

## Bad responses

- “Lag is usually small”  
- Cache user profile forever  
- Sync replicate everything globally (latency shock)

## Principal response

1. Measure replica lag; alert.  
2. After write: read primary for that session / use `read-after-write` sticky, or return version/`updated_at` and require fresher read.  
3. Causal token in cookie/header.  
4. Separate lag-sensitive paths (account settings) from eventually consistent paths (recommendations).  
5. If multi-region: don’t send user to another region mid-session without RYW story.

## Trade-offs

Primary load vs correct UX. Often tiny % of reads need primary.

## Interview probes

- How do you implement monotonic reads across replicas?  
- What’s acceptable lag for this page?  

Related: [../consistency.md](../consistency.md), [../replication.md](../replication.md), [../eventual-consistency.md](../eventual-consistency.md).
