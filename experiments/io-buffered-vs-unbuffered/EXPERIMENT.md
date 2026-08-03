# Experiment: Buffered vs unbuffered I/O

## Hypothesis

Writing many small chunks with `FileOutputStream.write(byte)` unbuffered is much slower than wrapping with `BufferedOutputStream` (or writing larger buffers), because each write may syscall.

## Setup

JDK 25; write `n` single bytes to a temp file; compare unbuffered vs buffered; delete file after. Teaching microbench — disk/OS dependent.

## Code

```bash
./run.sh
./run.sh 5_000_000
```

## Expected behavior

Buffered median wall time **much lower**. Confirm locally (SSD vs HDD differs).

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: writing 2000000 single-byte writes; unbuffered FileOutputStream median=2586.23ms (min=2557.70 max=2598.26); BufferedOutputStream median=10.65ms (min=10.60 max=23.17)
- Production implication: Buffering cut median write time from 2586.23ms to 10.65ms (~243×)—always buffer small-read/write loops.

```text
writing 2000000 single-byte writes (not JMH; disk-dependent)
unbuffered FileOutputStream median=2586.23ms (min=2557.70 max=2598.26)
BufferedOutputStream median=10.65ms (min=10.60 max=23.17)
```

## Explanation

Buffering amortizes syscalls. Same lesson for readers (`BufferedInputStream`/`BufferedReader`).

## Production implication

Always buffer small-read/write loops. For NIO at scale, consider channels/transferTo and measure.

## Interview takeaway

“Unbuffered byte loops are an I/O footgun I can demonstrate.”
