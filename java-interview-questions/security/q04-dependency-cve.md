# Critical CVE in a JSON library

## Question

Staff process when a critical CVE hits a library on 40% of services?

## Difficulty

Staff

## Expected answer

Triage reachability/exploitability; patch golden images; force upgrades via platform BOM; WAF/interim mitigations if needed; track completion; communicate risk.

## Reasoning

Speed × coverage; don’t wait for every team voluntarily.

## Follow-up

How do SBOM and dependency locks help?

## Common mistake

“Not exploitable in our usage” without analysis evidence.

## Principal-level discussion

Central upgrade trains; SLAs by severity; automatic PRs; exception process with expiry.
