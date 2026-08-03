# JWT validation mistakes

## Question

API gateway “verifies JWT” by base64-decoding payload without signature check. Risk?

## Difficulty

Mid

## Expected answer

Anyone can forge claims. Must verify signature/alg with trusted keys, validate exp/nbf/aud/iss, reject `alg=none`/algorithm confusion.

## Common mistake

Trusting claims because they’re in a JWT shape.

## Follow-up

Access token vs refresh token storage?
