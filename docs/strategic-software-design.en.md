# Strategic software delivery design

**[中文](./strategic-software-design.md) · [English](./strategic-software-design.en.md)**

October 6, 2026. Engineering plan following the [capability gap review](./strategic-software-gap.en.md), not a certification or a claim that every strategic-software category has been implemented.

```text
Browser / mini-program → business APIs → authentication/authorization (next phase)
                                      ├→ manufacturing work order → state machine → offline simulator → trace (delivered)
                                      ├→ isolated test runner (planned)
                                      └→ optional model gateway and evaluation (planned)
Each demo retains its own local JSON; this is neither a transactional platform nor tamper-proof storage.
```

**Delivered first increment:** The existing [manufacturing project](../manufacturing/README.md) now exposes `POST /api/workorders/{id}/simulate` and `GET /api/workorders/{id}/trace`. A synchronous server-side state machine accepts start, output, fault, resume, quality inspection and completion events; output cannot exceed plan and inspection requires the planned quantity. Event IDs are durable idempotency keys; a conflicting reuse gets 409. Trace data is persisted with the work order, and older files without events remain readable. Once events exist, the generic editor cannot rewrite the state or plan, and deletion is blocked. [Actual local UI screenshot](../manufacturing/screenshots/simulator.png).

**Not production/OT software:** No physical device, protocol adapter, signed telemetry, hardware-in-loop verification, offline queue, multi-user authorization, industrial network separation, immutable audit or recovery SLA. Direct local JSON edits are not protected.

| Phase | Deliverable and evidence |
| :--- | :--- |
| P0 shared security | Browser login and resource-level server authorization on one existing app, audit, 401/403, expiry/rotation and role-based browser tests. Only then generalize. |
| P1 manufacturing | Device simulator protocol adapter and offline retry, signed events and export; restart, duplicate, reorder, disconnect and quantity-conservation tests. Physical equipment acceptance remains separate. |
| P1 dev/test tooling | An isolated allow-listed runner with timeouts, resource limits, versioned logs, failed-case replay and malicious-input rejection. |
| P2 design/business software | One reproducible simulation problem plus procurement-stock-settlement transactions, concurrent deduction, rollback and authorization tests. |
| P2 AI integration | Opt-in gateway with no bundled keys, limits, timeout/fallback, sensitive-data checks, evaluation and human review. |
| Separate R&D programs | OS, database, compiler, middleware, cloud platform and firmware need appropriate languages, expertise and benchmarks; Vue/Java record screens do not satisfy them. |

Run `mvn -f manufacturing/backend/pom.xml test` and `npm --prefix manufacturing/frontend run build`. Tests cover simulated transitions, idempotency, invalid input, persistence and API guards; they do not certify real hardware or all 102 apps.
