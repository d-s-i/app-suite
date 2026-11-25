# Kotlin/JaCoCo reader fix and Cleaner recheck

## Historical intermediate status

The later Cleaner recheck applies the workflow's explicit changed-function scope rule and completes Cleaner; see [current Cleaner report](cleaner.md). Global exit 2 findings below remain unchanged and visible, not relabelled as a global PASS. The following preserves the intermediate reservation before scope classification was corrected.

**Reader fix verified; intermediate Cleaner REVISE for global threshold findings, not missing tooling/coverage.** Full-scope CRAP now produces complete measured results (exit 2), not an incomplete report (exit 3). No global threshold PASS/automatic Cleaner closure is claimed. All new/modified MONOREPO CLI functions remain at CRAP 6. Remaining violations are pre-existing, not newly introduced behavior.

## Authorized canonical change

Owner approved correcting the canonical reader in `~/nixos-config`, testing/building ephemerally without activating the machine, then rerunning Cleaner.

Files changed there, preserving unrelated existing work:

- `config/uml-viewer/kotlin-tools.test.py`: three regression tests covering zero/partial/full JaCoCo instruction counters; correct and incorrect local names; source spans; valid/invalid suffixes; overload isolation and ambiguity; stale reports and wrong source files.
- `config/uml-viewer/crapper-kotlin-jacoco.py`: recognize a trailing seven-character base64url Kotlin value-class suffix while retaining the original name, exact/local-function matching, source span and single-candidate requirement. No counters or XML modified.
- `config/uml-viewer/README.md`: document supported case and ephemeral-vs-installed distinction.

No project-local producer fork, new dependency, pin change, global installation, Home Manager/NixOS activation, commit/push, property-test run or full mutation campaign.

## Tests before/after

The new tests were written first. Running the actual local reader through the existing test-only source binding produced 6 failing assertions across the three new cases (`None` instead of real percentages), with 15 total tests. After the implementation, all 15 passed.

Canonical package checks ran as part of `pnpm build:uml:quality-tools` in `~/nixos-config`: 15 Kotlin CRAP tests, 14 local extension tests, 59 existing upstream regression tests passed. `pnpm test:uml:003:unit` passed its 24 wrapper/adapter/resource-boundary tests. These are regression tests, not properties or a mutation campaign.

Rebuilt suite used for recheck:

```text
/nix/store/26qkypsw3amszr7c7gvz1ijlr65vwrz6-uml-quality-tools
```

The default deployed command still points to the older store package. The source correction is canonical and the rebuilt suite is validated; use its explicit path (reproducible command in `docs/quality.md`) until a separately authorized activation. No replacement of deployed files or machine-wide change occurred.

## Fresh project measurement

Executed from `/home/anon/Documents/Workspace/app-suite`:

```sh
"$quality_tools/bin/crap" "nix develop ~/nixos-config#android --command bash -c './gradlew :calendar:coverage --rerun-tasks'" \
  apps/calendar-app/src/main shared/design/src/main \
  apps/calendar-app/tools/generate-acceptance.mjs --threshold 6
```

Real fresh native JaCoCo XML and Node LCOV, same classes/source scope as before, no production exclusions. Result: **exit 2, complete report with threshold findings**. The canonical wrapper removed its own incomplete marker after recognizing complete measurements; no manual marker bypass.

`TimeGrid` is now mapped to actual `TimeGrid-1FqWxJw` counters: JVM-test coverage **0%**, CC **10**, CRAP **110**. That is not Android UI coverage. Separate generated UI acceptance still passes all 49 cases. Former unknown coverage becoming measured zero is a measurement correction, not an application regression.

Remaining global findings (all pre-existing): TimeGrid 110, CalendarApp 72, TimedCalendar 72, MonthGrid 56, Modifier.horizontalPaging 12, expandScenarios 9.0198. MONOREPO-modified `main`, new `prepareOutput` and new `generateFeature` each remain **6**. No applicable modified function exceeds the threshold; no global PASS is invented for the pre-existing code. No broad UI/parser refactor was performed merely to suppress the global exit status.

The earlier `cleaner.md` records the local generator refactor and scope/preservation evidence; its initial incomplete analysis is retained as historical. The corrected reports are snapshots named `cleaner-fixed-*` beside this file.

## Other final checks

- `./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:acceptanceTest :calendar:lintDebug :design:lintDebug :calendar:testReleaseArtifact` with `ANDROID_SERIAL=emulator-5580` in existing Android Nix environment: PASS. Fresh JVM coverage run exercised 18 JVM / 4 generator tests; final UI acceptance 49/49, artifact verification 3/3.
- Canonical rebuilt `dryer`: exit 0; report read, no candidates.
- Canonical rebuilt `mutate --scan`: exit 0; same 129 sites (44 CalendarApp, 14 CalendarDates, 1 MainActivity, 55 TimedCalendar, 15 generator, 0 SuiteColors); no full mutants.
- Canonical `uml`: graph regenerated (bounded local viewer stopped via timeout); `uml-quality`: exit 0, `.uml-viewer/quality.json` refreshed and read, including real TimeGrid CRAP 110. No mutation score added.
- `git diff --check` for touched canonical files and project: PASS.

Final owner-device check found emulator-5554 stopped. Restarted the normal persistent visible `android-dev` using canonical `android start`, confirmed its AVD name/live serial, and launched Calendar with `Status: ok`; left it running. No wipe/clear/uninstall or temporary owner instance. The dedicated test instance was stopped. Evidence: `cleaner-fixed-owner.log`.

Only documentation/tracking changed in the app-suite recheck; Calendar behavior, identity, signing, versions, tests and feature statuses remain unchanged. Published release was not replaced. Independent public-GUI/GrapheneOS QA, external signing-key backup and full mutation testing remain unverified as before.

## Next step

The canonical mapping defect is resolved and regression-tested. Existing global CRAP threshold findings are now measurable and documented; the strict whole-selected-scope threshold still does not pass. Keep this distinct from validation of MONOREPO's modified functions, which meet <=6. Do not automatically refactor pre-existing UI/parser code or lower the threshold; further cleanup scope needs explicit agreement. Architect/hardener/QA have not been run.
