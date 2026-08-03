# Bridge

## Problem

Vary an **abstraction** and its **implementation** independently (remote control vs device; shape vs drawing API) without subclass explosion.

## Why naive approach fails

`BlueCircle`, `RedCircle`, `BlueSquare`, … — every combination is a class.

## Pattern

Split into abstraction + implementor hierarchy; abstraction holds a reference to implementor (composition).

## Implementation

```java
public interface Renderer { void drawCircle(double x, double y, double r); }

public final class VectorRenderer implements Renderer {
    public void drawCircle(double x, double y, double r) { /* vectors */ }
}

public abstract class Shape {
    protected final Renderer renderer;
    protected Shape(Renderer renderer) { this.renderer = renderer; }
    public abstract void draw();
}

public final class Circle extends Shape {
    private final double x, y, r;
    public Circle(Renderer renderer, double x, double y, double r) {
        super(renderer); this.x = x; this.y = y; this.r = r;
    }
    public void draw() { renderer.drawCircle(x, y, r); }
}
```

## Trade-offs

| Pros | Cons |
|------|------|
| Independent evolution | More indirection upfront |
| Avoids cartesian subclasses | Easy to confuse with Adapter |
| Platform portability | Two hierarchies to learn |

## Real-world usage

JDBC (`Driver` vs `Connection` usage), UI toolkits, persistence “driver” backends.

Related: [adapter.md](./adapter.md), [strategy.md](./strategy.md).
