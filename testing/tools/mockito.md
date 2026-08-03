# Tool: Mockito

```java
when(port.charge(any())).thenReturn(ok);
verify(port).charge(any());
```

Prefer mocking **interfaces you own** at boundaries. Strict stubbing helps catch dead stubs.

See [../mocking.md](../mocking.md) · [../what-not-to-mock.md](../what-not-to-mock.md).
