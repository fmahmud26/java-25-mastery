# JVM Architecture

Simple mental model → HotSpot shape → production implications.

## Mental Model

```text
Your .class files are not machine code.
The JVM is a process that:
  1) loads classes
  2) stores program state in well-defined memory areas
  3) executes bytecode (interpret + JIT)
  4) reclaims unreachable objects (GC)
  5) talks to native code when needed
```

```text
Java Application (.java → .class)
                ↓
              JVM (HotSpot)
 ┌──────────────┼──────────────────────────┐
 │              │                          │
Class Loader    Runtime Data Areas    Execution Engine
(Bootstrap /    (Heap, Stack,         (Interpreter +
 Platform /      Metaspace, PC,         JIT + GC)
 Application)    Native stacks)
                │
                Native Interface (JNI / FFM)
                ↓
              OS / Hardware
```

## Core Concept

The JVM specification defines *behavior*. **HotSpot** (OpenJDK) is the usual implementation: template interpreter, C1/C2 (or Graal) JIT, generational/region GCs, metaspace, safepoints.

| Subsystem | Role |
|-----------|------|
| **Class Loader** | Find bytes, define `Class`, link, initialize |
| **Runtime Data Areas** | Heap, per-thread stacks/PC/native stacks, metaspace |
| **Execution Engine** | Interpret / compile / run; coordinate GC |
| **Native Interface** | JNI; Java 25 **FFM** for safer foreign access |

## How It Works Internally (L1→L4)

1. **L1:** `java Main` starts HotSpot, loads main class, runs `main`.  
2. **L2:** First methods interpret; counters rise; JIT compiles hot methods to `nmethod`s.  
3. **L3:** Threads share the heap; each has a Java stack + PC; class metadata lives in metaspace.  
4. **L4:** Speculative optimizations + deopt; safepoints for GC/deopt/biased-lock cleanup (historical)/thread dumps.

## Production Implications

- Architecture knowledge drives **which tool** to use: heap OOM ≠ metaspace OOM ≠ high CPU from JIT ≠ safepoint stalls.  
- Container memory must cover **heap + metaspace + thread stacks + code cache + native** — `-Xmx` alone is not RSS.  
- Java 25: product feature **compact object headers** (`-XX:+UseCompactObjectHeaders`, JEP 519) — optional, not default until later JDKs.

## Failure Scenarios

| Symptom | Likely subsystem |
|---------|------------------|
| `OutOfMemoryError: Java heap space` | Heap / allocation |
| `OutOfMemoryError: Metaspace` | Class metadata / leaky loaders |
| `StackOverflowError` | Deep recursion / tiny `-Xss` |
| High CPU, little GC | Hot loops / lock contention / JIT compiling |
| Periodic latency spikes | Safepoints / GC pauses / deopt storms |

## Interview / PE

Draw the diagram from memory. Name what lives where. Explain why “the JVM is slow” is not a diagnosis.

### Related

[runtime-data-areas.md](./runtime-data-areas.md) · [jvm-execution.md](./jvm-execution.md) · [diagnostic-tools.md](./diagnostic-tools.md)
