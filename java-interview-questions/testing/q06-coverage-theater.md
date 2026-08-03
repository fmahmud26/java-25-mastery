# Coverage theater

## Question

Org mandates 90% line coverage; teams write assertions-free tests. Principal response?

## Difficulty

Principal

## Expected answer

Reject coverage as sole goal. Prefer mutation testing, critical-path risk coverage, contract/integration for boundaries, delete worthless tests. Measure escaped defects, not vanity %.

## Reasoning

Incentives drive behavior—bad metrics create busywork.

## Follow-up

What metrics indicate testing health?

## Common mistake

Blocking merges purely on % lines.

## Principal-level discussion

Change the scoreboard; educate EMs; invest in test quality reviews; celebrate deleted flaky tests that hid risk.
