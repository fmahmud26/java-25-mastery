# Multi-catch

`catch (A | B e)` when handling is identical — `e` is effectively final.

```java
} catch (NoSuchFileException | AccessDeniedException e) {
    metrics.fileFail();
    throw new FileFailureException(path, e);
}
```

Types must not be subtypes of each other in the same multi-catch.

### Related

[catch.md](./catch.md)
