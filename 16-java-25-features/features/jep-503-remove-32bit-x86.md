# JEP 503 — Remove the 32-bit x86 Port

| | |
|--|--|
| **JEP** | [503](https://openjdk.org/jeps/503) |
| **Status** | **Final** (platform removal) — JDK 25 |

## Purpose

Remove the 32-bit x86 port from the JDK.

## Problem Solved

Maintaining 32-bit x86 increased cost for little modern demand; 64-bit x86-64 / other arches dominate servers and desktops.

## Previous Approach

JDK still carried 32-bit x86 as a build/target in earlier releases (with deprecation warnings in the removal path).

## New Approach

JDK 25 no longer provides that port — use 64-bit JDKs.

## Syntax / API

N/A — platform support change.

## Internal Behavior

Build system and HotSpot 32-bit x86 code paths removed; reduces maintenance burden.

## Production Example

Any remaining 32-bit only appliances must stay on older JDKs or move to 64-bit OS/JDK.

## Limitations

- Hard break for rare 32-bit-only deployments.  
- Does not affect 64-bit x86-64.

## Migration Considerations

Inventory OS architecture before upgrading to 25. CI images must be 64-bit.

## Interview Questions

1. What was removed?  
2. Why remove ports?  
3. Impact on typical cloud Linux amd64/arm64?

### Related

[../compatibility.md](../compatibility.md) · [../migration-21-to-25.md](../migration-21-to-25.md)
