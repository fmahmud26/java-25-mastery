# Configuration

## Purpose

Separate **code** from **environment** — same artifact, different behavior via config.

## Before

```java
String url = "jdbc:postgresql://prod-db/orders"; // hardcoded
int timeout = 30_000;
```

## After

```yaml
# application.yaml + env overlays
datasource:
  url: ${DB_URL}
payment:
  base-url: ${PAYMENT_URL}
  connect-timeout: 2s
```

```java
@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(URI baseUrl, Duration connectTimeout) {}
```

## Practices

- Fail fast on missing required config at startup  
- Secrets from secret manager, not Git ([security mindset](./configuration.md))  
- Typed config over stringly `Map`  
- Document defaults and units  

## Trade-offs

Too many flags → combinatorial explosion. Feature flags need ownership and expiry.

## PE Decision

Config review for anything affecting money/timeouts/pool sizes; no silent defaults for security-critical settings.

### Related

[observability.md](./observability.md) · [maintainability.md](./maintainability.md)
