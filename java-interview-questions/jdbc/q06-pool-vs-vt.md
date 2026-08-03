# Connection pools under virtual threads

## Question

Staff standard needed: VT enabled services keep OOMing DB with `maxLifetimeConnections`. Policy?

## Difficulty

Staff

## Expected answer

Pool size from DB capacity / instance count—not from VT count. Admission control; queue; timeouts on acquire; never “Hikari=10000.”

## Reasoning

DB is the scarce resource.

## Follow-up

How do you compute a starting pool formula?

## Common mistake

One pool size copied to all services.

## Principal-level discussion

Central capacity sheet; DB owners enforce `max_connections`; platform templates ship conservative defaults + docs.
