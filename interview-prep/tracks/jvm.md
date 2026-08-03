# Track: JVM Interviews

## What they test

Heap/stack/Metaspace, classloading, JIT warmup/deopt, tooling (`jcmd`, JFR) — usually “why is prod weird?”

## PCR-OTDR emphasis

- Reasoning = runtime mechanism  
- Options = code fix vs flag vs restart policy  
- Result = JFR/dump evidence  

## Practice sources

- Bank: [../../java-interview-questions/jvm](../../java-interview-questions/jvm/) · [memory](../../java-interview-questions/memory/)  
- Depth: [../jvm](../jvm/) · [../../jvm-internals](../../jvm-internals/)  
- Formats: [../formats/debugging/metaspace-climb.md](../formats/debugging/metaspace-climb.md) · [../formats/deep-dive/jit-warmup.md](../formats/deep-dive/jit-warmup.md)

## Loop

Pick a symptom → list 3 hypotheses → say which tool falsifies each → Decision.
