# Track: Concurrency Interviews

## What they test

Visibility/ordering (JMM), races, pools, VT realism, retries/idempotency — usually via **incidents**.

## PCR-OTDR emphasis

- Options must include “wrong sync” and “right tool”  
- Result = stress test / metrics (pool wait, duplicate side effects)  

## Practice sources

- Bank: [../../java-interview-questions/concurrency](../../java-interview-questions/concurrency/) · [virtual-threads](../../java-interview-questions/virtual-threads/)  
- Depth: [../concurrency](../concurrency/) · [../virtual-threads](../virtual-threads/) · [../completable-future](../completable-future/)  
- Experiments: [../../experiments/virtual-vs-platform-blocking](../../experiments/virtual-vs-platform-blocking/)  
- Formats: [../formats/debugging/pool-exhaustion.md](../formats/debugging/pool-exhaustion.md) · [../formats/scenarios/double-charge-race.md](../formats/scenarios/double-charge-race.md)

## Version honesty

On Java 25, don’t recite outdated `synchronized`-pins-VT lore without checking current JEPs ([bank Principal Q](../../java-interview-questions/concurrency/q07-pinning-synchronized.md)).

## Loop

Debug mock → name happens-before → propose fix → define Result metric.
