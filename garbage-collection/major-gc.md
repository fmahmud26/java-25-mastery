# Major Collection

Informal term — **clarify in interviews** because blogs disagree.

## Mental Model

```text
Minor  ≈ young only
Major  ≈ old-gen effort (sometimes loosely “not minor”)
Full   ≈ whole-heap fallback / complete collection
```

## Prefer Precise Vocabulary (especially G1)

| Term | Prefer saying |
|------|----------------|
| Young / young-only | Eden+survivor collection |
| Mixed (G1) | Young + selected old regions |
| Concurrent mark | Old live-data discovery while app runs |
| Full GC | Whole-heap, usually emergency / fallback |

## Production Implications

Saying “major GC spike” in an incident channel is ambiguous. Paste **GC log lines** or JFR event names instead.

## Interview / PE

Define how *you* use major vs full. Explain G1 mixed GC without calling it “major” only.

### Related

[minor-gc.md](./minor-gc.md) · [full-gc.md](./full-gc.md) · [g1-gc.md](./g1-gc.md)
