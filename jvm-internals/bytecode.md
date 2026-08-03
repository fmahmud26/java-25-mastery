# Bytecode

`javac` emits **JVM bytecode** — a stack-based instruction set the interpreter and JIT consume.

## Mental Model

```text
Java source  →  javac  →  .class (constant pool + methods’ bytecode)
                              ↓
                    verify → interpret / JIT → CPU
```

You never ship machine code for portability; the JVM specializes at runtime.

## Technical Mechanism

```bash
javac Hello.java
javap -c -p -v Hello   # disassemble + constant pool
```

```text
0: iload_1
1: iload_2
2: iadd
3: istore_3
```

| Trait | Detail |
|-------|--------|
| Stack machine | Operand stack + locals per frame |
| Typed | Verification rejects unsafe code |
| Invoke kinds | `invokestatic`, `invokevirtual`, `invokeinterface`, `invokespecial`, `invokedynamic` |
| Tools | `javap`, ASM, Class-File API (Java 24+) |

## JVM Internals

- Constant pool holds symbolic refs; **resolution** binds them during linking / first use.  
- `invokedynamic` + bootstrap methods power lambdas and dynamic languages.  
- Verification ensures stack map frames / type safety (security boundary).  
- JIT does not “run bytecode forever” — it compiles hot regions to native, using bytecode + profile as input.

## Production Implications

- Weird bugs after bytecode weaving (agents, Mockito inline, some AOP) → verify with `javap` / agent logs.  
- Enormous methods can hurt inlining / compile time.  
- Class-File API lets tooling read/write class files without ASM in modern JDKs.

## Code — see what `javac` did

```java
static int add(int a, int b) {
    return a + b;
}
// javap -c → iload_0; iload_1; iadd; ireturn
```

## Interview / PE

Why stack-based? What does verification buy you? Difference between `invokevirtual` and `invokedynamic`? Does bytecode run on the CPU directly? (No.)

### Related

[interpreter.md](./interpreter.md) · [jit-compiler.md](./jit-compiler.md) · [class-loading.md](./class-loading.md) · [jvm-execution.md](./jvm-execution.md)
