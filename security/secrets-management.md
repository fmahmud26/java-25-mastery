# Secrets Management

## Problem

Protect credentials and keys: DB passwords, API keys, signing keys, keystore passwords.

## Mental Model

```text
Build artifact → no secrets
Runtime → inject from secret manager / sealed volume / cloud IAM
Rotate → dual-run → revoke
```

## Practices

| Do | Don’t |
|----|-------|
| Secret manager / vault / cloud secret store | Secrets in Git, images, tickets |
| Short-lived credentials (IAM roles) | Long-lived static keys in config maps plain |
| Rotate on schedule + on incident | Share one key across all envs |
| Audit access | Log secret values |

## Production Scenario

Spring Boot reads `spring.datasource.password` from vault agent sidecar; payment PSP key rotated quarterly; old key accepted briefly then disabled.

## Failures

- Secrets in exception messages  
- World-readable mounted files  
- Copy-paste into CI logs  

## Trade-offs

Developer convenience vs blast radius; env vars (simple, easy to leak via `/proc`) vs dedicated secret APIs.

### Related

[keystore.md](./keystore.md) · [secure-coding.md](./secure-coding.md) · [principal-decisions.md](./principal-decisions.md)
