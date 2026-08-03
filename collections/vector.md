# Vector (Legacy)

Synchronized resizable array `List`. **Prefer ArrayList + explicit concurrency** (or COW/other) in new Java 25 code.

## Why avoid

Coarse `synchronized` on every op; legacy API (`elements()`). Same growth idea as ArrayList but older defaults.

## Interview

- Vector vs ArrayList?  
- Why was Vector superseded?

### Related

[arraylist.md](./arraylist.md) · [copyonwritearraylist.md](./copyonwritearraylist.md)
