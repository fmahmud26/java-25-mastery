# max

```java
Optional<Customer> top = customers.stream()
        .max(Comparator.comparingLong(Customer::lifetimeValueCents));
```

### Related

[min.md](./min.md) · [grouping-by.md](./grouping-by.md)
