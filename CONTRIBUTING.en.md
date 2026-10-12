# Contributing

Thanks for helping ai-hub. The goal is not to maximize directory count, but to turn real workflows into **runnable, testable and adaptable** software starting points.

## Where to start

- Commerce, products and orders: [`shop`](./shop/README.md).
- Administration, users and permissions: [`manage`](./manage/README.md).
- Shared identity capabilities: [`authcenter`](./authcenter/README.en.md) and [`docs/auth-integration.md`](./docs/auth-integration.md).
- A new profession or sector: check the [project catalog](./docs/project-catalog.en.md) first.

## Before submitting

```bash
python -m unittest discover -s tools -p 'test_*.py' -q
python tools/generate_product_matrix.py --check
./test-all.sh
```

For a focused change, also run the changed frontend build and `mvn -f <project>/backend/pom.xml test`. State the exact commands, results and uncovered boundaries in the pull request. A passing build is not the same as full browser or production acceptance.

## Conventions

- Keep Vue 3 and Java 21/Spring Boot as the default stack unless there is a documented reason to diverge.
- Each project has its own directory and port; keep the catalog and automated checks in sync.
- Bind to `127.0.0.1` by default and never commit demo secrets or sensitive data.
- A new project needs a README, run command, screenshots, port and tests; do not add empty shells.
- Update the corresponding Chinese documentation when changing English documentation, and vice versa, or explain why synchronization is not needed.

## Issues and pull requests

Include the project directory, OS, Java/Node versions, reproduction steps, expected result, actual result and logs. For security reports, do not open a public issue; read [`SECURITY.md`](./SECURITY.md) first.

Keep pull requests focused. UI, accessibility, tests, documentation and real workflow improvements are welcome; copying empty templates only to increase the project count is not.
