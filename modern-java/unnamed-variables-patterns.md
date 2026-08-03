# Unnamed Variables & Patterns (`_`)

**Final:** Java 22 (JEP 456) · **Java 25:** standard

## Problem Before

Unused bindings forced fake names: `ignored`, `unused`, `e` in empty catches — noisy and misleading.

```java
catch (IOException ignored) {
    log.warn("skip");
}
case Point(int x, int y) -> useOnly(y); // x unused — warning
```

## The Feature

`_` declares “intentionally unused” for locals, catch, try-with-resources, lambda params, and pattern bindings. Can appear multiple times.

## How It Works

`_` does not introduce a named variable you can read. Keeps pattern structure without bindings.

## Before → After

```java
// After
catch (IOException _) {
    log.warn("skip");
}

return switch (shape) {
    case Point(_, int y) -> "y=" + y;
    case Rect(_, _) -> "rect";
};

map.forEach((_, v) -> process(v));
```

## Production Usage

- Nested record patterns where only some components matter  
- Side-effect pops from queues  
- Empty catch only when truly intentional (still prefer handling)

## Trade-offs

Documents intent; overuse in catch can hide errors — review empty catches still.

## When NOT to Use

- When the value should be logged/metric’d — name it  
- As an excuse to ignore checked failures silently in critical paths

## Migration Notes

Replace `ignored` names; enable in pattern-heavy modules first.

## Interview Questions

- Can you read `_` after declaring it?  
- Why allow multiple `_`?  
- Difference between unused variable warning and `_`?

### Related

[record-patterns.md](./record-patterns.md) · [var.md](./var.md) · [pattern-matching.md](./pattern-matching.md)
