# Composite

## Problem

Treat individual objects and compositions **uniformly** (file/folder trees, UI widget trees, org charts).

## Why naive approach fails

Clients branch `if (isLeaf)` vs `if (isFolder)` everywhere — adding node types breaks all clients.

## Pattern

Component interface; leaf and composite both implement it; composite stores children.

## Implementation

```java
public sealed interface FsNode permits FileNode, DirNode {
    String name();
    long size();
}

public record FileNode(String name, long size) implements FsNode { }

public final class DirNode implements FsNode {
    private final String name;
    private final List<FsNode> children = new ArrayList<>();
    public DirNode(String name) { this.name = name; }
    public void add(FsNode n) { children.add(n); }
    public String name() { return name; }
    public long size() {
        return children.stream().mapToLong(FsNode::size).sum();
    }
}
```

## Trade-offs

| Pros | Cons |
|------|------|
| Uniform tree ops | Overly general components |
| Recursive algorithms natural | Type safety of “leaf-only” ops weaker |
| Sealed hierarchies help exhaustiveness | Memory for many small nodes |

## Real-world usage

Swing/JavaFX scene graphs, file systems, expression ASTs, menu hierarchies.

Related: [decorator.md](./decorator.md), [bridge.md](./bridge.md), [../oop/sealed-classes.md](../oop/sealed-classes.md).
