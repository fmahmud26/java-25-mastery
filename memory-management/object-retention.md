# Object Retention

Why memory stays high: something still **reaches** the object from a GC root.

## Mental Model

```text
Retained size = memory freed if this object (and only its dominated subgraph) died
Path to GC root = the smoking gun in MAT
```

## Technical Mechanism — common retainers

| Retainer | Pattern |
|----------|---------|
| Static field | Global cache / registry |
| Long-lived thread | `ThreadLocal`, runnable capture |
| Session / user maps | HttpSession attributes |
| Listener lists | EventBus, JMS, Swing-style |
| Caches | Guava/Caffeine misconfigured or raw `HashMap` |
| Classloader | Static cache in library + webapp reload |
| Inner class | `$outer` this |
| Collections | Grow-only lists/queues |

```java
// retention: outer this
class Service {
    void start() {
        bus.register(new Listener() {
            void onEvent(Event e) { Service.this.handle(e); }
        });
    }
}
```

## Production Implications

Ask for every long-lived structure: **Who removes entries? What is max size? What is TTL? What metric shows occupancy?**

## Investigation

Heap dump → Dominator Tree → Path to GC Root. See [investigation.md](./investigation.md).

## Interview / PE

Retained vs shallow size? Give path-to-root for a classic listener leak.

### Related

[strong-references.md](./strong-references.md) · [memory-leaks.md](./memory-leaks.md) · [incidents.md](./incidents.md)
