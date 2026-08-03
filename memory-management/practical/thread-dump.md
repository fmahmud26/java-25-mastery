# Practical: Thread Dump

A **thread dump** shows every thread’s state and stack — deadlocks, blocked I/O, hot stacks.

## Capture

```bash
jcmd <pid> Thread.print
jstack <pid>
kill -3 <pid>          # Unix — prints to stdout of process
jcmd <pid> Thread.dump_to_file filename=/tmp/threads.txt
```

Take **several** dumps a few seconds apart for CPU/stuck issues.

## Read a dump

```text
"http-0" #23 RUNNABLE
   java.lang.Thread.State: RUNNABLE
        at com.example.Handler.handle(...)
"worker-1" #24 BLOCKED
   java.lang.Thread.State: BLOCKED (on object monitor)
        - waiting to lock <0x...> (a java.lang.Object)
```

| State | Meaning |
|-------|---------|
| `RUNNABLE` | Running or ready (may be in native) |
| `BLOCKED` | Waiting for monitor |
| `WAITING` / `TIMED_WAITING` | `wait`, park, sleep, join |
| `DEADLOCK` section | JVM detected cycle |

Virtual threads may appear in dumps differently (carriers + VT names) — look for both.

Related: [deadlock.md](./deadlock.md), [high-cpu.md](./high-cpu.md), [../stackoverflowerror.md](../stackoverflowerror.md).
