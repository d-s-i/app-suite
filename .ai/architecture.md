# app-suite architecture

Scope: the implemented MONOREPO-001 migration, not the future events/storage/reminders roadmap. Calendar is the only application. This document records current boundaries and intentional decisions; it does not introduce new product behavior or a speculative framework.

## Module and ownership graph

```text
Root Gradle settings / plugin versions / single wrapper
    registers :calendar and :design (no root runtime app)

:calendar  apps/calendar-app/
    └── implementation(project(":design"))
            :design  shared/design/
                └── api(androidx.compose.ui:ui:1.7.8)
```

There is one internal runtime-module edge, `:calendar → :design`. The library has no project dependency back to Calendar. Source inspection and resolved `debugRuntimeClasspath` reports confirm this graph; neither the old GitHub repository nor task files are runtime dependencies.

| Boundary | Owns | Must not own |
| --- | --- | --- |
| Repository root | Module registration, repository policy, pinned plugin versions, single Gradle wrapper | Calendar scripts, signing state or a second app wrapper |
| `apps/calendar-app/` | Android identity/version/permissions, activity, views, navigation/date logic, tests, local tooling and public signing pin | A copied shared palette or automatic sharing of future app data/keys |
| `shared/design/` | Existing reusable presentation tokens | App navigation, date calculations, clock/device access, storage, permissions or signing |
| `.ai/` | Shared original task IDs, approved Gherkin, QA, generated test entrypoints and evidence | Per-app duplicate task sources or production app data |
| `docs/` | Developer guides for consumption, independent apps, quality and publication | A generic multi-app framework or unapproved delivery automation |

See [design consumption](../docs/design.md) and [adding apps](../docs/adding-apps.md).

## Shared API and encapsulation

The intended cross-module API is `app.suite.design.SuiteColors`: five read-only `Color` values (`Ink`, `Muted`, `Accent`, `Line`, `Soft`) with the original Calendar palette values. Calendar imports this object as `CalendarColors`; that alias is not another definition.

Compose `Color` is intentional at a **presentation** boundary. `api` exposes the existing Compose UI dependency needed by public presentation types; this is not a domain-policy API or an attempt to make the library framework-independent. There are no Calendar types, mutable app state, callbacks, filesystem paths or credential DTOs in this API.

Typography, dimensions, date headers, time-axis measurement and calendar-specific gestures remain local. Extract further presentation only for a demonstrated approved use; do not share business logic merely because another app may need it. No second consumer was created or claimed tested.

## Calendar data/control flow

```text
Android lifecycle / resources / DateFormat
    → MainActivity.refreshEnvironment()
    → CalendarEnvironment(Clock, Locale, is24Hour)
    → CalendarApp: selected date, period, active view, drawer state
        → pure CalendarDates functions
        → MonthGrid / TimedCalendar / private TimeGrid
        → shared SuiteColors for presentation
```

- `MainActivity` is the device-input adapter. Environment values are plain clock/locale/format inputs; no activity/resource object is passed as an application policy contract.
- Date/range/format helpers use `java.time` and `Locale`, not Android IO. Existing JVM tests exercise leap days, consecutive date ranges, navigation, formats and capitalization without a real device.
- `CalendarView` is an app-local presentation enum, physically in `CalendarApp.kt`; date navigation references it inside the same app module. That pre-existing placement is not a reverse Gradle-module edge or a shared-domain API. A separate policy module/port layer is not justified by this migration.
- Lifecycle-driven ticking (`Handler`/`Looper`) stays at the timed UI edge. UI selection/drawer/scroll state is local, not exposed through the design singleton.
- No event repository, database, network client, account service or reminder engine exists yet. Their future interfaces belong to separately approved tasks, not placeholder abstractions here.

## Build, signing and tooling boundaries

- Root repositories/plugin versions are common; app signing/version settings remain in Calendar. The design module is a library, not an independently signed APK.
- Calendar identity remains `app.local.calendar`; version and credentials are independent of the repository/folder/module names. Private keys/passwords stay outside Git and the Nix store; only the public certificate pin is app-local/versioned. Missing credentials fail release packaging, never regenerate a key or fall back to debug signing.
- All commands run from the root wrapper. App `Exec` tasks invoke `apps/calendar-app/tools/` from that app's directory. `:calendar:coverage` additionally reads design class outputs for measurement only; this does not create a runtime edge from design to Calendar.
- The existing acceptance chain is root `.feature` → canonical `parse-gherkin` IR → Calendar's generator → root task-scoped generated Kotlin → Calendar Android tests/explicit regex handlers. Unknown/ambiguous handlers fail; no second parser/runner is introduced.
- JVM unit tests, generated UI acceptance and public-interface QA remain distinct. Development input injection at `CalendarEnvironment`/composition boundaries is not a public device-Settings or GrapheneOS QA claim.
- Publication is code plus an app-specific signed APK asset in app-suite. Future multi-app release/filter organization remains a decision for actual future products, not an implemented framework.

## Verification and proportionality

Use [Calendar's guide](../apps/calendar-app/README.md) and [quality commands](../docs/quality.md). The Architect review ran fresh unit/generator tests, all 49 UI acceptance cases, compilation, lint, debug/release/artifact checks, resolved Gradle module/dependency reports and an independent `:design:assembleDebug` library build. Evidence is in [the task Architect report](tasks/MONOREPO-001/checks/architect.md).

No separate architecture/property command is configured. For two small explicit Gradle modules, source/import/API review plus actual resolved dependency reports/builds are proportionate; no generic source-scanning framework or brittle regex check was added. This migration changes ownership/consumption, not algorithms, serialization or state transformations that need new property tests. Existing date invariants are already example-tested; new property machinery is not mechanically added, consistent with the owner's existing-test reuse decision.

The Architect pass changes documentation/tracking only: existing Cleaner CRAP/DRY reports are not invalidated or rerun. Their pre-existing global threshold findings remain documented, not a global quality PASS. Full mutation and independent QA remain separate roles.

Owner-emulator hardware rendering was unavailable during Cleaner. Architect's fresh UI regression used software rendering on the dedicated test AVD only, preserving all assertions/viewport inputs and leaving owner configuration/data untouched. This does not prove the owner's emulator is repaired or currently open. No new app implementation was made in this documentation-only pass.
