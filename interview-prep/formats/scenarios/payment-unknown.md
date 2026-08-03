# Scenario: Payment unknown outcome

## Prompt

PSP call times out at 30s; funds may be captured. What states and processes do you need?

## Force in answer

Options for sync response vs webhook vs reconcile; trade-off UX vs correctness; Result = PENDING age alert.

## Deep sources

- [../../../system-design/distributed-systems/scenarios/payment-unknown-outcome.md](../../../system-design/distributed-systems/scenarios/payment-unknown-outcome.md)
- [../../../system-design/systems/payment-system.md](../../../system-design/systems/payment-system.md)
- [../../../low-level-design/systems/payment-system.md](../../../low-level-design/systems/payment-system.md)
- [../../../real-world-projects/07-payment-orchestrator/](../../../real-world-projects/07-payment-orchestrator/)
- [../../../scenario-lab/08-duplicate-payment.md](../../../scenario-lab/08-duplicate-payment.md)
