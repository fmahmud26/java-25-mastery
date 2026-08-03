# Secrets in logs and exceptions

## Question

Support finds API tokens in exception messages and debug logs. Prevention?

## Difficulty

Mid

## Expected answer

Never log secrets; redact; use secret managers; careful `toString`; avoid putting tokens in exceptions; structured logging allowlists.

## Common mistake

Logging full HTTP headers “temporarily.”

## Follow-up

How do you rotate a leaked token?
