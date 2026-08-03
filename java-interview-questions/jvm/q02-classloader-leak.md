# Metaspace climb after hot redeploys

## Question

An old WAR container redeploys weekly without restart; Metaspace grows until NPE/ OOM. Likely cause?

## Difficulty

Staff

## Expected answer

Classloader leak: previous deployment’s classloader retained via ThreadLocal, static caches, JDBC drivers, shutdown hooks, JMX. New classes load; old ones never unload.

## Reasoning

Classes GC only when loader becomes unreachable. Leaked loader → permanent Metaspace retention.

## Follow-up

How do you confirm with `jcmd`/`jmap` histo or classloader histograms?

## Common mistake

Only raising MaxMetaspaceSize.

## Principal-level discussion

Prefer immutable infra + process restart over in-place redeploy; ban known leak patterns; periodic restart policy if stuck on legacy app servers.
