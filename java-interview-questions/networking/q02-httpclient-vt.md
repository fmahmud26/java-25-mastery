# java.net.http.HttpClient with virtual threads

## Question

Why is the modern HttpClient a good fit with VT for many concurrent outbound calls?

## Difficulty

Mid

## Expected answer

Blocking `send` on VT scales without platform-thread-per-call; async API also available. Still need timeouts, limits, and connection caps.

## Common mistake

Unbounded concurrent calls to one host.

## Follow-up

HttpClient thread/executor configuration pitfalls?
