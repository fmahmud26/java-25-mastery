# 01 — Structured Log Analyzer CLI

**Tier:** Foundation · **Demonstrates:** clean architecture (lite), testing, observability (structured stderr), performance (streaming I/O)

## Problem

Ops dumps multi-GB application logs. Engineers need a **local CLI** that parses levels, finds error bursts, and emits a machine-readable report — without loading the entire file into memory, and with predictable exit codes for scripts/CI.

## Requirements

**Functional**
- Parse args: `--file`, `--level`, `--window`, `--json` / human report, `--help`
- Stream-parse lines; count by level; detect bursts (N errors in window)
- Exit `0` success, `2` usage, `1` processing failure, `3` file missing (contract)

**Non-functional**
- Handle files larger than heap via streaming  
- Deterministic output for tests  
- No framework dependency  

## Architecture

```text
cli (Args, ExitCodes, CliLogger)
  → application (LogAnalyzer use-case)
    → domain (LogLine, LogLevel, AnalysisReport)
    → ports: LogParser
```

Keep parsing and analysis separable so parsers can be swapped (plain / JSON logs).

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| Java 25 + stdlib only | Portable interview demo | Spring CLI — overkill |
| Streaming `BufferedReader` | Scale to large files | `Files.readAllLines` |
| Records for domain | Immutability | Mutable beans |
| stderr logs / stdout report | Script-friendly | Mixed streams |

## Design decisions

- **Exit code contract** is part of the API — CI depends on it.  
- **Parser port** isolates regex/format churn from analysis.  
- **Window burst detection** in one pass with deque of timestamps — O(n) time, O(window) memory.

## Implementation plan

1. Domain records + enums  
2. `LogParser` implementation  
3. `LogAnalyzer` single-pass algorithm  
4. `Args` validation + help  
5. Wire `main`; add sample.log  
6. Tests: parser, burst edge, exit codes via main harness  

*(Reference code already under `src/cli/` — evolve toward explicit packages `domain` / `app` if extending.)*

## Failure scenarios

| Failure | Behavior |
|---------|----------|
| Missing file | Exit 3, message on stderr |
| Malformed line | Skip/count `unknown`; don’t crash (policy) |
| Empty file | Valid empty report |
| Huge file | Steady memory; time ~ throughput of disk |

## Testing strategy

- Unit: parser golden lines; burst exactly at threshold  
- Property: shuffled levels → counts sum to parsed lines  
- Contract: argv matrices → expected exit codes  
- Perf smoke: 100MB generated file completes under budget on your machine  

## Performance considerations

- Avoid regex catastrophic backtracking  
- Reuse buffers; don’t concat strings per line for report until end  
- For multi-file: parallel streams of files with VT in a later extension  

## Scaling strategy

- CLI scales with disk; for fleet-wide analysis → ship to [08](../08-notification-outbox/)-style pipeline / log platform  
- Optional: mmap for huge single hosts — measure first  

## Interview discussion

“I treated the CLI as a product: streaming I/O, exit contracts, separable parser, and tests on failure paths. It’s small but shows I don’t start with a framework for a local tool.”

**Follow-ups:** How would you parse JSON logs? Parallelize across files without breaking deterministic order of report sections?

## Run

```bash
chmod +x run.sh && ./run.sh
./run.sh --file sample.log --level ERROR
```
