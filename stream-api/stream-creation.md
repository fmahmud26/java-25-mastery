# Stream Creation

How pipelines start — collections, values, generators, I/O, primitives.

## What Happens

```java
orders.stream();
orders.parallelStream();
Stream.of(a, b, c);
Stream.empty();
List.of(...).stream();
Arrays.stream(array);
Stream.iterate(0, n -> n + 1);
Stream.generate(rng::nextInt);
IntStream.range(0, n);
Files.lines(path);           // must close
BufferedReader.lines();
```

## Why Useful

Uniform processing model regardless of source — but resource-backed streams need lifecycle care.

## Production Example

```java
// Reporting from in-memory page
return page.content().stream().map(OrderDto::from).toList();

try (Stream<String> lines = Files.lines(path)) {
    return lines.filter(l -> l.startsWith("ERROR")).count();
}
```

## Performance Implications

`Files.lines` is lazy — good. `iterate`/`generate` without `limit`/short-circuit → infinite. Boxing from `Stream.of(1,2,3)` gives `Stream<Integer>`.

## Common Mistake

Forgetting try-with-resources on `Files.lines`; reusing streams; `parallelStream` by default.

### Related

[stream-lifecycle.md](./stream-lifecycle.md) · [lazy-evaluation.md](./lazy-evaluation.md)
