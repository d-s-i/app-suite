# Cleaner Report — MONOREPO-001

## Status

**PASS — MONOREPO-001 changed-function scope.** All applicable changed/new functions meet CRAP <= 6; behavior regression, DRY and site-scan checks pass. The full contextual CRAP command still exits **2** for six unchanged/pre-existing functions, not 0: this is explicitly not a global quality PASS. The workflow says pre-existing/out-of-scope violations do not block validation of in-scope functions. The previous whole-project reservation was broader than that rule; it is corrected without changing the threshold, excluding sources or rewriting old code.

## Latest recheck after Architect

Repeated the explicitly requested Cleaner workflow after reading the current `.ai/architecture.md` and Architect report. Architect made documentation/tracking changes only; no new production defect justified further refactoring.

- Fresh 18 JVM / 4 generator tests, all 49 generated UI cases, lint/build and 3 artifact checks PASS. Dedicated `monorepo-001-test` / emulator-5580 used software rendering, one core / 1536 MiB, same offline 1080×2400 / 440 dpi fixture. No assertion, source, identity or signing change.
- Default deployed canonical commands now contain the tested Kotlin mangling fix. Verified deployed reader `/nix/store/47gkhza13x2lrxmc0fz17bv3jr245x9i-unclebob-crapper-0.1.0/.../crapper/kotlin_jacoco.py`; no new install/activation performed here.
- Ran ordinary `crap "<fresh :calendar:coverage --rerun-tasks command>" <same full source paths> --threshold 6`: complete report, raw exit 2 for the same six pre-existing findings. Read `.metrics/crap.edn`; byte-identical to the prior complete report. New/modified helpers remain 6; no threshold lowered, source removed or score increased.
- Default `dryer`: exit 0, no candidates; `mutate --scan`: exit 0, unchanged 129 sites, no full mutants; `uml-quality`: exit 0 after refreshed diagram, real overlay updated.
- Only documentation/evidence/tracking changed in this pass: corrected obsolete tool-deployment wording in `docs/quality.md`. Architect's completed status and all feature statuses preserved.

Owner-emulator GPU failure below is a **historical environment warning**, not a fresh successful launch or a current re-diagnosis. No owner start/install was attempted in this report-only recheck; no new app implementation requires a new deployment. Previous successful deliveries and failed GPU attempt remain attributed to their actual runs. The test instance is stopped after verification.

## Next Step

- No further Cleaner or repeated Architect pass required for the unchanged production sources; Architect is already completed.
- Owner may decide administrative closure/proportional remaining review scope. Hardener and independent QA remain not_run, not implicitly executed or waived by this Cleaner invocation.
- Recheck the separate owner-emulator graphics warning before any claim that Calendar is open there. No GPU/system configuration change is attempted here.

## Changes made

- Final pass: documentation, task tracking and fresh evidence only. Existing user work preserved; feature statuses F01–F03 remain completed.
- Earlier local cleanup retained: `apps/calendar-app/tools/generate-acceptance.mjs` splits output preparation, feature generation and CLI orchestration; `main` fell from CC 4 / CRAP 20 to CC 2 / CRAP 6. Parser, ordering, generated content, IDs and errors preserved; generated entrypoint hashes were previously checked identical.
- Earlier infrastructure retained: standard Gradle JaCoCo 0.8.13 and native Node LCOV, documented in `docs/quality.md`; no custom counter converter, runtime dependency or production class exclusion. Canonical Kotlin reader fix was separately owner-approved, regression-tested and built ephemerally; see `kotlin-reader-fix.md`. No global activation.
- Tests improved: no app assertions weakened, changed or deleted; existing tests reused. Canonical reader regression tests were added/tested in its own source directory during the approved tool fix.
- Duplication: no canonical DRY candidates; no abstraction added just to lower duplication.
- No commit/push, branch/worktree, release replacement, QA/specification rewrite, second app, full mutation or property-test campaign.

## Scope and provenance

Read root/app instructions, approved task/specification, F01/F02/F03 Gherkin and matching QA, affected code/tests/build configuration, developer guides and prior evidence. The initial pass had no architecture document; the current post-Architect pass reads the now-existing `.ai/architecture.md` and `checks/architect.md`. Reviewed F01 tooling first, then F02 shared-design/docs and F03 publication/docs; no task/feature IDs or approved behavior changed.

Actual production behavior remains in Calendar; `:calendar` consumes the one `SuiteColors` definition from `:design`. Alias imports are not a duplicate palette. Test expected colors remain independent rendering oracles. No broad module graph or system-boundary decisions made.

Compared against original commit `328ad10`: all generator functions preceding the CLI orchestration (including `expandScenarios`) and CalendarApp function bodies after the palette are byte-identical. Timed UI changes predate this task's migration as CAL-001 hour-label work; coder preservation evidence records only its palette import changed for MONOREPO. App/shared Kotlin sources and generator unit tests have no new diff against the owner's published commit in this recheck.

## Previous verification (historical runs)

From `/home/anon/Documents/Workspace/app-suite`, existing Android environment `nix develop ~/nixos-config#android --command bash -c '…'`:

| Command | Current result |
| --- | --- |
| `./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator --rerun-tasks` | PASS, 18 JVM + 4 generator tests |
| `ANDROID_SERIAL=emulator-5580 ./gradlew :calendar:acceptanceTest` | First attempt interrupted by QEMU failure; retry PASS, 49/49, zero skipped/failures |
| `./gradlew :calendar:coverage --rerun-tasks` through canonical `crap` | PASS, real fresh native JVM counters + Node LCOV |
| `./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:lintDebug :design:lintDebug :calendar:testReleaseArtifact` | PASS, lint/build and 3 signed-artifact checks included |
| Corrected canonical `dryer <same paths>` | Exit 0; report read: `{:candidates []}` |
| Corrected canonical `mutate --scan <same paths>` | Exit 0; 129 sites, no mutants executed |
| Canonical `uml` / `uml-quality` | Diagram refreshed (bounded viewer stopped); overlay exit 0, refreshed |
| `git diff --check` and live relative guide-link review | PASS |

Canonical source-matching suite used without changing deployed tools:

```text
/nix/store/26qkypsw3amszr7c7gvz1ijlr65vwrz6-uml-quality-tools
```

Built reader bytes were compared with the current canonical `~/nixos-config/config/uml-viewer/crapper-kotlin-jacoco.py`. Raw command, without a source exclusion or threshold change:

```sh
"$quality_tools/bin/crap" "nix develop ~/nixos-config#android --command bash -c './gradlew :calendar:coverage --rerun-tasks'" \
  apps/calendar-app/src/main shared/design/src/main \
  apps/calendar-app/tools/generate-acceptance.mjs --threshold 6
```

Complete `.metrics/crap.edn` read, no incomplete marker. Its exit 2 is the unchanged contextual threshold finding, not a collection/test/tool failure and not reported as command PASS. The fresh report is byte-identical to the previous complete post-reader-fix report: no score increased.

### Applicable functions

| File / line | Function | CC | Unit-report coverage | CRAP |
| --- | --- | --- | --- | --- |
| `apps/calendar-app/tools/generate-acceptance.mjs:29` | `prepareOutput` | 2 | 0% | 6 |
| same file:33 | `generateFeature` | 2 | 0% | 6 |
| same file:42 | `main` | 2 | 0% | 6 |

Zero hits are real generator-unit LCOV counters; CLI execution through actual generation/UI acceptance is checked separately, not falsely added to LCOV.

### Pre-existing findings, retained and non-blocking for this task scope

| Function | CC | JVM/Node unit-report coverage | CRAP |
| --- | --- | --- | --- |
| `TimeGrid` | 10 | 0% | 110 |
| `CalendarApp` | 8 | 0% | 72 |
| `TimedCalendar` | 8 | 0% | 72 |
| `MonthGrid` | 7 | 0% | 56 |
| `Modifier.horizontalPaging` | 3 | 0% | 12 |
| `expandScenarios` | 9 | 93.75% | 9.0198 |

They remain in every analysis/report; no callable was moved into ignored/generated code or omitted to obtain a PASS. The `TimeGrid` source-to-JVM mangling defect is resolved, not an unmeasured path. No whole-application <=6 claim. Follow-up UI/parser cleanup is separate from moving the app and extracting its palette, not imposed incidentally here.

Coverage is JVM tests, not Android UI counters. All classes including design getters are in JaCoCo; source-only palette properties have no named source functions for CRAP, so no invented property/function scores. `.kts`, manifests and resources are reviewed through code/build/lint, not assigned unsupported callable metrics. Independent public-Settings S02/GrapheneOS QA and external signing backup remain unverified.

### DRY / scan / UML

Mutation sites by file: CalendarApp 44, CalendarDates 14, MainActivity 1, TimedCalendar 55, generator 15, SuiteColors 0. No file over 100, no mechanical split. This is not a mutation score. UML overlay contains measured CRAP/coverage only, no invented mutation data or DRY-as-mutation values.

### Environment observations

First UI attempt: QEMU logged hanging threads and disappeared; Gradle reported device not found and no completed test run. Preserved failed attempt rather than weakening tests. Retried on the same dedicated offline AOSP AVD `monorepo-001-test` with one CPU core / 1536 MiB, same 1080×2400 / 440 dpi viewport, airplane mode/Wi-Fi-off fixture and unmodified assertions: all 49 passed. Underlying cause of the first native hang is not established. No owner device received fixtures; no unrelated machine build/process was stopped.

After stopping the test instance, normal `android start` was attempted for persistent visible `android-dev`. It did not register with ADB; its launcher log reports hardware rendering unavailable, `VK_ERROR_INITIALIZATION_FAILED`, `FATAL | No physical devices available`. Therefore the extra install/launch check was not reached and the wait timed out. No wipe/uninstall/data-clear, fallback signature, emulator configuration or graphics-driver change. Prior successful owner installation is recorded in earlier evidence, not reasserted for this attempt.

## Remaining issues

- Architect: completed, no boundary/code changes; its documentation was reviewed in this recheck.
- Hardener: 129 sites and pre-existing UI/parser complexity; full mutation not run.
- Test failures: none in the completed unit/UI/artifact runs; initial native-emulator interruption and current owner-launch warning preserved separately.
- Missing tooling: none; the corrected canonical reader is now verified in deployed commands. Earlier ephemeral-suite references describe the original fix verification, not the current deployment.
- Human attention: owner-emulator GPU startup, external signing-key backup, independent QA. No public release replacement or phone testing by Cleaner.

Latest evidence: `cleaner-post-architect-tests.log`, `cleaner-post-architect-build.log`, `cleaner-post-architect-acceptance-tests.xml`, `cleaner-post-architect-crap.{edn,log}`, `cleaner-post-architect-dry.edn`, `cleaner-post-architect-scan.log`, `cleaner-post-architect-quality.json`.

Historical evidence beside this report: `cleaner-resume-tests-failed.log`, `cleaner-resume-tests.log`, `cleaner-resume-emulator-failed.log`, `cleaner-resume-crap.{edn,log}`, `cleaner-resume-dry.edn`, `cleaner-resume-scan.log`, `cleaner-resume-build.log`, `cleaner-resume-quality.json`, `cleaner-resume-owner.log`, `cleaner-resume-owner-emulator.log`. Prior reader-fix/baseline evidence remains intact.
