# OutOfMemoryError

Thrown when allocation fails. The **message** selects the memory domain — don’t treat all OOMs as “raise `-Xmx`.”

## Mental Model

```text
OOM = a specific space could not satisfy an allocation
Read the suffix. Dump/metrics for that space.
```

## Variants

| Message | Domain |
|---------|--------|
| `Java heap space` | Java heap |
| `Metaspace` | Class metadata native |
| `Compressed class space` | Compressed class region |
| `Direct buffer memory` | Off-heap `ByteBuffer` |
| `Unable to create native thread` | Threads / native memory / ulimit |
| `GC overhead limit exceeded` | Too much time in GC (if enabled) |
| `Requested array size exceeds VM limit` | Absurd array length |

```bash
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/dumps \
     -Xmx1g -jar app.jar
```

## Response Playbook

1. **Read the message** — heap vs metaspace vs direct vs threads  
2. **Capture** dump / NMT / metaspace / thread count  
3. **Analyze** retainers or native holders  
4. **Fix** leak or correctly resize **that** pool  
5. **Prevent** bounds + alerts on the right metric  

Blind `-Xmx↑` hides heap leaks and ignores metaspace/direct OOMs.

## Unexpected OOM

See [incidents.md](./incidents.md): container OOMKill with heap looking fine; or Metaspace OOM; or direct buffers.

## Interview / PE

List five OOM messages and first tool for each. Why is “just increase heap” a PE red flag?

### Related

[memory-leaks.md](./memory-leaks.md) · [metaspace.md](./metaspace.md) · [investigation.md](./investigation.md) · [stackoverflowerror.md](./stackoverflowerror.md)
