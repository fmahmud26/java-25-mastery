# JVM — Coding

Interview “coding” here = **explain + small experiments**, not LeetCode.

| Prompt | What they want |
|--------|----------------|
| Where is `new Object()`? | Heap (unless EA eliminates) |
| StackOverflowError vs OOM | Stack frames vs heap/metaspace/native |
| Why two classes same name clash? | Different loaders → different runtime types |
| Show TLAB / allocation | JFR Object Allocation sample |

```java
// Duplicate class identity — classic loader interview
void demo() throws Exception {
    var url = Path.of("plugin.jar").toUri().toURL();
    try (var a = new URLClassLoader(new URL[]{url}, null);
         var b = new URLClassLoader(new URL[]{url}, null)) {
        Class<?> c1 = Class.forName("com.demo.Plugin", true, a);
        Class<?> c2 = Class.forName("com.demo.Plugin", true, b);
        // c1 != c2 even though same binary name
        IO.println(c1 == c2); // false
    }
}
```

```java
// Recursion → StackOverflowError (not heap)
int boom(int n) { return boom(n + 1); }
```

**Talk track:** bytecode is portable; HotSpot makes it fast via profiling + JIT + GC. Memory bugs are area-specific (heap vs metaspace vs native vs stack).

Related chapter drills: [interview.md](../../jvm-internals/interview.md).
