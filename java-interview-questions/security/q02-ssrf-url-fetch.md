# Server-side URL fetch feature

## Question

Product wants “preview any URL.” Security review concerns?

## Difficulty

Senior

## Expected answer

SSRF risk: server may hit internal metadata/IPs. Mitigate with allowlists, block private ranges, network egress controls, no link-local/metadata IPs, size/time limits—not just regex.

## Reasoning

User-controlled URLs + powerful server network = internal recon/exfil paths.

## Follow-up

Why is DNS rebinding a concern?

## Common mistake

“We only allow http/https” as sufficient.

## Principal-level discussion

Default deny egress; dedicated proxy for fetches; threat model before feature build; security champions in product design.
