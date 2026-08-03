# Upgrading fleet from 17 to 25

## Question

You’re Principal for a 300-service Java estate on 17. Propose a Java 25 LTS migration approach.

## Difficulty

Principal

## Expected answer

Inventory language/API usage; build with 25 early; runtime canaries; fix removed APIs/security defaults; re-validate GC/flags; update VT guidance; progressive waves by risk; rollback plan; education on real JEPs only.

## Reasoning

Upgrades fail from unmanaged flags, deprecated removals, and behavior changes—not from “Java is hard.”

## Follow-up

How do you handle libraries that lag JDK support?

## Common mistake

Big-bang production cutover Friday.

## Principal-level discussion

Treat JDK as a platform product: compatibility CI matrix, golden Docker images, owned upgrade train, communicate preview policy, measure perf/regressions per wave.
