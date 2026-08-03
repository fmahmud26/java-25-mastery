# JEP honesty

## Question

Candidate claims “Java 25 made G1 default everywhere via JEP 523 and removed ZGC.” How do you respond?

## Difficulty

Mid

## Expected answer

Reject the claim. On **Java 25**: **G1** remains the typical **server-class** default (constrained environments may still get Serial unless pinned). **ZGC and Shenandoah remain available**. **JEP 523** (“G1 everywhere”) targets a **later** JDK (OpenJDK lists release **27**) — it is **not** a Java 25 change. Verify against the OpenJDK 25 JEP list / release notes; don’t invent features.

## Common mistake

Memorizing Twitter threads as specs.

## Follow-up

Where do you look up authoritative JEP status? (OpenJDK JEP pages, JDK release notes.)
