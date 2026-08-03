# TLS handshake CPU storms

## Question

Traffic spike causes CPU pegged in TLS handshakes; keep-alives low. Staff actions?

## Difficulty

Staff

## Expected answer

Enable connection reuse/keep-alive/pools; session resumption; HTTP/2 where appropriate; terminate TLS at edge; size crypto; avoid reconnect storms from tiny pools.

## Reasoning

Handshakes are CPU expensive vs bulk encrypt.

## Follow-up

How do you verify session reuse rates?

## Common mistake

Only adding more pods without reuse.

## Principal-level discussion

Edge termination standards; client pool defaults in templates; capacity models include handshake rates.
