# orbit CRM — customer and opportunity workspace

[简体中文](./README.md) · **English**

A local workspace for customer records, a three-stage sales pipeline, follow-up notes and won/lost outcomes. Built with Vue 3 and Java 21 / Spring Boot. Switch between English and Chinese in the top bar; the choice is saved in this browser. Existing business records are preserved in their original language.

![English sales pipeline, captured from a running local application](./screenshots/pipeline-en.png)

<details>
<summary>Customers, follow-up and filtered CSV export</summary>

![Customer directory](./screenshots/customers-en.png)
![Follow-up and stage progression](./screenshots/followup-en.png)
![Filtered opportunities and CSV export](./screenshots/export-en.png)

</details>

## Run it

Requirements: Java 21, Maven, Node.js 20.19+ or 22.12+, and npm. From the `crm` directory:

```powershell
./run.ps1
```

```sh
./run.sh
```

The launcher installs dependencies, builds the frontend, runs backend tests and starts one application at **http://127.0.0.1:8083**. The first run needs internet access to download dependencies. Stop with Ctrl+C.

## Try a complete workflow

1. Choose **English** in the language selector. Open **Customers**, then **Add customer**; use fictional details for demonstrations.
2. Open **Pipeline** and create an opportunity for that customer. Amounts are **CNY, in whole yuan**; language switching never changes the currency or converts values.
3. Open the opportunity, add a follow-up, and move through Discovery → Proposal → Negotiation → Won. You may mark an open opportunity as Lost. Closed opportunities cannot be reopened or edited through these actions.
4. Open **All opportunities**, search by opportunity, customer or owner, then **Export CSV**. Export includes only matching rows, IDs, customer names, CNY values, stages, owners and creation dates. Headers and stages use the selected UI language. Business text stays unchanged.

CSV uses UTF-8 with BOM and quoted fields. Formula-like cell prefixes are escaped for spreadsheet import; treat exports as untrusted data and do not remove the protection or enable external spreadsheet content. CSV is a report, **not a restorable backup**, and no import feature is included.

## Data and backups

Records are saved to `data/crm.json`, relative to the launcher directory. Stop the application before copying the file for backup or restoring a copy. Do not run multiple instances against one file. To reset fictional demonstration data, stop the application, back up and remove this file, then restart. A damaged file fails startup instead of silently resetting your records. Initial sample records are Chinese; English records can be added using the form.

## Verify a change

```sh
npm --prefix frontend ci
npm --prefix frontend test
npm --prefix frontend run build
mvn -f backend/pom.xml test
```

The frontend tests cover CSV quoting, formula prefixes, filtered exports and translated stage presentation. Backend tests cover the business API and persistence. Run the launcher to verify the packaged frontend as well.

## Current scope

This is a **single-user local application**, not a hosted multi-tenant service. It has no sign-in, role enforcement, audit log, payment integration or email integration. The owner is a text field. It binds to `127.0.0.1` by default. Add and verify server-side authentication, authorization and data isolation before any public deployment. Do not use real customer data in public screenshots.
