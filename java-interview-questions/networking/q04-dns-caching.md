# Stale DNS after failover

## Question

VIP failover completes but Java apps hit old IPs for minutes/hours. Cause?

## Difficulty

Senior

## Expected answer

JVM security DNS cache (`networkaddress.cache.ttl`) may cache forever for positive lookups if misconfigured (especially older defaults with security manager eras). Configure sensible TTL; also consider OS/`InetAddress` caching; prefer short TTLs carefully.

## Reasoning

App-level caching can outlive DNS TTL expectations.

## Follow-up

Negative caching TTL?

## Common mistake

Only flushing OS DNS.

## Principal-level discussion

Standard JVM DNS properties in base images; document failover runbooks including JVM cache; test failovers regularly.
