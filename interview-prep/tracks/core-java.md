# Track: Core Java Interviews

## What they test

Contracts (`equals`/`hashCode`), API boundaries, resource lifetime, type system judgment, Java version honesty — **applied to bugs and design**, not keyword lists.

## PCR-OTDR emphasis

- **Problem/Context:** production symptom or API misuse  
- **Reasoning:** language/runtime mechanism  
- **Decision:** safe default + when to break it  
- **Result:** test or invariant that proves the fix  

## Practice sources

- Bank: [../../java-interview-questions/core-java](../../java-interview-questions/core-java/) · [oop](../../java-interview-questions/oop/) · [generics](../../java-interview-questions/generics/) · [java-25](../../java-interview-questions/java-25/)  
- Depth: [../oop-solid](../oop-solid/) · [../collections](../collections/) · [../java-evolution](../java-evolution/)  
- Formats: [../formats/rapid-fire/core-java.md](../formats/rapid-fire/core-java.md) · [../formats/deep-dive/equals-hashcode.md](../formats/deep-dive/equals-hashcode.md)

## Sample prompt (structured)

“Users lose map entries after updating a key field.” Walk PCR-OTDR: mutable key / hashCode → immutable keys → regression test.

## Loop

1. Rapid-fire 10 (90s each)  
2. One Senior bank question full spine  
3. Re-answer without notes  
