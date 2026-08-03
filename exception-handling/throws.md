# throws

Declares checked exceptions a method may propagate. Unchecked need not be declared (but documenting them helps).

## Mental Model

```text
throws IOException = “I won’t handle this here — caller must”
```

## Bad vs Improved

```java
// Bad — viral checked pollution
public void checkout(Cart cart) throws SQLException, IOException, GeneralSecurityException

// Improved — translate inside; stable domain signature
public CheckoutResult checkout(Cart cart) {
    // adapters catch and wrap
}
```

## Production — InterruptedException

```java
public void takeJob(BlockingQueue<Job> q) throws InterruptedException {
    Job job = q.take();
    process(job);
}
// callers: catch interrupt → restore flag → exit worker loop
```

## Strategy

Declare the **narrowest** checked types. Prefer translation over long `throws` lists on public APIs. For libraries, checked can be intentional; for app services, usually wrap.

### Related

[checked-exceptions.md](./checked-exceptions.md) · [exception-propagation.md](./exception-propagation.md)
