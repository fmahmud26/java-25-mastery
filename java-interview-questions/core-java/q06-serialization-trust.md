# Java serialization in a public API

## Question

Legacy code accepts `ObjectInputStream` from untrusted clients “for convenience.” Leadership asks if that’s acceptable. How do you answer as Staff/Principal?

## Difficulty

Staff

## Expected answer

No. Java deserialization is a well-known RCE gadget surface. Don’t deserialize untrusted bytes. Prefer JSON/protobuf with allowlists, or signed opaque tokens. If legacy must remain, isolate, filter (`ObjectInputFilter`), network-segment, and plan kill.

## Reasoning

Gadgets in classpath can execute on deserialize. Filters reduce but don’t make it “safe by default” for open internet. Attackers innovate faster than filter lists.

## Follow-up

What’s the difference between serializing a session token vs a full object graph?

## Common mistake

“We only deserialize our classes” while dependencies still provide gadgets.

## Principal-level discussion

Set org standard: no Java serialization across trust boundaries. Track via security reviews and dependency scanning. Migration: replace with explicit schemas; dual-run; delete `Serializable` from public APIs. Own the residual risk statement for auditors.
