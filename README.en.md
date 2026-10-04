<div align="center">

**[简体中文](./README.md) · [English](./README.en.md)**

<img src="./docs/ai-hub-hero.gif" alt="ai-hub: runnable software ideas for more professions" width="1200" />

### Turn a real need into software you can see and run.

**101 independent runnable projects** &nbsp;·&nbsp; **4 WeChat mini-program builds** &nbsp;·&nbsp; **Vue 3 + Java 21** &nbsp;·&nbsp; **Local-first**

[Explore the ecosystem](#-ai-hub-ecosystem-matrix) · [Get started](#-quick-start) · [All projects](#-explore-by-domain) · [Screenshots](#-screen-previews) · [Architecture](#-architecture-and-delivery)

</div>

> **ai-hub is a growing collection of business software projects.** Explore selected workflows, lightweight record desks for many professions, mini-programs and creative utilities. Pick one directory, run it, inspect the actual pages and adapt it to a concrete need. **Software for every profession is a vision, not a claim of complete industry coverage today.**

## ✨ Pick an entry point

<table>
<tr>
<td width="25%" align="center"><a href="./shop/README.md"><img src="./shop/screenshots/desktop-home.png" alt="shop desktop storefront screenshot" width="100%" /></a><br/><b>01 · Commerce</b><br/><sub>shop · catalog / cart / orders</sub></td>
<td width="25%" align="center"><a href="./manage/README.md"><img src="./manage/screenshots/dashboard.png" alt="manage dashboard screenshot" width="100%" /></a><br/><b>02 · Operations</b><br/><sub>manage · dashboard / users / roles</sub></td>
<td width="25%" align="center"><a href="./labbook/README.md"><img src="./labbook/screenshots/overview.png" alt="labbook experiment records screenshot" width="100%" /></a><br/><b>03 · Research</b><br/><sub>labbook · experiments / revisions</sub></td>
<td width="25%" align="center"><a href="./scenic/README.md"><img src="./scenic/screenshots/overview.png" alt="scenic attraction record demo screenshot" width="100%" /></a><br/><b>04 · Sector demos</b><br/><sub>scenic · attractions / visitor records</sub></td>
</tr>
</table>

<div align="center"><sub>These are screenshots from running projects, not mockups. Click any image for its project guide and more screenshots.</sub></div>

## 🧩 ai-hub ecosystem matrix

![ai-hub ecosystem matrix: workflow apps, mini-programs, sector record desks, creative tools, and the independent local build chain](./docs/ecosystem-matrix.en.svg)

| Layer | Projects / entry points | What to explore | Current scope |
| :--- | :--- | :--- | :--- |
| **Workflow applications** | [shop](./shop/README.md) · [manage](./manage/README.md) · [labbook](./labbook/README.md) | Storefront purchase flow, administration dashboard and experiment records | Feature depth varies; these are not complete production products |
| **Mobile touchpoints** | [shop](./shop/README.md) · [barber](./barber/README.md) · [dining](./dining/README.md) · [selfshop](./selfshop/README.md) | Four uni-app WeChat mini-program builds, each with its project's Java API | The **4 build targets are included within the 101 projects**, not additional systems |
| **Sector record desks** | [scenic](./scenic/README.md) · [realestate](./realestate/README.md) · [health](./health/README.md) · [erp](./erp/README.md) · [more sectors ↓](#-explore-by-domain) | Industry records, related entries, search and forms | Mostly single-user local demos; no real ticketing, diagnosis, closing or regulatory workflows |
| **Creative utilities** | [ai](./ai/README.md) · [html](./html/README.md) · [crawler](./crawler/README.md) | CLI / skill workspace, HTML mini-games and directory demo, public-page crawling | Local utilities and demos, not a hosted AI platform or large-scale crawler |

**Shared delivery conventions, not a shared monolith:** Each project owns its directory, port and data file. Vue 3 pages are built and served by a Java 21 / Spring Boot application, with test scripts and actual screenshots. [See the architecture](#-architecture-and-delivery) · [Read the limits](#-verification-and-safety-limits)

<details>
<summary><b>Expand the animated sector constellation</b></summary>
<br/>
<img src="./docs/ai-hub-constellation.gif" alt="Animated ai-hub sector constellation" width="1200" />
</details>

> **New this round:** [Parcel station](./parcelstation/README.md), [Recycling](./recycling/README.md), and [Childcare](./childcare/README.md). [Three-project checks and limits](./docs/priority-three-validation-2026-10-04.md) are separate from the historical 98-project validation snapshot.

> 💬 **Need something for your profession?** Add QQ **3174667330** to discuss custom development. Bring your use case, number of users and essential workflows. These runnable projects are starting points; a production deployment needs further design, implementation and acceptance testing.

## ⚡ Quick start

Install **Java 21, Maven, Node.js 20.19+/22.12+, and npm**. From the repository root (the first run downloads dependencies):

| Windows PowerShell | macOS / Linux |
| :--- | :--- |
| `./shop/run.ps1` | `./shop/run.sh` |

Open `http://127.0.0.1:8081`, then press `Ctrl+C` to stop. Substitute another directory and use its port from the [catalog](./docs/project-catalog.en.md). The barber, dining and self-shopping apps are **mini-program-only**: their UI requires the corresponding WeChat tooling, not a desktop browser.

## 🗂️ Explore by domain

**101 independent projects · 12 non-overlapping primary categories.** Release batches are no longer used as categories. Mini-program support is a **cross-cutting client type**, not four additional projects. Feature depth varies: consult each project README and the [validation matrix](./docs/project-validation-2026-10-03.md).

| Category | Projects | Start here | Complete list |
| :--- | ---: | :--- | :--- |
| Retail, dining & local services | 13 | [shop](./shop/README.md) · [barber](./barber/README.md) · [dining](./dining/README.md) | [Browse](./docs/project-catalog.en.md#commerce) |
| Enterprise & professional services | 13 | [manage](./manage/README.md) · [crm](./crm/README.md) · [auditfirm](./auditfirm/README.md) | [Browse](./docs/project-catalog.en.md#enterprise) |
| Supply chain, manufacturing & quality | 14 | [erp](./erp/README.md) · [wms](./wms/README.md) · [foodsafety](./foodsafety/README.md) | [Browse](./docs/project-catalog.en.md#supply) |
| Health, healthcare & eldercare | 10 | [hospital](./hospital/README.md) · [health](./health/README.md) · [eldercare](./eldercare/README.md) | [Browse](./docs/project-catalog.en.md#care) |
| Pet services | 4 | [petcare](./petcare/README.md) · [veterinary](./veterinary/README.md) | [Browse](./docs/project-catalog.en.md#pets) |
| Education, research & culture | 11 | [school](./school/README.md) · [labbook](./labbook/README.md) · [museum](./museum/README.md) | [Browse](./docs/project-catalog.en.md#education) |
| Mobility, tourism & events | 10 | [scenic](./scenic/README.md) · [transit](./transit/README.md) · [autosales](./autosales/README.md) | [Browse](./docs/project-catalog.en.md#mobility) |
| Property, parks & construction | 4 | [realestate](./realestate/README.md) · [property](./property/README.md) · [construction](./construction/README.md) | [Browse](./docs/project-catalog.en.md#places) |
| Agriculture, resources & environment | 7 | [agriculture](./agriculture/README.md) · [energy](./energy/README.md) · [water](./water/README.md) | [Browse](./docs/project-catalog.en.md#resources) |
| Public services & infrastructure | 6 | [gridops](./gridops/README.md) · [access](./access/README.md) · [civic](./civic/README.md) | [Browse](./docs/project-catalog.en.md#public) |
| Developer operations & digital tools | 7 | [testops](./testops/README.md) · [ticketops](./ticketops/README.md) · [ai](./ai/README.md) | [Browse](./docs/project-catalog.en.md#digital) |
| Home & personal planning | 2 | [schedule](./schedule/README.md) · [parenting](./parenting/README.md) | [Browse](./docs/project-catalog.en.md#personal) |

> “Software for every profession” is a vision, not a claim of complete industry coverage. The [full catalog](./docs/project-catalog.en.md) lists every project, its local scope, port and screenshot.

## 🖼️ Screen previews

Captured application pages, not design mockups. A screenshot is an entry point, not evidence of complete acceptance testing.

<table>
<tr><td width="50%" align="center"><a href="./barber/README.md"><img src="./barber/screenshots/overview.png" alt="Barber booking preview" width="100%" /></a><br/><b>Barber booking · mini-program</b></td><td width="50%" align="center"><a href="./gridops/README.md"><img src="./gridops/screenshots/overview.png" alt="Grid case workflow" width="100%" /></a><br/><b>Grid cases · public services</b></td></tr>
<tr><td width="50%" align="center"><a href="./ai/README.md"><img src="./ai/screenshots/overview.png" alt="Local AI tools" width="100%" /></a><br/><b>AI tools · digital creation</b></td><td width="50%" align="center"><a href="./realestate/README.md"><img src="./realestate/screenshots/primary.png" alt="Real estate listings" width="100%" /></a><br/><b>Real estate · property</b></td></tr>
</table>

[All 101 projects' screenshot links](./docs/screenshots.en.md) · [Project catalog](./docs/project-catalog.en.md).

## 🧭 Architecture and delivery

![Independent frontend, Java service, local data and verification per project](./docs/architecture.svg)

- **Independent apps, not one monolith:** each directory has its own Java 21/Spring Boot service and port. Vue 3 Web builds are served from their corresponding jars alongside same-origin `/api` endpoints.
- **Client types do not inflate the count:** shop has desktop and WeChat mini-program clients; barber, dining and selfshop are mini-program-only. These four builds are part of the 101.
- **Local data:** record-based examples use separate local JSON files. Consult each README for its backup/reset behavior. Default binding is `127.0.0.1`; **do not expose directly to the public internet**.

## ✅ Verification and safety limits

```powershell
./test-all.ps1     # Windows: all builds, backend tests and packaging checks
./smoke-test.ps1   # Windows: live-service page, asset and API smoke checks
```

Use `./test-all.sh` on macOS/Linux or `mvn -f <project>/backend/pom.xml test` for a single backend. Recorded evidence as of **October 4, 2026**: round 4 passed 98/98 builds, 139 Maven tests and 99/99 remote CI jobs. Earlier live-service smoke checks covered all 98, and browser workflow checks covered 80 sector record demos; see the [per-project evidence](./docs/project-validation-2026-10-03.md). Round 5 only added manifest preflight and ran nine script tests; it **did not rerun all business tests**. The [quality ledger](./docs/quality-loop.md) separates each round's scope.

> [!IMPORTANT]
> These are runnable local starting points, **not production-ready business systems**. Unified authentication/authorization, real payments, encryption, tamper-proof audit, database migrations and concurrency guarantees are not universally provided. Do not enter real sensitive medical, financial, child, access-control or research data. A mini-program build is not a WeChat device acceptance test; production requires dedicated security, privacy, compliance, backup and business verification.

## 📚 Documentation and customization

[Full catalog](./docs/project-catalog.en.md) · [All screenshots](./docs/screenshots.en.md) · [Documentation guide](./docs/README.md) · [Validation matrix](./docs/project-validation-2026-10-03.md) · [Research map (Chinese)](./docs/business-map.md)

If you have a concrete workflow and user base in mind, we can discuss custom development starting from a relevant project. **QQ 3174667330 · 3174667330@qq.com**.
