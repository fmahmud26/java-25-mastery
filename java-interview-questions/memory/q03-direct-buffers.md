# Direct ByteBuffer native OOM

## Question

Heap looks fine; process dies with native OOM / `OutOfMemoryError: Direct buffer memory`. Cause?

## Difficulty

Senior

## Expected answer

Explicit/direct buffers allocate off-heap; may not be freed promptly if not released/ greeded by GC of buffer objects. Cap `-XX:MaxDirectMemorySize`; pool/reuse buffers; ensure `Cleaner`/release paths.

## Reasoning

Off-heap not constrained by `-Xmx`.

## Follow-up

Netty pooled arenas vs JDK buffers?

## Common mistake

Only watching heap charts.

## Principal-level discussion

Monitor direct memory metrics; standardize Netty allocator settings; incident playbooks distinguish heap vs native.
