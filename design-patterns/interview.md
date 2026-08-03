# Interview — Design Patterns (Decision Style)

**Format for every answer:** Problem → Forces → Naive pain → Pattern → Trade-offs → Use / don’t use → Domain example.

---

## Quick contrasts

| Pair | Distinguisher |
|------|----------------|
| Factory vs Abstract Factory | One product vs **family** of products |
| Strategy vs State | Swap algorithm vs swap **lifecycle** behavior |
| Decorator vs Proxy | Add responsibilities vs **control access** |
| Adapter vs Facade | Make interfaces compatible vs **simplify subsystem** |
| Command vs Observer | Encapsulate **intent/action** vs notify **interest** |
| Template Method vs Strategy | Inheritance skeleton vs composition algorithms |
| CoR vs Decorator | Pipeline may **stop** vs usually wrap & delegate |

---

## SOLID prompts

- Which pattern best supports OCP for payment methods? → **Strategy** (+ Adapter to vendors)  
- How does Facade relate to SRP? → Use-case API vs subsystem details  
- Why Singleton fights DIP? → Hidden global vs injected ports  

---

## LLD prompts

Design checkout: expect Facade + Strategy/Adapter + State + events (Observer/outbox). Name forces, not buzzwords.

---

## Trap

“Always use design patterns.”  
**Reject:** YAGNI — patterns when naive design’s forces hurt.

### Related

[how-to-think-about-patterns.md](./how-to-think-about-patterns.md) · [patterns-and-solid.md](./patterns-and-solid.md) · [patterns-and-lld.md](./patterns-and-lld.md)
