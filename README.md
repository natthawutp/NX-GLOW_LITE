# GWH Modern WMS

Enterprise Warehouse Management System with modern UI/UX.

## Development Guidance

Project-specific implementation instructions for humans and AI agents live in [AGENTS.md](AGENTS.md).

Important standing rule: every user-triggered action must show clear in-progress, success, and failure feedback, and must guard against duplicate submissions while it is running.

# Top Tier 1 WMS Platforms (2026 Meta)
Manhattan Active WM: Recognized for its cloud-native architecture and AI-driven automation for complex fulfillment.
<b>Blue Yonder Warehouse Management</b>: AI-powered solution, highly regarded for 3PL and dynamic order management.
**SAP Extended Warehouse Management (EWM)**: A top choice for large, complex operations needing deep ERP integration.
**Oracle Fusion Cloud Warehouse Management**: Highly rated for combining flexible, cloud-based WMS with strong transportation management.
**Körber Supply Chain WMS**: Strong in high-volume, complex distribution and voice-directed operations. 
**Infor WMS**: Tailored for 3PL with strong multi-tenant support and mobile optimization.

## Architecture

| Layer     | Technology                          |
|-----------|-------------------------------------|
| Frontend  | Angular 17 + PrimeNG 17 + Tailwind |
| Backend   | Spring Boot 2.7.18 (WAR)           |
| Database  | Oracle (existing SGWH0001 schema) refer to the [Database document](DATABASE_COMPREHENSIVE.md)         |
| Server    | WebLogic 12c/14c                    |
| Auth      | Custom JWT (stateless)              |
| i18n      | en / ja / th                        |

## Project Structure

```
GwhModernWMS/
├── backend/                    # Spring Boot API
│   ├── build.gradle
│   └── src/main/java/jp/co/nittsu/gwh/modernwms/
│       ├── config/             # Security, CORS, Web config
│       ├── security/           # JWT provider, filter, entry point
│       ├── common/             # Base entity, DTOs, exceptions
│       └── module/
│           ├── auth/           # Login, tokens, user details
│           ├── dashboard/      # KPI summary API
│           ├── inbound/        # Arrival entities
│           ├── outbound/       # Shipment entities
│           ├── inventory/      # Stock entities
│           └── master/         # Product entities
├── frontend/                   # Angular SPA
│   ├── package.json
│   ├── angular.json
│   └── src/
│       ├── app/
│       │   ├── core/           # Auth, API, interceptors, guards
│       │   ├── layout/         # Sidebar, topbar, breadcrumb
│       │   ├── shared/         # Reusable components
│       │   └── features/
│       │       ├── auth/       # Login page
│       │       ├── dashboard/  # KPI dashboard
│       │       ├── inbound/    # Orders, receive, confirm, putaway
│       │       ├── outbound/   # Orders, picking, packing, shipping
│       │       ├── inventory/  # Stock, adjustment, transfer, count
│       │       ├── master/     # Products, locations, customers, warehouses, users
│       │       └── reports/    # Report catalog, generation, history
│       ├── assets/i18n/        # en.json, ja.json, th.json
│       └── styles/             # Theme SCSS, global styles
└── README.md
```

## Theme Colors

| Name      | Hex       | Usage                  |
|-----------|-----------|------------------------|
| Primary   | `#1A005D` | Brand, headings, nav   |
| Accent    | `#8EC400` | Success, CTAs, active  |
| Secondary | `#5BC2E7` | Info, links, badges    |
| Warning   | `#FF9E1B` | Alerts, pending states |
| Danger    | `#FF585D` | Errors, destructive    |

## Quick Start

### Frontend Development

```bash
cd frontend
npm install
npm start                # Serves at http://localhost:4200
```

### Backend Development

```bash
cd backend
./gradlew bootRun        # Serves at http://localhost:8080
```

### Build WAR for WebLogic

```bash
cd frontend && npm run build     # Build Angular
cd ../backend && ./gradlew war   # Package WAR with frontend
```

### Deploy to WebLogic

```bash
cd backend
./gradlew deployToWebLogic \
  -PwlAdminUrl=t3://localhost:7001 \
  -PwlUsername=weblogic \
  -PwlPassword=welcome1
```

Access at: `http://localhost:7001/gwh-modern/`

## WMS Modules

| Module      | Pages                                          |
|-------------|-------------------------------------------------|
| Dashboard   | KPIs, charts, activity timeline, alerts         |
| Inbound     | Arrival orders, receiving, confirmation, putaway|
| Outbound    | Shipment orders, picking, packing, shipping     |
| Inventory   | Stock inquiry, adjustment, transfer, cycle count|
| Master Data | Products, locations, customers, warehouses, users|
| Reports     | Catalog, generate, history, scheduled reports   |

## API Endpoints

| Method | Path                          | Description          |
|--------|-------------------------------|----------------------|
| POST   | `/api/v1/auth/login`          | User login           |
| POST   | `/api/v1/auth/refresh`        | Refresh JWT token    |
| POST   | `/api/v1/auth/logout`         | User logout          |
| GET    | `/api/v1/dashboard/summary`   | Dashboard KPIs       |

## Security

- JWT access tokens (15 min) + refresh tokens (7 days)
- Role-based access: ADMIN, INBOUND, OUTBOUND, INVENTORY, MASTER, REPORTS, OPERATOR
- Tenant context (company/warehouse/customer) in JWT claims
- Stateless session management


## Guild

 The fastest way depends on whether you need the backend (API) or are just working on UI.

**Option 1 — Fastest: `ng serve` with hot-reload (recommended for frontend work)**

Your project already has proxy.conf.json configured to forward API calls to WebLogic. Just run:

```
cd D:\WMS_WORK_SPACE\GWA\GwhModernWMS\frontend
npx ng serve --proxy-config proxy.conf.json
```

This gives you **instant hot-reload** — every file save refreshes the browser automatically at `http://localhost:4200`. No rebuild, no redeploy. API calls route through to WebLogic at port 7001.

**Option 2 — Deploy to WebLogic (when you need to test the full WAR)**

```powershell
cd D:\WMS_WORK_SPACE\GWA\GwhModernWMS\frontend
npx ng build --configuration=production --base-href=/gwh-modern/

cd ..\backend
gradle war --no-daemon

# Redeploy
java -cp "D:\Oracle\Middleware\Oracle_Home\wlserver\server\lib\weblogic.jar" weblogic.Deployer -adminurl t3://localhost:7001 -username weblogic -password welcome1 -redeploy -name GwhModernWMS -source "D:\WMS_WORK_SPACE\GWA\GwhModernWMS\backend\build\libs\gwh-modern.war"
```

## Start frontend
cd D:\WMS_WORK_SPACE\GWH\GWH\NX-GLOW_LITE\frontend; npm start

## Start backend
cd D:\WMS_WORK_SPACE\GWH\GWH\NX-GLOW_LITE\backend; gradle bootRun
