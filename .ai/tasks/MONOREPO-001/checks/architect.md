# Architect Report — MONOREPO-001

## Status

**PASS** for the approved monorepo migration. No blocking module-boundary, dependency-direction or shared-interface violation found. No production refactor is justified by this review. All configured applicable unit/UI/build verifications pass freshly; absent/non-applicable property/architecture test commands are not labelled as executed.

## Latest requested revalidation — unchanged sources

The repeated Architect invocation independently rechecked the current tree, Gradle registrations/dependencies, imports, `SuiteColors`, environment/date contracts and generator/build integration after the latest Cleaner pass. No production/test/configuration change or new boundary violation was found. Existing uncommitted Cleaner work and feature/other-role statuses were preserved.

Fresh verification repeated the full unit/generator/generation/lint/debug/release/artifact command with `--rerun-tasks`, then Gradle `projects`, both modules' resolved `debugRuntimeClasspath` reports and `:design:assembleDebug`: all exit 0. Results remain 18 JVM, 4 generator and 3 artifact checks. The only internal resolved edge is again `:calendar → :design`; no reverse project dependency. Dedicated offline AVD `monorepo-001-test` / emulator-5580, software SwiftShader, one core / 1536 MiB, 1080×2400 / 440 dpi: all 49 generated UI cases passed on the first attempt, unchanged assertions.

No code, module/interface/dependency or test changes were needed. Existing architecture documentation remains accurate. Property-test and dedicated architecture-check applicability remain as justified below; no configured command was skipped, replaced or invented. Latest Cleaner CRAP/DRY were read as context only, not rerun, reclassified or invalidated. Owner emulator/phone/configuration were not touched; historical GPU warning is not re-diagnosed or claimed repaired here.

The further identical Architect invocation again found unchanged production/test sources and the same sole module edge. Unit/build/lint/generation/release checks were actually rerun, not inferred from the previous PASS: 18 JVM, 4 generator, 3 artifact checks and all 49 UI cases passed, together with fresh resolved dependency reports and the design AAR task. No new interface, property obligation or configured architecture-check command appeared.

Only this report, fresh evidence and task tracking are updated. Latest evidence: `architect-repeat-build.log`, `architect-repeat-acceptance.log`, `architect-repeat-acceptance-tests.xml`. Previous revalidation evidence remains in `architect-recheck-*`; the original review evidence remains in `architect-build.log`, `architect-acceptance.log` and the other original snapshots. No additional Cleaner/Architect cycle is required for these unchanged sources; remaining role/administrative closure decisions belong to the owner.

## Inputs reviewed

- Specification: `task.md`, `specification.md`, approval/state and all three approved features (F01–F03 / S01–S07).
- Acceptance criteria: AC-MONOREPO-01–07 and matching `qa/procedure.md`; no requirement/QA changes and no independent end-to-end QA executed.
- Coder report: `coder-organized.md` (historical build/move evidence) plus `github-release.md` and current state/owner feedback for subsequent publication.
- Cleaner report: latest `cleaner.md`; prior `kotlin-reader-fix.md` for tool/mapping provenance.
- Existing architecture documentation: no `.ai/architecture.md` existed; root/app README, AGENTS, `docs/design.md`, `docs/adding-apps.md` and module/tooling configuration inspected directly.
- Existing quality reports: `.metrics/crap.edn`, `.metrics/dry.edn`, native coverage context and `.uml-viewer/diagram.json`/Cleaner evidence. Global CRAP findings remain pre-existing (maximum 110); no CRAP/DRY producer rerun or metric cleanup in this role. No property-test report/command is present.
- Current source/tests inspected: all root/module Gradle files, five Kotlin production files, Calendar manifest/backup policy, app-local Node tools/tests, all 18 JVM tests, complete Compose step-handler source, generated acceptance integration and developer guides. Existing generated diagram is context, not substituted for independent source/Gradle dependency inspection.

## Findings

| Severity | Location | Finding | Action |
|---|---|---|---|
| None | `settings.gradle.kts`, app/library build files and sources | Exactly two runtime modules; the only internal dependency is `:calendar → :design`, no reverse edge/cycle. | Retain; source and resolved dependency reports agree. |
| Info | `shared/design/SuiteColors`, Calendar palette imports | Public shared API is five immutable presentation colors; Calendar consumes it under a local alias, not a copy. No app/date/navigation/signing/data types leaked into design. | Document intentional Compose presentation contract, no abstraction layer added. |
| Info | Root build and `apps/calendar-app/tools/` | Common wrapper/versions/repository policy are root-owned; app orchestration/tooling/signing are app-owned; task sources stay shared at root. | Retain current ownership; no speculative second app or generic tooling framework. |
| Low, pre-existing | `CalendarDates.movePeriod` / `CalendarView` | Pure app date navigation references a framework-free presentation enum physically defined in a UI file. This is inside one app module, not a library reverse dependency. | Document accepted placement; no separate domain module/ports solely for this migration. Reconsider only if a real future policy extraction requires it. |
| Info | `MainActivity`, `CalendarEnvironment`, timed UI | Android settings/lifecycle/ticking remain at the adapter/UI edge. Date helpers use native Java time/locale and are JVM-testable. Shared API contains no activity, storage or transport shapes. | No behavior-preserving structural correction needed now. |
| Info | Tests/build configuration | No architecture/property command configured; the approved change introduces no new transformation law needing property machinery. | Reuse configured tests and resolved Gradle reports; no mechanical tests or generic framework. |
| Environment, pre-existing | Owner `android-dev` GPU startup | Cleaner reported native hardware-GPU initialization failure. | Not repaired here; fresh dedicated UI regression passes with software rendering. Do not claim owner emulator is open. |

## Changes made

- Files changed in this role: new `.ai/architecture.md`, root README link/status, task tracking and this report/fresh logs. Existing uncommitted source/test/quality-infrastructure work preserved.
- Module or boundary changes: none. Responsibilities/contracts/data flows and intentional trade-offs documented from actual code.
- Dependency-direction changes: none; no added dependencies, modules, permissions, framework or CI.
- Encapsulation changes: none required. The intended cross-module API remains `app.suite.design.SuiteColors`; app state/behavior/credentials remain private to app ownership.
- Property tests added or changed: none. This is a structural migration/palette ownership change, not new serialization, round-trip or state-transform logic. Existing date examples already assert range/consecutive-date/boundary invariants. Owner explicitly requested existing-test reuse; no suitable task-specific new property obligation was identified.
- Architecture checks added or changed: none. For two explicit modules, direct source/API/import review and real Gradle module/dependency reports/library build are sufficient. No brittle regex source scanner or generic architecture framework introduced merely to produce a new command.
- No specification, acceptance criteria, QA, tests/assertions, signing identity, version or public product behavior changed. No commit/push or release/old-repository operation.

## Verification

All commands ran from repository root in `nix develop ~/nixos-config#android --command bash -c '<command>'`.

| Check | Command / evidence | Result |
|---|---|---|
| Unit tests | `./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator ... --rerun-tasks` | **PASS**: 18 JVM / 4 Node generator tests, zero failures/skips |
| Acceptance tests | `ANDROID_SERIAL=emulator-5580 ./gradlew :calendar:acceptanceTest` | **PASS**: all 49 generated UI cases, zero failures/skips |
| Typecheck / compilation / build | Same rerun invocation includes `:calendar:generateAcceptance :calendar:lintDebug :design:lintDebug :calendar:assembleDebug :calendar:assembleRelease :calendar:testReleaseArtifact` | **PASS**: Kotlin compile, Android packaging, lint and 3 signature/identity checks. Existing 8 app / 2 design update advisories remain warnings. No separate typecheck command configured. |
| Module/dependency review | `./gradlew projects :calendar:dependencies --configuration debugRuntimeClasspath :design:dependencies --configuration debugRuntimeClasspath` | **PASS**: root registers `:calendar`, `:design`; only `project :design` under Calendar, none back to Calendar under design |
| Standalone shared-library build | `./gradlew :design:assembleDebug`; inspected AAR entry list | **PASS**: `shared/design/build/outputs/aar/design-debug.aar`, ordinary library manifest/classes/metadata, not an APK |
| Property tests | No configured applicable command | **NOT RUN**: assessed unnecessary for this structural slice; no property result claimed |
| Automated architecture checks | No configured dedicated checker | **NOT RUN**: no absent checker presented as PASS; source/Gradle graph review above passed independently |
| Documentation/metadata consistency | Relative link targets, valid JSON and `git diff --check` | **PASS** |

Full rerun build command:

```sh
./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:generateAcceptance \
  :calendar:lintDebug :design:lintDebug :calendar:assembleDebug :calendar:assembleRelease \
  :calendar:testReleaseArtifact --rerun-tasks
```

Dedicated existing AVD `monorepo-001-test`, API 35 / emulator-5580, 1080×2400 at 440 dpi, airplane mode enabled/Wi-Fi disabled. Used documented emulator `-gpu swiftshader`, one core / 1536 MiB, no window/snapshot, Vulkan feature disabled in that test invocation only. This avoids reliance on the unavailable host hardware GPU without changing any test assertion or production source. All 49 cases passed on the first Architect run. No owner configuration/data, phone or physical-device settings were modified; the test emulator is stopped after verification.

No new app implementation in this documentation-only Architect pass, therefore no new owner install/launch claimed. Owner hardware-GPU issue remains separate and unverified as repaired. This is not GrapheneOS, external public-Settings S02 QA or an owner release-APK check.

Fresh evidence: `architect-build.log`, `architect-acceptance.log`, `architect-acceptance-tests.xml`, `architect-dependencies.log`, `architect-design-aar.log` beside this report.

## CRAP / DRY report status

- Reports reviewed: latest complete raw `.metrics/crap.edn`, `.metrics/dry.edn`, Cleaner report and its coverage/reader-fix context. JVM-only coverage is not misrepresented as Android UI coverage.
- Reports invalidated by architectural changes: **none**. No measured production, tooling function, test, Gradle source or dependency configuration changed in this role; documentation/tracking only.
- Cleaner must rerun: **no**, not because of this review. Existing pre-migration UI/parser findings remain visible and were not fixed/reclassified by Architect.
- No CRAP/DRY scan/cleanup, source mutations, property campaign or end-to-end QA rerun performed outside role ownership.

## Remaining risks

- Shared design has one real consumer; independent second-app integration is intentionally not claimed. Follow the existing add-app guide when a real next product is approved.
- Existing app UI/parser complexity and limited JVM coverage remain Cleaner/hardener context, not a newly introduced architecture defect.
- Owner-emulator native GPU availability, secure external key backup and independent GrapheneOS/public-GUI QA remain outside this completed structural review.
- Future actual apps require independent Android identities, versions, data, permissions and credentials, plus explicit release-asset selection; no speculative distribution scheme imposed now.

## Human decisions required

None blocking this architecture slice. Changes outside the approved task (GPU/system fixes, future app/product/data design, source-wide cleanup, deletion of the old remote repository or global tool activation) still require separate approval. Architect role is completed; hardener/QA remain not_run, not implicitly approved or executed.
