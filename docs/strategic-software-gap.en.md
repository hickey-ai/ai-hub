# Strategic software: ai-hub capability gap

**[中文](./strategic-software-gap.md) · [English](./strategic-software-gap.en.md)**  
Reviewed October 7, 2026 against the 102 current projects. “Strategic software” is a convenient shorthand for national policy priorities, **not an official certification or project eligibility claim**. The [product matrix](./product-matrix.en.md) instead uses four software-industry revenue reporting directions. These are different classification axes.

## Policy basis

- The [15th Five-Year Plan Outline, special column 2, item 03](https://www.ndrc.gov.cn/fggz/fzzlgh/gjfzgh/202603/U020260317369114704096.pdf) (March 2026) identifies operating systems, databases, middleware, programming languages and compilers, development/testing tools, cloud software, and industrial software for R&D design, production control, and business management.
- MIIT's [explanation of the “AI + Software” action plan](https://www.miit.gov.cn/jgsj/xxjsfzs/gzdt/art/2026/art_45df9463e105446c996568f299e07bca.html) (September 11, 2026) addresses AI-assisted development and upgrading basic, industrial and application software.
- MIIT's [14th Five-Year Plan for software and IT services](https://www.miit.gov.cn/zwgk/zcwj/wjfb/tz/art/2021/art_587ca7f076db43a499400525e0c6a244.html) (November 30, 2021) is a **historical cross-check**, not a new 2026 policy.

## Actual coverage versus gaps

| Direction | Relevant local project | Assessment and first verifiable milestone |
| :--- | :--- | :--- |
| OS, database, middleware | Projects **use** Java/Spring Boot and local JSON | **Absent as core products.** Define a bounded kernel/transaction problem and consistency benchmark before considering an independent project. |
| Languages, compilers, dev/test tools | [testops](../testops/README.md) records manual tests; [ai](../ai/README.md) previews commands | **No compiler or automated test runner.** A first test execution service would need sandboxing, timeouts, exit codes, logs and reproducible failure tests. |
| Cloud/platform software | [itops](../itops/README.md) is a local app | **Absent.** Demonstrate two isolated tenants, resource limits and recovery before calling it a platform. |
| Industrial R&D/design | [labbook](../labbook/README.md), [qms](../qms/README.md) record data | **No CAD/CAE/simulation/PLM.** Pick one reproducible model calculation with known expected results; forms alone do not qualify. |
| Industrial production control | [manufacturing](../manufacturing/README.md) now has material/work-order records plus an offline simulator with state transitions and event trace | **Demo, not MES/control software.** Real device acquisition and resilient offline messaging are still absent. Close a work-order → simulated-device data → exception → traceability flow, with reconnect/idempotency tests; physical device acceptance remains separate. |
| Industrial business management | [erp](../erp/README.md), [supply](../procurement/README.md) are local records | **Demo.** Test purchase → receipt → stock → settlement, duplicate requests, concurrent deduction, authorization and reversal. |
| Embedded/device software | [access](../access/README.md), [telecom](../telecom/README.md) are management pages | **No firmware.** Name a device/protocol, then test secure handshake, offline recovery and update rollback; hardware-in-loop testing is additional. |
| AI/software integration | [ai](../ai/README.md) supplies CLI/skill/prompt templates, without model inference | **Demo.** An optional model gateway first needs authentication, timeout/cost limits, sensitive-data protection, evaluation and human review. |
| Trust/security foundation | [authcenter](../authcenter/README.en.md) issues RSA JWTs with JWKS and audience isolation; [manage](../manage/README.md) can verify them for its API | **Local pilot only.** Browser login, role/data authorization, durable audits, key rotation and production security are missing; see [security inventory](./auth-integration.md). |
| Industry application software | [catalog](./project-catalog.en.md) covers many domains | Mostly single-user local demos. Each proposed launch needs an end-to-end business workflow plus security, backup, device/browser and actual-user acceptance. |

**Delivered increment (October 6–7, 2026):** [manufacturing](../manufacturing/README.md) gained offline simulation, durable idempotent events and a trace UI; see the [design and acceptance plan](./strategic-software-design.en.md). The [authcenter](../authcenter/README.en.md) local JWT/JWKS pilot was also added. Neither is production-ready.

**Recommended sequence:** the authcenter JWT/JWKS pilot is in place, but reusable authorization and testing remain first; then one manufacturing workflow with a device simulator; only then evaluate the far more costly OS/database/compiler/simulation tracks. Vue + Java 21 are suitable for these business UIs, not a mandatory implementation language for kernels or firmware. Do not create empty directories to inflate coverage.

On October 7, 2026 the local full build passed 102/102 project packages, four mini-program builds and 160 Maven tests. This does **not** verify all 102 browser business flows, real devices or production compliance. See [existing test evidence](./project-validation-2026-10-03.md); update statuses only with runnable commands, tests and known limitations.
