# Java Evolution — Theory

Think in **eras**, not a feature laundry list. Java 25 is an LTS after 21.

## Era map

| Era | Headline |
|-----|----------|
| **8** | Lambdas, Streams, `Optional`, `java.time`, default methods |
| **9–11** | JPMS, `var`, standard HTTP Client; LTS **11** |
| **12–17** | Switch expressions, text blocks, `instanceof` patterns, **records**, **sealed**; LTS **17** |
| **18–21** | Pattern `switch`, record patterns, **virtual threads**, sequenced collections; LTS **21** |
| **22–25** | Language polish, Loom maturity, API/JVM refinements; LTS **25** |

## Mental model shifts

| Old default | Modern default (Java 25) |
|-------------|---------------------------|
| Anonymous classes | Lambdas / method refs |
| Mutable POJOs everywhere | Records for data; immutability bias |
| `instanceof` + cast | Pattern matching |
| Long `switch` statements | Switch expressions + patterns |
| Thread pools for blocking I/O scale | Virtual threads per task |
| Classpath-only thinking | Modules awareness (even if classpath app) |

## Interview framing

- Name **LTS ladder**: 8 → 11 → 17 → 21 → 25.  
- Tie features to **problems solved** (boilerplate, concurrency scale, safer modeling).  
- Know preview vs finalized for anything you claim in production.

Related: [lts-evolution.md](../../16-java-25-features/lts-evolution.md), [interview.md](../../modern-java/interview.md).
