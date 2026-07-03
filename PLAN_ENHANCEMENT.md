# NX-GLOW WMS Combined v5.0 — Master Development Specification

**Version:** 5.0  |  **Date:** 24 June 2026  |  **Author:** Natthawut Potisit (Beg) — Manager, NX SAO  
**Reviewers:** Ashish Dey (Sponsor) · Ito Takeshi (Tech Lead) · Pravin Vaidya (Solution Architect)  
**Classification:** Confidential

---

## How to Use This Document

This is the **single-source-of-truth development specification** for the NX-GLOW WMS Combined v5.0 release, merging:
- **Wave Picking SDD v1.0** (Wave Profile, Wave Execution, Task Management, RF Mobile)
- **LPN Integration SDD v4.0** (License Plate Number lifecycle — 9 statuses, dimensions, history)

**AI coding agents read it in this order:**
- §1–§3 — project context, problem statement
- §4–§7 — scope, benchmark, architecture, tech-stack conventions
- §8 — module-by-module build instructions (FRs, BRs, DB tables, REST APIs, pseudocode)
- §9–§10 — function list + database design reference
- §11–§15 — execution plan (process flow, roadmap, KPIs, risks, quick wins)
- Appendix D — DDL quick reference

**Companion files:**
- `NX_GLOW_WMS_SDD_Combined_v5_Wave_Task_LPN.docx` — formatted 18-section SDD
- `NX_GLOW_WMS_Combined_Workshop_Deck_v5.pptx` — 20-slide stakeholder deck
- `NX_GLOW_WMS_LPN_v5_Migration.sql` — idempotent Oracle 19c DDL with rollback

---

## Table of Contents

1. Project Context
2. Executive Summary
3. Problem Statement
4. Scope, Objectives & Success Criteria
5. Industry Benchmark
6. Unified Architecture
7. Tech Stack & Conventions
8. Module Specifications
   - 8.1 Wave Profile Management
   - 8.2 Wave Picking Execution (LPN-aware)
   - 8.3 Task Management (LPN-anchored)
   - 8.4 RF / Mobile Interface
   - 8.5 LPN Master & Lifecycle
9. Unified Function List (92 functions)
10. Combined Database Design (27 new + 5 altered)
11. End-to-End Process Flow
12. 8-Phase Roadmap
13. Expected Benefits & KPIs
14. Risks & Mitigations
15. Quick Wins
16. Acceptance Criteria & Definition of Done
- Appendix A — Glossary
- Appendix B — Reference Files
- Appendix C — Sign-off Block
- Appendix D — DDL Quick Reference

---

## 1. Project Context

| Attribute | Value |
|---|---|
| Company | Nippon Express South-East Asia (NX SAO) |
| Project | NX-GLOW WMS — Warehouse Management System |
| Database | Oracle 19c on AWS RDS, ap-northeast-1 (Tokyo) |
| Schema | `NXGLOW` |
| Baseline | 488 tables, Java EE application-managed referential integrity |
| Existing mobile subsystem | AGS (Agility System) — `AGS_TM_*` / `AGS_TJ_*` |

### Stakeholders

| Role | Name |
|---|---|
| Author | Natthawut Potisit (Beg) — Manager, NX SAO |
| Sponsor / Skip Manager | Ashish Dey |
| Tech Lead | Ito Takeshi |
| Solution Architect | Pravin Vaidya |
| Business Analyst | Sumawadee Nuankeaw |
| Stakeholders | Iwata Mari, Kuki Takanobu |
| AGS Integration POC | Nice-san (India / Australia) |

### Why a Combined v5.0 Release

- Both projects share **the same operational lifecycle** — an LPN is born at dock, allocated by a wave, picked by a task, shipped under the same identifier.
- Combining removes **integration risk** between two parallel projects.
- One stakeholder ask = **less approval friction**.
- Parallel paths save **~8 weeks** (33-week critical path vs. 41-week sequential).
- Single architecture diagram is **easier to explain** to executives.

---

## 2. Executive Summary

NX-GLOW WMS Combined v5.0 merges two previously separate enhancement programs — the Wave Picking SDD v1.0 (Wave Profile, Wave Execution, Task Management, RF Mobile) and the LPN Integration SDD v4.0 (License Plate Number lifecycle with 9 statuses, dimensions, and full history) — into a single unified release that can be delivered by one development squad.

The unified design delivers **5 modules, 27 new database tables, 5 altered existing tables, 92 functions across 11 module groups, and an 8-phase roadmap with a 33-week critical path** that saves approximately 8 weeks vs. sequential delivery.

**LPN is the data spine** — the Wave Engine and Task Engine consume LPNs end-to-end (ASN → LPN(100) → Putaway(500) → Wave → Allocate(600) → Task → Pick(700) → Pack(800) → Ship(900)). Every transition writes one row to `GWH_TJ_LPN_HIS` providing full SOX / customs audit coverage.

The combined approach removes integration risk, reduces approval friction with one stakeholder ask, and produces a single architecture diagram that is easier to explain to executives and operations leaders.

---

## 3. Problem Statement

### 3.1 Without Wave / Task Engines
- Order-by-order processing — no wave grouping → **40–60% redundant picker travel** per shift
- No order grouping by cut-off time, route, customer priority, or carrier
- Manual task assignment — supervisors verbally assign work; no system-directed picking
- No real-time visibility — supervisors rely on end-of-day batch reports
- No task interleaving — pickers return empty-handed between zones
- Feature gap vs. Tier-1 WMS (Manhattan Active WM, SAP EWM, Blue Yonder, Oracle WMS Cloud)

### 3.2 Without LPN Spine
- No LPN lifecycle history table — cannot trace status changes or location moves
- No merge / split audit log — cannot reverse or report consolidation events
- No dimension fields (L×W×H, weight, volume) on LPN — blocks volumetric allocation
- No mixed-SKU flag — picking and cycle-count cannot distinguish homogeneous vs. mixed LPNs
- No QC status field on LPN master — quality holds only tracked at receipt level
- Inventory table `GWH_TJ_INV` does not reference `LPN_ID` — anchored only by SKU/location
- Pick task table `GWH_TJ_PCK` missing `LPN_ID` — blocks full-LPN pick optimisation
- Putaway task table `GWH_TJ_PUT` missing `LPN_ID` — directed putaway operates at carton level
- No LPN-based cycle count program — cycle counts run by SKU/location only

### 3.3 Combined Impact
Both gaps compound — a Wave Engine without LPN cannot perform full-LPN-pick optimisation; an LPN spine without Wave/Task engines has no operational consumer of the lifecycle data. Combining v1.0 and v4.0 delivers the **full value**: directed work, LPN-aware allocation, and end-to-end traceability in one release.

---

## 4. Scope, Objectives & Success Criteria

### 4.1 In-Scope
- **Module 1** — Wave Profile Management
- **Module 2** — Wave Picking Execution (LPN-aware)
- **Module 3** — Task Management (LPN-anchored)
- **Module 4** — RF / Mobile PWA Interface
- **Module 5** — LPN Master & Lifecycle (9 statuses 100→900)

### 4.2 Out-of-Scope
- Slotting optimisation
- Cartonisation (deferred to future Phase 9)
- Voice picking

### 4.3 Success Criteria

| ID | Criterion | Target |
|---|---|---|
| SC-01 | Wave creation from 50 eligible orders | < 5 sec |
| SC-02 | Allocation accuracy | 100% (zero double-allocation) |
| SC-03 | Task push to RF device latency | < 2 sec |
| SC-04 | System availability | ≥ 99.5% |
| SC-05 | Pickers using directed work | ≥ 90% in 60 days |
| SC-06 | LPN scan-confirm rate | ≥ 99% |
| SC-07 | LPN history coverage | 100% |
| SC-08 | Inventory accuracy (LPN-anchored) | ≥ 99.5% |

---

## 5. Industry Benchmark

NX-GLOW currently lacks all 7 capabilities; v5.0 closes the gap to Tier-1 parity.

| WMS | Wave Profile | Wave Picking | Task Mgmt | Task Interleave | LPN Nested | LPN History | RF Directed |
|---|---|---|---|---|---|---|---|
| **NX-GLOW (current)** | No | No | No | No | No | No | No |
| Manhattan Active WM | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| SAP EWM | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Blue Yonder (JDA) | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Oracle WMS Cloud | Yes | Yes | Yes | Partial | Yes | Yes | Yes |
| Infor WMS | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Körber (HighJump) | Yes | Yes | Yes | Yes | Yes | Std | Yes |

---

## 6. Unified Architecture — LPN as Data Spine

### 6.1 Architectural Principle
LPN is the data spine across all five modules. The Wave Engine groups orders into waves; the Task Engine generates and dispatches directed work; the RF/Mobile PWA confirms execution at every scan; every transaction writes to LPN history for end-to-end traceability.

### 6.2 Architecture Diagram

```text
+----------------+   +-------------+   +-----------------+
|  Wave Engine   |   | Task Engine |   |  RF/Mobile PWA  |
|  Profile->Plan |   | Priority    |   |  React, offline |
|  ->Alloc->Rls  |   | Interleave  |   |  scan-confirm   |
+--------+-------+   +------+------+   +--------+--------+
         |                  |                   |
         v                  v                   v
+--------------------------------------------------------+
|              LPN SPINE  (GWH_TJ_LPN)                   |
|  9 statuses 100->900 | nested LPN | full history       |
+--------------------------------------------------------+
         ^                  ^                   ^
         |                  |                   |
+--------+-------+   +------+------+   +--------+--------+
|   Inbound /    |   |  Inventory  |   |   Pack/Ship/    |
|   Putaway      |   |  (LPN x SKU |   |   Yard/Returns  |
|                |   |  x Location)|   |                 |
+----------------+   +-------------+   +-----------------+
```

### 6.3 Integration Touchpoints

| Existing Table | Direction | Used By | Purpose |
|---|---|---|---|
| `GWH_TJ_SP_H/D` | Read | Wave Engine | Source shipping orders |
| `GWH_TJ_ST` | R/W | Wave + Task | Stock availability + allocation |
| `GWH_TJ_LPN/_D` | Alter + R/W | All modules | LPN spine |
| `GWH_TM_LOC` | Read | Wave + Task | Location/zone for routing |
| `GWH_TJ_SPK_H/D` | Write | Task Engine | Output picking records |
| `GWH_TJ_SP_R` | Write | Task Engine | Update shipping results |
| `AGS_TM_DEVC` | Read | RF Layer | Device configuration |
| `AGS_TJ_SCAN` | Write | RF Layer | Scan transaction log |

---

## 7. Tech Stack & Conventions

### 7.1 Backend
- **Runtime:** Java EE on Oracle 19c
- **Persistence:** Application-managed integrity — no DB-level FK constraints
- **Transactions:** ACID with row-level locking for allocation
- **Build:** Maven multi-module (one module per WMS module)

### 7.2 Frontend
- **Admin / Dashboard:** JSP + Servlet (existing) plus React 18 + TypeScript for new screens
- **State management:** Redux Toolkit or Zustand
- **Component library:** Material-UI v5 or Ant Design 5

### 7.3 Mobile / RF
- **Framework:** PWA — React 18 + TypeScript
- **Barcode scan:** Camera mode (ZXing.js / QuaggaJS); HID mode (Bluetooth / USB keyboard-wedge)
- **Offline:** Service Worker + IndexedDB (max 1 active task offline)
- **Push:** Web Push API for new-task notifications

### 7.4 Authentication & Authorisation
- **Auth:** JWT bearer tokens, 8-hour expiry
- **Roles (RBAC):** `PICKER`, `SUPERVISOR`, `MANAGER`
- **Session timeout:** 5 minutes inactivity auto-lock
- **Device sharing:** PIN-based quick-login

### 7.5 Naming Conventions
- **Master tables:** `GWH_TM_*`
- **Transaction tables:** `GWH_TJ_*`
- **History tables:** `GWH_TH_*`
- **Indexes:** `IDX_<table-abbrev>_<col>`
- **Partitioning ready:** `CPNY_COD` + `WHS_COD`
- **Audit columns:** `CRT_DT`, `CRT_USR`, `UPD_DT`, `UPD_USR`
- **Active flag:** `ACT_FLG CHAR(1) DEFAULT 'Y'`

### 7.6 Status Code Conventions

| Object | Type | Values |
|---|---|---|
| LPN | `NUMBER(3)` | 100, 200, 300, 400, 500, 600, 700, 800, 900 |
| Wave | `VARCHAR2(2)` | `'01'..'07'` |
| Task | `VARCHAR2(2)` | `'01'..'08'` |

### 7.7 REST API Conventions
- **Base path:** `/api/v5/`
- **Versioning:** URL path prefix
- **Pagination:** `?page=1&size=50&sort=field,asc`
- **Standard response envelope:**

```json
{
  "success": true,
  "data": { },
  "error": null,
  "ts": "2026-06-24T10:30:00Z"
}
```

### 7.8 Error Codes

| Code | Meaning |
|---|---|
| E0001 | Validation error (bad input) |
| E0002 | Not found |
| E0003 | Conflict (state / concurrency) |
| E0004 | Unauthorized |
| E0005 | Forbidden |
| E0500 | Server error |

---

## 8. Module Specifications

### 8.1 Module 1 — Wave Profile Management

**Purpose:** Configurable rules engine defining **how** and **when** orders group into waves. Profiles are reusable templates owned by warehouse admins and consumed by the Wave Engine.

#### Functional Requirements (10)

| ID | Requirement | Description | Priority |
|---|---|---|---|
| FR-WP-001 | Create Wave Profile | Create named profile with type, pick method, allocation method | MVP |
| FR-WP-002 | Grouping Rules | Define grouping by customer, route, carrier, ship date, zone | MVP |
| FR-WP-003 | Release Rules | Triggers: time-based, volume-based, manual, carrier cut-off | MVP |
| FR-WP-004 | Capacity Constraints | Max orders, lines, weight, volume, picks per wave | MVP |
| FR-WP-005 | Allocation Strategy | FIFO / FEFO / LIFO / Priority / FULL_LPN | MVP |
| FR-WP-006 | Pick Method | Discrete / batch / cluster / zone | MVP |
| FR-WP-007 | Auto-Schedule | Recurring wave creation schedules | P2 |
| FR-WP-008 | Clone Profile | Duplicate existing profile with modifications | P2 |
| FR-WP-009 | Profile Versioning | Maintain version history with effective dates | P3 |
| FR-WP-010 | Activate / Deactivate | Toggle profile active without deletion | MVP |

#### Business Rules (8)

| ID | Rule | Description |
|---|---|---|
| BR-WP-001 | Unique Profile Code | `WVPF_COD` globally unique |
| BR-WP-002 | Mandatory Grouping | At least one grouping rule required |
| BR-WP-003 | Release Rule Required | Cannot activate without a release rule |
| BR-WP-004 | Capacity Validation | Max values > min values |
| BR-WP-005 | Profile Priority | Resolves conflicts when multiple match |
| BR-WP-006 | FEFO Enforcement | Allocation must reserve oldest expiry first |
| BR-WP-007 | Schedule No Overlap | Auto-schedules cannot overlap |
| BR-WP-008 | Deactivation Guard | Cannot deactivate profile with open waves |

#### Database Tables (6)

| Table | Type | Purpose | Key Columns |
|---|---|---|---|
| `GWH_TM_WVPF` | Master | Wave Profile Master | WVPF_COD, WVPF_TYP, PICK_MTD, ALLOC_MTD, MAX_ORD_CNT, ACT_FLG |
| `GWH_TM_WVGR` | Master | Grouping Rules | WVPF_COD, WVGR_SEQ, GRP_ATTR, GRP_OPER, GRP_VAL, MAND_FLG |
| `GWH_TM_WVRL` | Master | Release Rules | WVPF_COD, WVRL_SEQ, RLS_TYP, RLS_TRG_TIM, RLS_INTRVL_MIN |
| `GWH_TM_WVCA` | Master | Capacity Constraints | WVPF_COD, CNST_TYP, CNST_MAX_VAL, CNST_MIN_VAL |
| `GWH_TM_WVAL` | Master | Allocation Strategy | WVPF_COD, ALLOC_SEQ, ALLOC_TYP, LOC_TYP, ZONE_COD |
| `GWH_TM_WVSC` | Master | Wave Schedule | WVSC_COD, WVPF_COD, SCH_TYP, SCH_DAY, SCH_TIM_FROM |

#### REST API Endpoints
- `POST   /api/v5/wave-profiles` — create new profile
- `GET    /api/v5/wave-profiles/{code}` — get one
- `PUT    /api/v5/wave-profiles/{code}` — update
- `DELETE /api/v5/wave-profiles/{code}` — soft delete (set `ACT_FLG = 'N'`)
- `POST   /api/v5/wave-profiles/{code}/clone` — clone
- `POST   /api/v5/wave-profiles/{code}/activate` — activate with rule validation
- `GET    /api/v5/wave-profiles?type=GENERAL&active=Y` — list with filters

#### UI Screens
- **Wave Profile List** — searchable / sortable grid
- **Wave Profile Editor** — tabs: Basic · Grouping · Release · Capacity · Allocation · Schedule
- **Wave Profile Versions Viewer** — history with diff view

#### Acceptance Criteria
- ✅ All 8 business rules enforced (unit-tested)
- ✅ CRUD profile via REST API
- ✅ Profile activation validates required grouping + release rules
- ✅ Editor UI supports all 6 tabs with field validation

---

### 8.2 Module 2 — Wave Picking Execution (LPN-aware)

**Purpose:** Runtime engine that creates waves from active profiles, allocates inventory (LPN-aware), generates pick tasks, manages wave lifecycle, and provides real-time monitoring.

#### Functional Requirements (14)

| ID | Requirement | Description | Priority |
|---|---|---|---|
| FR-WE-001 | Wave Creation | Engine scans `GWH_TJ_SP_H` and matches active profiles | MVP |
| FR-WE-002 | Wave Simulation | Preview wave content before committing | MVP |
| FR-WE-003 | Inventory Allocation | Reserve stock from `GWH_TJ_ST` per strategy | MVP |
| FR-WE-004 | Replenishment Pre-check | Trigger replen if forward-pick short | MVP |
| FR-WE-005 | Wave Release | Generate pick tasks, push to RF queue | MVP |
| FR-WE-006 | Wave Monitoring | Real-time progress dashboard | MVP |
| FR-WE-007 | Wave Modification | Add / remove orders before release | P2 |
| FR-WE-008 | Short Allocation | Handle partial / short with reason codes | MVP |
| FR-WE-009 | Wave Freeze / Unfreeze | Pause wave temporarily | P2 |
| FR-WE-010 | Wave Cancellation | Cancel with rollback | MVP |
| FR-WE-011 | Wave Completion | Auto-complete once all tasks done | MVP |
| FR-WE-012 | Exception Logging | Log to `GWH_TJ_WV_EXCP` | MVP |
| **FR-WE-013** | **Full-LPN-First Allocation** | **Prefer full LPNs to minimise pick touches** | **MVP** |
| **FR-WE-014** | **Partial LPN Split** | **Split LPN when partial qty allocated; log `GWH_TJ_LPN_MS`** | **MVP** |

#### Wave Status Lifecycle (7)

| Code | Status | Description | Transitions |
|---|---|---|---|
| 01 | Planned | Wave created, awaiting allocation | → 02 |
| 02 | Allocated | Inventory reserved | → 03 / 07 |
| 03 | Released | Tasks dispatched | → 04 / 07 |
| 04 | InProgress | Pickers executing | → 05 / 06 |
| 05 | Complete | All tasks confirmed | Final |
| 06 | Short Complete | Closed with shortages | Final |
| 07 | Cancelled | Cancelled by user | Final |

#### Business Rules (10)

| ID | Rule | Description |
|---|---|---|
| BR-WE-001 | No Duplicate Wave | One order belongs to at most one open wave |
| BR-WE-002 | Full Allocation Required | Cannot release with un-allocated lines unless short-allowed |
| BR-WE-003 | Reservation Lock | Allocated stock locked from other waves |
| BR-WE-004 | Cancel Rolls Back | Cancellation releases reservation |
| BR-WE-005 | Pre-Release Mod Only | Modification only allowed before status 03 |
| BR-WE-006 | Auto-Complete | Wave closes when all tasks at 05 |
| BR-WE-007 | Short Reason Mandatory | `SHORT_QTY > 0` requires `RSN_COD` |
| BR-WE-008 | FEFO Enforcement | Shorter-expiry inventory allocated first |
| BR-WE-009 | LPN Atomicity | Allocate full LPN before splitting |
| BR-WE-010 | Exception Visible | Every exception visible to supervisor dashboard |

#### Database Tables (5)

| Table | Type | Purpose | Key Columns |
|---|---|---|---|
| `GWH_TJ_WV_H` | Txn | Wave Header | WV_NUM, WVPF_COD, WV_STS, TOT_ORD_CNT, RLS_DT, CMPL_DT |
| `GWH_TJ_WV_D` | Txn | Wave Detail | WV_NUM, WV_LIN_NUM, SP_NUM, PROD_COD, REQ_QTY, ALLOC_QTY |
| `GWH_TJ_WV_ALLOC` | Txn | Allocation (LPN-aware) | WV_NUM, ALLOC_SEQ, **LPN_ID**, FR_LOC_COD, PROD_COD, ALLOC_QTY |
| `GWH_TJ_WV_EXCP` | Txn | Exception Log | WV_NUM, EXCP_SEQ, EXCP_TYP, PROD_COD, SHORT_QTY, RSN_COD |
| `GWH_TH_WV_HIS` | History | Wave Archive | WV_NUM, HIS_SEQ, FR_STS, TO_STS, ACT_DT, ACT_USR |

#### REST API Endpoints (10)
- `POST /api/v5/waves` — create wave from eligible orders
- `POST /api/v5/waves/simulate` — preview wave composition
- `GET  /api/v5/waves/{id}` — retrieve wave detail
- `POST /api/v5/waves/{id}/allocate` — execute allocation
- `POST /api/v5/waves/{id}/release` — release → triggers task generation
- `POST /api/v5/waves/{id}/freeze` — pause wave
- `POST /api/v5/waves/{id}/unfreeze` — resume wave
- `POST /api/v5/waves/{id}/cancel` — cancel with rollback
- `GET  /api/v5/waves/monitor` — real-time dashboard
- `GET  /api/v5/waves/{id}/exceptions` — exception list

#### Allocation Algorithm — Full-LPN-First Strategy

```python
def allocate_wave_line(line, wave):
    remaining = line.req_qty
    candidates = query_lpns(
        product=line.prod_cod,
        sort=["full_lpn DESC", "expiry ASC", "receipt_date ASC"]  # FEFO/FIFO
    )
    for lpn in candidates:
        if lpn.qty >= remaining:
            if lpn.qty == remaining:
                allocate_full_lpn(lpn, line, wave)          # ideal case
            else:
                new_lpn = split_lpn(lpn, qty=remaining)     # logs GWH_TJ_LPN_MS
                allocate_full_lpn(new_lpn, line, wave)
            remaining = 0
            break
        else:
            allocate_full_lpn(lpn, line, wave)
            remaining -= lpn.qty
    if remaining > 0:
        write_row("GWH_TJ_WV_EXCP",
                  wave_num=wave.id, prod_cod=line.prod_cod,
                  short_qty=remaining, rsn_cod="SHORT")
```

#### UI Screens
- **Wave Planning** — order pool + profile picker + simulation
- **Wave Monitor** — real-time dashboard with progress bars
- **Wave Detail** — order lines, allocation grid, pick progress
- **Allocation Review** — short items, alternate locations, override

#### Acceptance Criteria
- ✅ Wave of 50 orders allocated and released in < 5 sec (SC-01)
- ✅ Zero double-allocation under 100 concurrent operations (SC-02)
- ✅ Full-LPN-first allocation logs non-null `LPN_ID` in `GWH_TJ_WV_ALLOC`
- ✅ Cancellation cascades to release reservations and cancel downstream tasks

---

### 8.3 Module 3 — Task Management (LPN-anchored)

**Purpose:** Directed-work engine that replaces manual supervisor assignment with system-pushed, LPN-anchored tasks.

#### Functional Requirements (15)

| ID | Requirement | Description | Priority |
|---|---|---|---|
| FR-TM-001 | Task Generation | Auto-generate tasks from waves/inbound/replen | MVP |
| FR-TM-002 | Task Types | Pick / Putaway / Move / Count / Replen | MVP |
| FR-TM-003 | Priority Calculation | Score by SLA, customer, age | MVP |
| FR-TM-004 | Auto-Assignment | Push to best-match worker | MVP |
| FR-TM-005 | Zone Assignment | Workers tied to allowed zones | MVP |
| FR-TM-006 | Skill-based Assignment | Forklift / heavy / temp-zone | P2 |
| FR-TM-007 | Task Interleaving | Chain complementary tasks (5 rules) | MVP |
| FR-TM-008 | Task Queue Push | Push to RF device near-real-time | MVP |
| FR-TM-009 | RF Task Dispatch | Workers retrieve on RF | MVP |
| FR-TM-010 | Execution Tracking | Step-by-step scan tracking | MVP |
| FR-TM-011 | Task Confirmation | Validate LPN-LOC-SKU-QTY scan | MVP |
| FR-TM-012 | Exception Handling | Short / damage / wrong-LPN | MVP |
| FR-TM-013 | Task Split / Merge | Supervisor manual control | P2 |
| FR-TM-014 | Supervisor Controls | Reassign / cancel / hold | MVP |
| FR-TM-015 | Performance Tracking | Picks/hr, idle time, errors | MVP |

#### Task Status Lifecycle (8)

| Code | Status | Description | Transitions |
|---|---|---|---|
| 01 | Created | Task generated | → 02 |
| 02 | Queued | Waiting in queue | → 03 / 07 |
| 03 | Assigned | Pushed to worker | → 04 / 07 |
| 04 | InProgress | Worker scanning | → 05 / 06 / 08 |
| 05 | Complete | Confirmed | Final |
| 06 | Exception | Short / damage / wrong | → 02 / 07 |
| 07 | Cancelled | Cancelled | Final |
| 08 | Suspended | Worker break | → 04 |

#### Task Interleaving (5 rules)

| From | To | Logic | Benefit |
|---|---|---|---|
| Pick | Putaway | Worker in zone A passes a reserve | Eliminates empty return |
| Putaway | Pick | After putaway, worker near a queued pick | Combines inbound/outbound |
| Pick | Replenish | Worker at low-stock zone triggers replen | Immediate replenishment |
| Replenish | Pick | Pick task ready for that location | Zero wait time |
| Count | Pick | After cycle count, picks nearby | Maximises utilisation |

> **Reduces empty walks by 30–40%.**

#### Business Rules (12)

| ID | Rule | Description |
|---|---|---|
| BR-TM-001 | One Active per Worker | Only one InProgress task per worker |
| BR-TM-002 | Zone Constraint | Worker only gets tasks in allowed zones |
| BR-TM-003 | Skill Match | Skill-required tasks need matching skill |
| BR-TM-004 | Priority Score | Lower number = higher priority |
| BR-TM-005 | LPN Required for Pick | Pick tasks must reference `LPN_ID` |
| BR-TM-006 | Scan Sequence | Location scanned before LPN |
| BR-TM-007 | Qty Validation | Picked qty must equal task qty unless exception |
| BR-TM-008 | Exception Flow | Exception suspends task until supervisor decision |
| BR-TM-009 | Auto-Reassign Idle | Tasks idle > N min auto-reassign |
| BR-TM-010 | Performance Logged | Every completion logs to `GWH_TJ_TSK_PERF` |
| BR-TM-011 | Interleave Optional | Worker can decline suggestion |
| BR-TM-012 | Supervisor Override | Can re-assign any non-final task |

#### Database Tables (11)

| Table | Type | Purpose | Key Columns |
|---|---|---|---|
| `GWH_TM_TSKP` | Master | Task Type / Priority Master | TSK_TYP, DFLT_PRTY, SLA_MIN |
| `GWH_TM_TSKZ` | Master | Zone Master | ZONE_COD, ZONE_NAM, ZONE_TYP |
| `GWH_TM_WRKR` | Master | Worker Master | WRKR_ID, WRKR_NAM, STATUS |
| `GWH_TM_WRKR_ZN` | Master | Worker × Zone | WRKR_ID, ZONE_COD |
| `GWH_TM_WRKR_SK` | Master | Worker × Skill | WRKR_ID, SKL_COD |
| `GWH_TM_TILV` | Master | Interleaving Rules | TILV_SEQ, FR_TYP, TO_TYP, RULE_TXT |
| `GWH_TJ_TSK_H` | Txn | Task Header | TSK_NUM, TSK_TYP, WV_NUM, **LPN_ID**, WRKR_ID, TSK_STS, PRTY |
| `GWH_TJ_TSK_D` | Txn | Task Detail | TSK_NUM, TSK_LIN, **LPN_ID**, FR_LOC, TO_LOC, PROD_COD, QTY |
| `GWH_TJ_TSK_Q` | Txn | Task Queue | TSK_NUM, QUEUE_DT, PRTY_SCORE |
| `GWH_TH_TSK_HIS` | History | Task Archive | TSK_NUM, HIS_SEQ, FR_STS, TO_STS |
| `GWH_TJ_TSK_PERF` | Txn | Performance Metrics | WRKR_ID, SHIFT_DT, TSKS_DONE, AVG_TIME_SEC |

#### REST API Endpoints (10)
- `GET  /api/v5/tasks/queue/{workerId}` — fetch worker queue
- `POST /api/v5/tasks/{id}/accept` — accept task
- `POST /api/v5/tasks/{id}/start` — start execution
- `POST /api/v5/tasks/{id}/confirm` — body: `{ lpnId, locationId, qtyPicked, scans[] }`
- `POST /api/v5/tasks/{id}/exception` — body: `{ reasonCode, photo, comments }`
- `POST /api/v5/tasks/{id}/suspend` — suspend
- `POST /api/v5/tasks/{id}/resume` — resume
- `POST /api/v5/tasks/{id}/reassign` — supervisor only
- `GET  /api/v5/tasks/performance/{workerId}?from=&to=` — performance KPIs
- `GET  /api/v5/tasks/monitor` — supervisor dashboard

#### Priority Calculation

```python
def calculate_priority(task):
    score = base_priority(task.type)                          # config: 100, 50, 200 ...
    score += sla_urgency(task.sla_remaining_hours) * 100      # negative = more urgent
    score += customer_tier_bonus(task.customer_id)            # platinum: -50
    score += wave_priority(task.wave_id)                      # inherits wave priority
    score += age_penalty(task.created_at)                     # older = lower score
    return score                                              # lower = higher priority
```

#### Interleaving Algorithm

```python
def on_task_complete(worker, completed_task):
    current_loc = completed_task.to_loc or worker.last_loc
    candidates = fetch_queue(worker.id)
    for c in candidates:
        for rule in interleave_rules(from_type=completed_task.type):
            if rule.to_type == c.type:
                distance = location_distance(current_loc, c.from_loc)
                if distance <= rule.max_dist_m:
                    offer_to_worker(worker, c)
                    return
    fetch_next_priority_task(worker)
```

#### UI Screens
- **Task Monitor** — real-time task tracking
- **Worker Assignment** — worker–zone–skill drag-and-drop
- **Task Queue View** — per-worker queue depth + reorder
- **Performance Dashboard** — picks/hr heat map
- **Supervisor Control** — bulk reassign / cancel / hold

#### Acceptance Criteria
- ✅ Task push to RF < 2 sec (SC-03)
- ✅ Pick confirmation requires valid LPN scan
- ✅ Interleaving reduces empty walks by ≥ 30% in load test
- ✅ Performance metrics written to `GWH_TJ_TSK_PERF` every completion
- ✅ Supervisor can reassign any non-final task

---

### 8.4 Module 4 — RF / Mobile Interface

**Architecture:** Progressive Web App built with React 18 + TypeScript, served via HTTPS to existing HHT/handheld scanner devices. Service Worker for offline cache; IndexedDB for local task queue; JWT for auth; Web Push API for new-task notifications.

#### Screen Specifications (6)

| Screen | Purpose | Key Elements | Actions |
|---|---|---|---|
| Login | Authenticate worker | Worker ID, PIN, device-id auto | Login / Forgot PIN |
| Task List | Show queued tasks | Task type, priority, LPN, location | Accept / Skip |
| Task Execution | Scan-confirm flow | LOC → LPN → SKU → QTY → DEST | Scan / Confirm / Exception |
| Exception Report | Report problem | Reason code, photo, comments | Submit |
| Supervisor Monitor | Real-time dashboard | Workers / tasks / WIP | Reassign / Cancel |
| Settings | Device / camera config | Scanner type, sound, language | Save |

#### Scan Workflow (6 steps)
1. **Scan location barcode** → validate against `GWH_TM_LOC`
2. **Scan LPN barcode** → validate against `GWH_TJ_LPN` (must be status 500 or 600)
3. **Scan SKU barcode** → validate against `GWH_TM_PBAR`
4. **Enter quantity** → validate ≤ task `req_qty`
5. **Scan destination location** → validate against `GWH_TM_LOC`
6. **Submit confirmation** → POST `/api/v5/tasks/{id}/confirm`

#### Offline Mode
- Service Worker caches application shell + active task payload
- IndexedDB stores scan events queued for sync
- Max 1 active task held offline
- Visual online/offline indicator
- Auto-sync on reconnect

#### AGS Integration

| AGS Table | Direction | Purpose |
|---|---|---|
| `AGS_TM_DEVC` | Read | Device registration & configuration |
| `AGS_TM_MENU` | Read | Navigation menu definitions |
| `AGS_TJ_SCAN` | Write | Per-scan audit transaction log |
| `AGS_TJ_SESS` | Write | Active session tracking |

#### Security
- JWT bearer tokens, 8-hour expiry (matches shift length)
- RBAC: `PICKER`, `SUPERVISOR`, `MANAGER`
- 5-minute inactivity auto-lock
- PIN quick-login for device sharing
- All API calls over TLS 1.3

#### Acceptance Criteria
- ✅ All 6 screens functional on Android & Windows CE browser
- ✅ Camera + Bluetooth/USB scanner both work for barcode input
- ✅ Offline mode persists 1 active task and auto-syncs on reconnect
- ✅ JWT auth + 5-min auto-lock validated by security review

---

### 8.5 Module 5 — LPN Master & Lifecycle

**Definition:** A License Plate Number (LPN) is a unique scannable identifier attached to a unit-load (pallet, case, or tote). The LPN — not the SKU — becomes the primary object that is received, putaway, moved, picked, packed, and shipped. One scan = full LPN context.

#### LPN Status Lifecycle (9 statuses)

| Status Code | Meaning |
|---|---|
| 100 | Created (announced via ASN) |
| 200 | Received (physically scanned at dock) |
| 300 | In QC / Hold |
| 400 | Putaway in progress |
| 500 | Stored / Available |
| 600 | Allocated to outbound |
| 700 | Picked |
| 800 | Packed / Loaded |
| 900 | Shipped / Closed |

#### LPN Hierarchy
**Pallet LPN (parent)** → **Case LPN (child)** → **Tote / Inner LPN (grandchild)**. Aggregate scans of children roll up to parent.

#### Barcode Standards
- **SSCC-18** — GS1 18-digit Serial Shipping Container Code
- **GS1-128** — variable-length GS1 barcode with embedded AIs
- **Custom** — `NX-LPN-yyyymmdd-####` for internal use
- **QR code** — fallback for damaged barcodes

#### Existing LPN Tables (6)

| Table | Purpose |
|---|---|
| `GWH_TJ_LPN` | LPN header (one row per LPN) |
| `GWH_TJ_LPN_D` | LPN detail (SKU × qty per LPN) |
| `GWH_TM_LPN_CFG` | LPN numbering & configuration |
| `GWH_TM_LPPA_PAIR` | Parent-child pairing rules |
| `GWH_TM_LPR` | Label print rules |
| `GWH_TJ_LPR_HIS` | Label print history |

#### New LPN Tables (3)

| Table | Purpose |
|---|---|
| `GWH_TJ_LPN_HIS` | Event-sourced lifecycle history (every status change) |
| `GWH_TJ_LPN_MS` | Merge / split audit log |
| `GWH_TM_LPN_LBL` | Extended label template master (SSCC-18 + GS1-128) |

#### DDL — ALTER & CREATE

```sql
-- Extend LPN master with 10 columns
ALTER TABLE GWH_TJ_LPN ADD (
  mixed_flag           CHAR(1)      DEFAULT 'N',
  length_cm            NUMBER(8,2),
  width_cm             NUMBER(8,2),
  height_cm            NUMBER(8,2),
  weight_kg            NUMBER(10,3),
  volume_m3            NUMBER(10,4),
  qc_status            VARCHAR2(20) DEFAULT 'OK',
  parent_lpn_id        VARCHAR2(30),
  current_location_id  VARCHAR2(20),
  status_code          NUMBER(3)    DEFAULT 100
);

-- Lifecycle history (event-sourced)
CREATE TABLE GWH_TJ_LPN_HIS (
  lpn_id       VARCHAR2(30) NOT NULL,
  seq_no       NUMBER(10)   NOT NULL,
  status_from  NUMBER(3),
  status_to    NUMBER(3)    NOT NULL,
  from_loc     VARCHAR2(20),
  to_loc       VARCHAR2(20),
  qty_change   NUMBER(12,3),
  user_id      VARCHAR2(30) NOT NULL,
  event_ts     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  ref_doc_no   VARCHAR2(30),
  remarks      VARCHAR2(500),
  CONSTRAINT PK_LPN_HIS PRIMARY KEY (lpn_id, seq_no)
);

-- Merge / split audit log
CREATE TABLE GWH_TJ_LPN_MS (
  event_id      VARCHAR2(30) PRIMARY KEY,
  event_type    VARCHAR2(10) NOT NULL,
  source_lpn_id VARCHAR2(30) NOT NULL,
  target_lpn_id VARCHAR2(30) NOT NULL,
  qty           NUMBER(12,3),
  user_id       VARCHAR2(30) NOT NULL,
  event_ts      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
```

#### REST API Endpoints (8)
- `POST /api/v5/lpn` — generate new LPN_ID
- `GET  /api/v5/lpn/{id}` — get LPN detail
- `GET  /api/v5/lpn/{id}/history` — read `GWH_TJ_LPN_HIS`
- `POST /api/v5/lpn/{id}/move` — change current location
- `POST /api/v5/lpn/merge` — body: `{ sourceLpnIds[], targetLpnId }`
- `POST /api/v5/lpn/split` — body: `{ sourceLpnId, qty, newLpnId }`
- `POST /api/v5/lpn/{id}/print-label` — body: `{ templateCode }`
- `GET  /api/v5/lpn/inventory?location=&sku=` — LPN-anchored stock inquiry

#### Auto-Generation Algorithm

```python
def generate_lpn_id():
    prefix = "NX-LPN"
    date_part = datetime.now().strftime("%Y%m%d")
    seq = next_sequence("LPN_SEQ")          # Oracle sequence
    return f"{prefix}-{date_part}-{seq:04d}"
# Example: NX-LPN-20260701-0042
```

#### Acceptance Criteria
- ✅ All 9 statuses transition through `GWH_TJ_LPN_HIS` (SC-07)
- ✅ Merge / split writes `GWH_TJ_LPN_MS` with non-null event_id
- ✅ Nested LPN supported (parent_lpn_id populated, queries roll up)
- ✅ SSCC-18 label generation validates GS1 check digit

---

## 9. Unified Function List — 92 Functions

### 9.1 Summary by Module

| Module | Count | ID Range |
|---|---|---|
| Wave Profile | 10 | WP-001..010 |
| Wave Execution | 14 | WE-001..014 |
| Task Management | 15 | TM-001..015 |
| RF Mobile | 8 | RF-001..008 |
| Inbound | 8 | INB-001..008 |
| Putaway | 6 | PUT-001..006 |
| Inventory | 10 | INV-001..010 |
| Pack / Ship | 8 | SHP-001..008 |
| Returns | 5 | RET-001..005 |
| Yard | 4 | YRD-001..004 |
| Cross-LPN | 4 | LPN-001..004 |
| **Total** | **92** | — |

### 9.2 Detailed Function List

| # | Module | Function ID | Function Name | LPN Impact |
|---|---|---|---|---|
| 1 | Wave Profile | WP-001 | Create Wave Profile | Reads LPN config |
| 2 | Wave Profile | WP-002 | Define Grouping Rules | — |
| 3 | Wave Profile | WP-003 | Define Release Rules | — |
| 4 | Wave Profile | WP-004 | Set Capacity Constraints | — |
| 5 | Wave Profile | WP-005 | Bind Allocation Strategy | Reads LPN dims |
| 6 | Wave Profile | WP-006 | Set Pick Method | — |
| 7 | Wave Profile | WP-007 | Configure Auto-Schedule | — |
| 8 | Wave Profile | WP-008 | Clone Profile | — |
| 9 | Wave Profile | WP-009 | Profile Versioning | — |
| 10 | Wave Profile | WP-010 | Activate/Deactivate Profile | — |
| 11 | Wave Execution | WE-001 | Wave Creation | Pre-reads LPN inventory |
| 12 | Wave Execution | WE-002 | Wave Simulation | Previews LPN allocation |
| 13 | Wave Execution | WE-003 | Inventory Allocation | Reserves LPN status→600 |
| 14 | Wave Execution | WE-004 | Replenishment Pre-check | Triggers LPN move |
| 15 | Wave Execution | WE-005 | Wave Release | Creates LPN-anchored tasks |
| 16 | Wave Execution | WE-006 | Wave Monitoring | Aggregates LPN status |
| 17 | Wave Execution | WE-007 | Wave Modification | Re-allocates LPN |
| 18 | Wave Execution | WE-008 | Short Allocation | Logs LPN short |
| 19 | Wave Execution | WE-009 | Wave Freeze/Unfreeze | Holds LPN reservation |
| 20 | Wave Execution | WE-010 | Wave Cancellation | Releases LPN status→500 |
| 21 | Wave Execution | WE-011 | Wave Completion | Closes LPN allocation |
| 22 | Wave Execution | WE-012 | Exception Logging | LPN exception per row |
| 23 | Wave Execution | WE-013 | Full-LPN-First Allocation | Prefers full LPN |
| 24 | Wave Execution | WE-014 | Partial LPN Split | Splits LPN, logs MS |
| 25 | Task Mgmt | TM-001 | Task Generation | Creates LPN-anchored tasks |
| 26 | Task Mgmt | TM-002 | Task Types Support | Pick/Putaway/Move/Count by LPN |
| 27 | Task Mgmt | TM-003 | Priority Calculation | — |
| 28 | Task Mgmt | TM-004 | Auto-Assignment | — |
| 29 | Task Mgmt | TM-005 | Zone Assignment | — |
| 30 | Task Mgmt | TM-006 | Skill-based Assignment | — |
| 31 | Task Mgmt | TM-007 | Task Interleaving | Optimises LPN proximity |
| 32 | Task Mgmt | TM-008 | Task Queue Push | Pushes LPN tasks to RF |
| 33 | Task Mgmt | TM-009 | RF Task Dispatch | Worker receives LPN task |
| 34 | Task Mgmt | TM-010 | Execution Tracking | Tracks LPN scan times |
| 35 | Task Mgmt | TM-011 | Task Confirmation | Validates LPN scan |
| 36 | Task Mgmt | TM-012 | Exception Handling | LPN short/damage/wrong |
| 37 | Task Mgmt | TM-013 | Task Split/Merge | — |
| 38 | Task Mgmt | TM-014 | Supervisor Controls | Reassign LPN tasks |
| 39 | Task Mgmt | TM-015 | Performance Tracking | Picks/hr by LPN |
| 40 | RF Mobile | RF-001 | RF Login | — |
| 41 | RF Mobile | RF-002 | RF Task List | LPN tasks sorted |
| 42 | RF Mobile | RF-003 | RF Task Execution | Scan-confirm LPN |
| 43 | RF Mobile | RF-004 | RF Scan Validation | LPN scan vs `GWH_TJ_LPN` |
| 44 | RF Mobile | RF-005 | RF Exception Report | Photo + LPN |
| 45 | RF Mobile | RF-006 | RF Supervisor Monitor | LPN-level visibility |
| 46 | RF Mobile | RF-007 | RF Offline Sync | Cache LPN scans |
| 47 | RF Mobile | RF-008 | RF Settings | Camera/wedge config |
| 48 | Inbound | INB-001 | ASN Receipt | Pre-creates LPN status=100 |
| 49 | Inbound | INB-002 | Goods Receipt Scan | Updates LPN status=200 |
| 50 | Inbound | INB-003 | LPN Auto-Generation | Generates new LPN_ID |
| 51 | Inbound | INB-004 | QC Hold/Release | LPN status=300/500 |
| 52 | Inbound | INB-005 | Cross-Dock Decision | LPN flagged cross-dock |
| 53 | Inbound | INB-006 | Receipt Reconciliation | LPN vs ASN match |
| 54 | Inbound | INB-007 | Damage Recording | LPN flagged damaged |
| 55 | Inbound | INB-008 | ASN-vs-Receipt Variance | LPN variance report |
| 56 | Putaway | PUT-001 | Directed Putaway Engine | Generates LPN putaway task |
| 57 | Putaway | PUT-002 | Putaway Task Confirm | LPN status=500 |
| 58 | Putaway | PUT-003 | LPN Move | Updates LPN current_location |
| 59 | Putaway | PUT-004 | Bulk Putaway | Multi-LPN single trip |
| 60 | Putaway | PUT-005 | Putaway Exception | LPN re-route |
| 61 | Putaway | PUT-006 | Putaway History | Writes `GWH_TJ_LPN_HIS` |
| 62 | Inventory | INV-001 | LPN Inventory Inquiry | Anchored by LPN_ID |
| 63 | Inventory | INV-002 | SKU Inventory Inquiry | Roll-up by LPN |
| 64 | Inventory | INV-003 | Location Inventory Inquiry | Roll-up by LPN |
| 65 | Inventory | INV-004 | Inventory Adjustment | Adjust LPN qty |
| 66 | Inventory | INV-005 | Cycle Count Task | Random LPN cycle |
| 67 | Inventory | INV-006 | LPN Cycle Count | Full-LPN cycle |
| 68 | Inventory | INV-007 | Stock Reservation | Reserves LPN |
| 69 | Inventory | INV-008 | Stock Status Change | LPN status change |
| 70 | Inventory | INV-009 | Inventory Snapshot | LPN snapshot |
| 71 | Inventory | INV-010 | Inventory Aging Report | Days-since-receipt by LPN |
| 72 | Pack/Ship | SHP-001 | Carton Selection | LPN-level carton |
| 73 | Pack/Ship | SHP-002 | Pack Consolidation (Parent LPN) | Builds parent LPN |
| 74 | Pack/Ship | SHP-003 | Shipping Label (SSCC-18) | SSCC-18 label per LPN |
| 75 | Pack/Ship | SHP-004 | Manifest Generation | LPN list per manifest |
| 76 | Pack/Ship | SHP-005 | Trailer Load | LPN status=800 |
| 77 | Pack/Ship | SHP-006 | Shipment Close | LPN status=900 |
| 78 | Pack/Ship | SHP-007 | BOL Generation | LPN ref on BOL |
| 79 | Pack/Ship | SHP-008 | Outbound EDI/API | LPN data outbound |
| 80 | Returns | RET-001 | Return Receipt | Creates new LPN |
| 81 | Returns | RET-002 | RMA Disposition | LPN disposition |
| 82 | Returns | RET-003 | Re-stock to LPN | New LPN to stock=500 |
| 83 | Returns | RET-004 | Repair Workflow | LPN repair status |
| 84 | Returns | RET-005 | Scrap/Disposal | LPN status=scrap |
| 85 | Yard | YRD-001 | Trailer Check-in | LPN list per trailer |
| 86 | Yard | YRD-002 | Dock Door Assignment | LPN flow to dock |
| 87 | Yard | YRD-003 | Trailer Check-out | LPN list shipped |
| 88 | Yard | YRD-004 | Yard Move | LPN trailer change |
| 89 | Cross-LPN | LPN-001 | LPN History Inquiry | Reads `GWH_TJ_LPN_HIS` |
| 90 | Cross-LPN | LPN-002 | LPN Merge | Writes `GWH_TJ_LPN_MS` |
| 91 | Cross-LPN | LPN-003 | LPN Split | Writes `GWH_TJ_LPN_MS` |
| 92 | Cross-LPN | LPN-004 | LPN Print/Re-print | Updates `GWH_TJ_LPR_HIS` |

---

## 10. Combined Database Design

### 10.1 Summary
**27 new tables + 5 altered tables.** All new tables follow `GWH_` naming convention with `CPNY_COD` + `WHS_COD` partitioning ready.

### 10.2 New Tables (27)

| # | Table | Type | Module | Purpose |
|---|---|---|---|---|
| 1 | `GWH_TM_WVPF` | Master | Wave Profile | Wave Profile master |
| 2 | `GWH_TM_WVGR` | Master | Wave Profile | Grouping rules |
| 3 | `GWH_TM_WVRL` | Master | Wave Profile | Release rules |
| 4 | `GWH_TM_WVCA` | Master | Wave Profile | Capacity constraints |
| 5 | `GWH_TM_WVAL` | Master | Wave Profile | Allocation strategy |
| 6 | `GWH_TM_WVSC` | Master | Wave Profile | Wave schedule |
| 7 | `GWH_TJ_WV_H` | Txn | Wave Execution | Wave header |
| 8 | `GWH_TJ_WV_D` | Txn | Wave Execution | Wave detail |
| 9 | `GWH_TJ_WV_ALLOC` | Txn | Wave Execution | Allocation (LPN-aware) |
| 10 | `GWH_TJ_WV_EXCP` | Txn | Wave Execution | Exception log |
| 11 | `GWH_TH_WV_HIS` | History | Wave Execution | Wave archive |
| 12 | `GWH_TM_TSKP` | Master | Task Mgmt | Task type/priority master |
| 13 | `GWH_TM_TSKZ` | Master | Task Mgmt | Zone master |
| 14 | `GWH_TM_WRKR` | Master | Task Mgmt | Worker master |
| 15 | `GWH_TM_WRKR_ZN` | Master | Task Mgmt | Worker × zone |
| 16 | `GWH_TM_WRKR_SK` | Master | Task Mgmt | Worker × skill |
| 17 | `GWH_TM_TILV` | Master | Task Mgmt | Interleaving rules |
| 18 | `GWH_TJ_TSK_H` | Txn | Task Mgmt | Task header (+LPN_ID) |
| 19 | `GWH_TJ_TSK_D` | Txn | Task Mgmt | Task detail (+LPN_ID) |
| 20 | `GWH_TJ_TSK_Q` | Txn | Task Mgmt | Task queue |
| 21 | `GWH_TH_TSK_HIS` | History | Task Mgmt | Task archive |
| 22 | `GWH_TJ_TSK_PERF` | Txn | Task Mgmt | Performance metrics |
| 23 | `GWH_TJ_LPN_HIS` | History | LPN | Lifecycle history |
| 24 | `GWH_TJ_LPN_MS` | Txn | LPN | Merge/split audit log |
| 25 | `GWH_TM_LPN_LBL` | Master | LPN | Label template |
| 26 | `GWH_TM_LPN_DIM_CFG` | Master | LPN | Dimension capture config |
| 27 | `GWH_TJ_LPN_AUD` | History | LPN | Audit (SOX/customs) |

### 10.3 Altered Tables (5)

| Table | Change | Reason |
|---|---|---|
| `GWH_TJ_LPN` | +10 cols (mixed_flag, dims, qc_status, parent_lpn_id, current_location_id, status_code) | Enable LPN spine |
| `GWH_TJ_INV` | +`LPN_ID` + `IDX_INV_LPN` | Anchor inventory to LPN |
| `GWH_TJ_PCK` | +`LPN_ID` + `IDX_PCK_LPN` | Full-LPN pick optimisation |
| `GWH_TJ_PUT` | +`LPN_ID` + `IDX_PUT_LPN` | LPN-directed putaway |
| `GWH_TJ_TSK_D` | +`LPN_ID` + `IDX_TSK_LPN` | LPN-anchored task execution |

### 10.4 Indexes (14)

| Index | Table | Columns | Purpose |
|---|---|---|---|
| `IDX_INV_LPN` | `GWH_TJ_INV` | LPN_ID | LPN-anchored inventory lookup |
| `IDX_PCK_LPN` | `GWH_TJ_PCK` | LPN_ID | Pick task LPN lookup |
| `IDX_PUT_LPN` | `GWH_TJ_PUT` | LPN_ID | Putaway task LPN lookup |
| `IDX_TSK_LPN` | `GWH_TJ_TSK_D` | LPN_ID | Task detail LPN lookup |
| `IDX_WV_ALLOC_LPN` | `GWH_TJ_WV_ALLOC` | LPN_ID | Wave allocation LPN lookup |
| `IDX_LPN_PARENT` | `GWH_TJ_LPN` | parent_lpn_id | Nested LPN parent lookup |
| `IDX_LPN_STS` | `GWH_TJ_LPN` | status_code | Status filter |
| `IDX_LPN_HIS_TS` | `GWH_TJ_LPN_HIS` | event_ts | Audit by timestamp |
| `IDX_LPN_MS_SRC` | `GWH_TJ_LPN_MS` | source_lpn_id | Merge/split source lookup |
| `IDX_WV_H_STS` | `GWH_TJ_WV_H` | WV_STS | Wave status filter |
| `IDX_WV_D_SP` | `GWH_TJ_WV_D` | SP_NUM | Shipping order lookup |
| `IDX_TSK_H_STS` | `GWH_TJ_TSK_H` | TSK_STS | Task status filter |
| `IDX_TSK_H_WV` | `GWH_TJ_TSK_H` | WV_NUM | Tasks by wave |
| `IDX_TSK_Q_PRTY` | `GWH_TJ_TSK_Q` | PRTY_SCORE DESC | Queue priority order |

### 10.5 Migration Script
Companion file: `NX_GLOW_WMS_LPN_v5_Migration.sql` — idempotent Oracle 19c PL/SQL with verification + commented rollback block.

---

## 11. End-to-End Process Flow

```text
ASN  -->  LPN created (100)
            |
            v
     Goods Receipt scan (200)
            |
            v
      QC routing? --Yes--> (300) Hold --> Release --+
            | No                                    |
            v                                       v
      Directed Putaway (400) <---------------------+
            |
            v
      Stored / Available (500) <-- LPN-anchored Inventory
            |
            v
      Wave creation <-- Wave Profile rules
            |
            v
      Allocation (full-LPN-first / FIFO / FEFO) (600)
            |
            v
      Task generation --> Task Queue --> RF Mobile
            |
            v
      Pick confirm (700) -- LPN scan validated
            |
            v
      Pack / Consolidate (800) -- Parent LPN built
            |
            v
      Ship / Manifest close (900) -- SSCC-18 label
```

> Each transition writes one row to `GWH_TJ_LPN_HIS` for full SOX / customs audit trail.

---

## 12. 8-Phase Roadmap — 33-Week Critical Path

| Phase | Name | Duration | Key Deliverables | Dependencies |
|---|---|---|---|---|
| P1 | Foundation | 4 wks | DDL changes, LPN history, status enum | — |
| P2 | Wave Profile + Admin UI | 4 wks | Profile master, grouping rules, capacity | P1 |
| P3 | LPN Inbound + Putaway | 5 wks | Auto-gen LPN, ASN scan, directed putaway | P1 |
| P4 | LPN Inventory Anchor (parallel) | 4 wks | Inv FK, LPN queries, adjustments | P1 |
| P5 | Wave Execution (LPN-aware) | 6 wks | Full-LPN-first alloc, 5 strategies | P2, P4 |
| P6 | Task Mgmt + RF Mobile | 8 wks | Task engine, RF PWA, interleaving | P5 |
| P7 | Pack/Ship + Yard + Returns + Cycle | 6 wks | SSCC-18, manifests, RMA | P6 |
| P8 | Hypercare + KPI Dashboard | 4 wks | Go-live support, Power BI | P7 |

> **Critical path = 33 weeks.** P4 runs in parallel with P2/P3 saving ~8 weeks vs. 41-week sequential.

---

## 13. Expected Benefits & KPIs

| KPI | Baseline | Target | Improvement |
|---|---|---|---|
| Picker travel distance | Unoptimised | −30 to −40% | ↓ 35% |
| On-time ship rate | 82% | ≥ 95% | +13 pts |
| **Picks per hour** | 35/hr | 55/hr | **+57%** |
| Pick accuracy | 97% paper | 99.5% scan | ↑ 2.5 pts |
| **Inventory accuracy** | 95.0% | 99.5%+ | **+4.5 pts** |
| **Mis-ship rate** | 0.5% | 0.2% | **−60%** |
| Putaway productivity | baseline | +20% | ↑ 20% |
| Cycle count time | baseline | −40% | ↓ 40% |
| New worker training | 5-7 days | 1-2 days | −60% |
| Supervisor task assignment | 100% manual | 80% automated | ↑ Efficiency |

---

## 14. Risks & Mitigations

| # | Risk | Impact | Probability | Mitigation |
|---|---|---|---|---|
| R-01 | User adoption / change management | High | High | Phased rollout + operator training + champion users |
| R-02 | DB migration on 10-year legacy | High | Medium | Blue-green deploy + tested rollback + DBA review |
| R-03 | Barcode label printer capacity | Medium | Medium | Pre-procurement during P1 + SSCC-18 standardisation |
| R-04 | RF WAN reliability | Medium | Medium | PWA offline mode + IndexedDB cache + auto-sync |
| R-05 | AGS integration (CargoWise / SAP) | High | Low | Sandbox test with Nice-san + API contracts in P3 |
| R-06 | Inventory backfill of existing stock | High | Medium | Phased LPN-tag rollout zone-by-zone + reconciliation reports |

---

## 15. Quick Wins (4–8 weeks each)

1. **DB Indexes** — add `IDX_INV_LPN` and `IDX_PCK_LPN` for high-volume query relief
2. **Power BI Dashboard** — LPN throughput KPIs (receipts/day, putaways/day, ships/day, dwell time)
3. **Standardise Barcode Labels** — across the 6 NXSAO subsidiaries (SSCC-18 / GS1-128)
4. **Excel Reconciliation Tracker** — MAWB vs. invoice vs. receipt using existing AI extraction pipeline
5. **Text-to-SQL AI Agent** — deploy the Node.js + PostgreSQL + Ollama pilot for ad-hoc ops queries
6. **Mobile RF UI Refresh** — offline-capable PWA with near-production design

---

## 16. Acceptance Criteria & Definition of Done

- ✅ All **27 new tables** created in `NXGLOW` schema
- ✅ All **5 altered tables** have new `LPN_ID` columns + indexes
- ✅ All **14 supporting indexes** present
- ✅ All **8 success criteria** (SC-01..SC-08) met under load test
- ✅ All **92 functions** accessible via REST API or UI
- ✅ DDL migration script **re-runnable** (idempotent — verified by re-execution)
- ✅ Workshop deck **signed-off** by Ashish Dey, Ito Takeshi, Pravin Vaidya
- ✅ UAT **passed** by warehouse operations team in pilot warehouse
- ✅ Power BI hypercare dashboard **live** (KPIs from §13)

---

## Appendix A — Glossary

| Term | Definition |
|---|---|
| **Wave** | A group of shipping orders processed together for optimised picking |
| **Wave Profile** | Template defining rules for how orders group into waves |
| **Allocation** | Process of reserving inventory from stock for a wave |
| **FIFO** | First In First Out — allocation based on receipt date |
| **FEFO** | First Expiry First Out — allocation for perishable goods |
| **LIFO** | Last In First Out — used for stack-fed locations |
| **LPN** | License Plate Number — unique ID for a unit-load (pallet/case/tote) |
| **Nested LPN** | Parent/child LPN hierarchy (Pallet→Case→Tote) |
| **SSCC-18** | GS1 18-digit Serial Shipping Container Code |
| **GS1-128** | GS1 variable-length application-identifier barcode |
| **Directed Work** | System-assigned tasks pushed to worker devices |
| **Task Interleaving** | Chaining different task types to minimise empty travel |
| **RF** | Radio Frequency — handheld scanner/terminal |
| **HHT** | Hand-Held Terminal — mobile device for warehouse workers |
| **PWA** | Progressive Web App — web app with native-like capabilities |
| **AGS** | Agility System — NX-GLOW existing mobile/scanner subsystem |
| **Pick Path** | Optimised route through warehouse for picking |
| **Zone Picking** | Warehouse divided into zones with dedicated pickers |
| **Batch Picking** | Multiple orders picked simultaneously |
| **Short Pick** | Required quantity not available at pick location |
| **Cycle Count** | Periodic counting of subset of inventory |
| **Hypercare** | Post-go-live intensive support period |
| **MAWB** | Master Air Waybill |
| **Steerco** | Steering Committee |
| **RBAC** | Role-Based Access Control |
| **JWT** | JSON Web Token — auth token format |

---

## Appendix B — Reference Files

- `NX_GLOW_WMS_SDD_Combined_v5_Wave_Task_LPN.docx` — formatted 18-section SDD
- `NX_GLOW_WMS_Combined_Workshop_Deck_v5.pptx` — 20-slide stakeholder deck
- `NX_GLOW_WMS_LPN_v5_Migration.sql` — Oracle 19c idempotent DDL with rollback
- `WMS_SDD.docx` — original Wave Picking SDD v1.0 source
- `NX_GLOW_WMS_SDD_v4_LPN_Integrated 1.docx` — original LPN Integration SDD v4.0 source

---

## Appendix C — Sign-off Block

| | | |
|---|---|---|
| ________________________ | ________________________ | ________________________ |
| **Ashish Dey** | **Ito Takeshi** | **Pravin Vaidya** |
| Sponsor / Reviewer | Tech Lead | Solution Architect |
| Date: __________ | Date: __________ | Date: __________ |

---

## Appendix D — DDL Quick Reference

```sql
-- 4 ALTER + 4 INDEX (existing tables)
ALTER TABLE NXGLOW.GWH_TJ_INV ADD (LPN_ID VARCHAR2(30));
CREATE INDEX IDX_INV_LPN ON NXGLOW.GWH_TJ_INV(LPN_ID);

ALTER TABLE NXGLOW.GWH_TJ_PCK ADD (LPN_ID VARCHAR2(30));
CREATE INDEX IDX_PCK_LPN ON NXGLOW.GWH_TJ_PCK(LPN_ID);

ALTER TABLE NXGLOW.GWH_TJ_PUT ADD (LPN_ID VARCHAR2(30));
CREATE INDEX IDX_PUT_LPN ON NXGLOW.GWH_TJ_PUT(LPN_ID);

ALTER TABLE NXGLOW.GWH_TJ_TSK_D ADD (LPN_ID VARCHAR2(30));
CREATE INDEX IDX_TSK_LPN ON NXGLOW.GWH_TJ_TSK_D(LPN_ID);

-- GWH_TJ_LPN spine extension (+10 columns)
ALTER TABLE NXGLOW.GWH_TJ_LPN ADD (
  mixed_flag           CHAR(1)      DEFAULT 'N',
  length_cm            NUMBER(8,2),
  width_cm             NUMBER(8,2),
  height_cm            NUMBER(8,2),
  weight_kg            NUMBER(10,3),
  volume_m3            NUMBER(10,4),
  qc_status            VARCHAR2(20) DEFAULT 'OK',
  parent_lpn_id        VARCHAR2(30),
  current_location_id  VARCHAR2(20),
  status_code          NUMBER(3)    DEFAULT 100
);

-- For the 27 CREATE TABLE statements + seed data + verification + rollback,
-- see companion script: NX_GLOW_WMS_LPN_v5_Migration.sql
```

---

*— End of Master Development Specification — NX-GLOW WMS Combined v5.0 — Confidential — June 2026 —*