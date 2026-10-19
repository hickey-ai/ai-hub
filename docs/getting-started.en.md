# Getting started: try 4 representative projects locally

Run the commands below from the **repository root**. Install Java 21, Maven, Node.js 20.19+/22.12+, and npm. On first run, the scripts download dependencies, build the Vue client, execute backend tests, and package the service. Each command occupies its terminal; wait for the server to start before opening the page and press `Ctrl+C` to stop it. The projects are independent, so start with just one.

## Check the tools before building

Run these read-only checks in a terminal:

```sh
java -version
mvn -v
node --version
npm --version
```

`java -version` should report Java 21, and the Java version shown by `mvn -v` should also be 21. Node.js must meet the version requirement above. If a command is missing or Maven uses a different JDK, fix the local tool setup first and reopen the terminal. These checks confirm tool availability; they do not verify dependency downloads, free ports, or business functionality.

| Project | Windows PowerShell | macOS / Linux | Local page |
| :--- | :--- | :--- | :--- |
| Shop | `./shop/run.ps1` | `./shop/run.sh` | `http://127.0.0.1:8081` |
| [CRM](../crm/README.en.md) | `./run-demo.ps1 crm` | `./run-demo.sh crm` | `http://127.0.0.1:8083` |
| Manage | `./manage/run.ps1` | `./manage/run.sh` | `http://127.0.0.1:8082` |
| Authcenter | `./authcenter/run.ps1` | `./authcenter/run.sh` | `http://127.0.0.1:8182` |

## 1. Shop: from catalog to demo order

Start shop and open `http://127.0.0.1:8081`:

1. Filter or search the catalog, then open a product to inspect its details, price, and stock.
2. Add it to the cart, change the quantity, proceed to checkout, and enter a **fictional** name and email address.
3. Submit to see a demo order number. No real payment or delivery occurs. Orders and stock persist in `shop/data/shop.json`; repeated checkouts change the demo stock.

To restore the initial catalog, stop the service first, back up or delete `shop/data/shop.json`, then restart. **Do not delete it while the service is running.** The WeChat mini-program needs a separate `./shop/run-miniprogram.ps1` or `./shop/run-miniprogram.sh` build. Import `shop/miniprogram/dist/build/mp-weixin/` in WeChat DevTools. A browser screenshot is not a device acceptance test; see the [shop guide](../shop/README.md) for AppID, approved HTTPS domain, and device setup.

## 2. Manage: users and activity

Start manage and open `http://127.0.0.1:8082`. The default is an **unauthenticated local demo**:

1. Inspect dashboard metrics and search or filter demo members under User Management.
2. Add a user with fictional details and check that the list updates.
3. Open Operation Logs to see the change. Users and new log entries persist in `manage/data/manage.json`. To restore defaults, stop the service first, then back up or delete this file.

The default mode has no real identity verification; displayed roles and menus are not access controls. **When `AIHUB_AUTH_ISSUER_URI` is configured, `/api/**` requires a JWT, but the browser has no SSO login flow and cannot complete the steps above in the same way.** Use an API client and the [authentication integration guide](./auth-integration.md) for token testing. A single config value does not deliver a complete browser login.

## 3. Authcenter: local token pilot

Start authcenter separately and open `http://127.0.0.1:8182`:

1. Sign in with the demo account `admin / admin123` to inspect the session and administrator audit view. `operator / operator123` cannot view administrator audit events.
2. Inspect the discovery and public-key endpoints, then clear the session in the UI. Clearing the browser token **does not revoke** an already issued JWT.

Accounts, RSA keys, and audit events are for local demonstration; restarting invalidates old tokens. To connect the manage API, run manage in **another terminal** with a compatible issuer and audience configured before startup. See the [integration guide](./auth-integration.md) and [authcenter guide](../authcenter/README.en.md) for commands, 401/200 checks, and limitations. **Never use demo credentials or tokens for a public deployment.**

## 4. CRM: customers and opportunity follow-up

From the repository root, run `./run-demo.ps1 crm` (Windows) or `./run-demo.sh crm` (macOS/Linux), then open `http://127.0.0.1:8083`:

1. Choose **English** in the top bar. In **Customers**, add a fictional customer, then **Edit** the contact details and save. The customer ID and existing opportunity links remain stable.
2. In **Pipeline**, create an opportunity for that customer. Add a follow-up note and progress it through the stages to **Won** or **Lost**. Amounts are CNY in whole yuan; switching language does not convert currency.
3. In **All opportunities**, search by customer, title or owner and choose **Export CSV**. The report contains only matching opportunities and uses the current customer name. CSV is not a restorable backup.
4. Stop with `Ctrl+C`, rerun the same launcher and confirm the saved records remain. To back up, stop the application and copy `crm/data/crm.json`; do not delete or replace it while the service runs.

This local single-user application has no sign-in or role enforcement. Keep it on the loopback interface and use fictional details. [Full CRM workflow, screenshots and limits](../crm/README.en.md).

## More projects and verification

- Browse by profession in the [catalog](./project-catalog.en.md), by software-industry direction in the [product matrix](./product-matrix.en.md), or by page in the [screenshot index](./screenshots.en.md). Barber, dining, and self-shopping have mini-program UIs only, not desktop pages.
- After code changes, run `./test-all.ps1` or `./test-all.sh` at the repository root for build and backend checks. The [historical per-project validation record](./project-validation-2026-10-03.md) does not claim acceptance of every page or device.
- On a port conflict, stop the process occupying that port. For a stale page, rerun the project's start script. Services bind to `127.0.0.1` by default; **do not expose them directly to the internet or enter real sensitive data**. See the [security policy](../SECURITY.md).
