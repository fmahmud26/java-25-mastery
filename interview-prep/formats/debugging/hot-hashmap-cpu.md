# Debug: Hot HashMap CPU

## Symptoms

Flamegraph: `HashMap.get` / hashCode; one endpoint; custom key type.

## Your move

Bad hash / mutable key / resize Options → Decision → Result (microbench/experiment + prod CPU).
