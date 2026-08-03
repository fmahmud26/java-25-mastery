# Tool: AssertJ

```java
assertThat(order.lines()).hasSize(2)
    .extracting(Line::sku)
    .contains("SKU-1");
```

See [../assertions.md](../assertions.md).
