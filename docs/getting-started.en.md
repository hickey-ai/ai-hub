# Getting started: try the 3 flagship projects first

If you are new to ai-hub, do not choose randomly from 102 directories. The sequence below gives you a useful first impression locally.

## 1. Shop: start with a complete workflow

```bash
./shop/run.sh
```

Open `http://127.0.0.1:8081` and explore the catalog, cart and order flow. The desktop client and mini-program build live under `shop`.

## 2. Manage: see the operations workspace

```bash
./manage/run.sh
```

Open the port listed in the catalog and explore the dashboard, users, roles and business data. To validate API tokens, start `authcenter` and follow the [integration guide](./auth-integration.md).

## 3. Authcenter: inspect the shared foundation pilot

```bash
./authcenter/run.sh
```

Open `http://127.0.0.1:8182` and use the demo account in its README to inspect token issuing, key discovery and audit events. This is a local pilot, not a production identity platform.

## Choose another project

- By profession or sector: [project catalog](./project-catalog.en.md).
- By software-industry direction: [product matrix](./product-matrix.en.md).
- By screenshot: [screenshot index](./screenshots.en.md).
- By evidence: [validation record](./project-validation-2026-10-03.md).

## Troubleshooting

- **Port conflict:** each project has its own port; use the README and catalog, then stop the process holding the port.
- **Frontend dependency failure:** use Node.js 20.19+/22.12+, remove the project `node_modules`, and run `npm ci` again.
- **Stale page:** build the frontend before starting the backend; do not treat the source directory as the jar's static page.
- **Public deployment:** do not expose these demo services directly. Complete authentication, authorization, key management, persistence, backups, rate limits, logging and security review first.
