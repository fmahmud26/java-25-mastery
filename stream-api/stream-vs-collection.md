# Stream vs Collection

Streams are **not** a new collection type — they’re a view for computing over data once.

| | Collection | Stream |
|--|------------|--------|
| Storage | Holds elements | Doesn’t store (mostly) |
| Reuse | Yes | No — one terminal |
| Mutation API | add/remove | Functional transforms |
| Laziness | Eager structures | Intermediate lazy |

```java
List<Order> paid = orders.stream().filter(Order::paid).toList(); // back to collection
```

Use collections for sharing/reuse; streams for compute pipelines.

### Related

[stream-vs-loop.md](./stream-vs-loop.md) · [stream-lifecycle.md](./stream-lifecycle.md)
