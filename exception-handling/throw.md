# throw

Explicitly abort the current path with a `Throwable`.

## Mental Model

```text
precondition failed → throw
unexpected infra → throw translated
expected business outcome → often return Result, not throw
```

## Production Examples

```java
// validation
if (cmd.cents() <= 0) {
    throw new IllegalArgumentException("cents must be positive");
}

// domain
throw new OrderNotFoundException(orderId);

// wrap with cause — never lose root
throw new PaymentGatewayException(paymentId, cause);
```

## Bad vs Improved

```java
// Bad
throw new RuntimeException("error");

// Improved — type + context + cause
throw new ThirdPartyApiException("billing-tax", status, body, cause);
```

## Strategy

Throw at the **lowest honest layer**; translate upward. Include ids (paymentId, orderId) in message/fields for support.

### Related

[throws.md](./throws.md) · [custom-exceptions.md](./custom-exceptions.md) · [exception-propagation.md](./exception-propagation.md)
