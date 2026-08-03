# Tool: JFR

See main guide: [../jfr.md](../jfr.md).

Quick start:

```bash
jcmd <pid> JFR.start name=t settings=profile duration=60s filename=/tmp/t.jfr
```

JDK 25 adds richer method timing / cooperative sampling / optional experimental CPU-time (Linux) — enable deliberately and measure overhead.
