# JMC — JDK Mission Control

GUI (and related tooling) to analyze JFR recordings and inspect live JVMs.

## Measure / Analyze

1. Capture `.jfr` (jcmd or start-on-launch)  
2. Open in JMC  
3. Start with automated rules / overview, then:  
   - Hot methods / flame-ish views  
   - GC / heap  
   - Lock instances  
   - Allocations  

## Hypothesize

Use JMC to turn “slow” into “method M + GC pause at T+12s + monitor N.”

## Experiment

Compare two recordings (before/after) with the same workload window length.

## Notes

- JMC version should understand your JDK’s event set.  
- For headless: `jfr` CLI views still valuable.  

### Related

[jfr.md](./jfr.md) · [tools/java-mission-control.md](./tools/java-mission-control.md) · [incidents.md](./incidents.md)
