# CAL-001 — Coder report

## Implementation

- Implemented empty Month, Week, and Day views with Monday-first dates, localized month titles, 12h/24h time labels, period swipes, date selection, mini-calendar expansion/collapse, Today, and the current-time line/dot.
- Month uses the extracted 7x6 geometry, 42 consecutive dates, muted spillover dates, 12sp day numbers, baseline/outline/separator dimensions. Source-derived structure is implemented; exact rendered Proton parity is not claimed.
- Added one Kotlin/Compose Android module and the approved minimal build/runtime/test dependency set. No Material3, event store, event editor, recurrence engine, reminders, network/calendar permission, Google Play services, Proton code, or Proton assets.
- Added a small device-input boundary (`CalendarEnvironment`) used by production and deterministic development tests. Production reads the actual device clock/locale/hour format and refreshes environment on resume; a disposable minute ticker updates the current-time indicator.
- Added an original launcher icon and explicit no-backup/extraction configuration as the fully-local default. No event/persistence behavior was implemented.
- Build baseline: JDK 17, Gradle 8.13 with official distribution checksum, AGP 8.13.2, Kotlin/Compose compiler 2.2.21, compile/target SDK 35, minSdk 26. These are implementation/build settings, not owner-approved support guarantees for older Android versions.
- Added README commands and updated factual repository instructions in AGENTS.md. No Git repository exists, so no Git diff was available; created production/test/build files and tracking changes were reviewed directly. No branch, worktree, commit, or push was created.

### Main changed files

- `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, Gradle wrapper and `app/build.gradle.kts`.
- `app/src/main/java/app/local/calendar/{CalendarDates,MainActivity,CalendarApp,TimedCalendar}.kt`.
- `app/src/main/AndroidManifest.xml` and small style/icon/extraction resources.
- `app/src/test/java/app/local/calendar/`: three focused test classes.
- `app/src/androidTest/java/app/local/calendar/acceptance/CalendarSteps.kt`.
- `tools/generate-acceptance.mjs` and `tools/generate-acceptance.test.mjs`.
- `README.md`, `AGENTS.md`, `.gitignore`, task tracking, and task-scoped development evidence.

## Tests

Features were processed sequentially: F01 completed after its 13 cases passed, then F02 after its 9 cases passed, then F03 after its 11 cases passed. A final unfiltered regression ran all 33 cases together after the final source/resource/test-adapter adjustments.

- JVM unit tests: **14 passed**, covering leap/non-leap February, Monday-first 42-date ranges, year boundaries, localized titles, week ranges, 12h/24h labels, minute positions, day/week/month movement, and invalid paging direction.
- Generator tests: **4 passed**, covering background/row expansion, missing/unused parameters, stable IDs, Kotlin string escaping, and duplicate scenario rejection.
- Development acceptance: **33 passed, 0 skipped, 0 failures/errors**, covering all 15 approved scenarios through generated Kotlin entrypoints and explicit regex handlers. Unknown/ambiguous handler matches fail closed.
- Focused unit test files were written before their production functions. Initial red runs failed to compile because those functions did not exist; implementation then made the tests pass. The generator also had a recorded missing-module red run before implementation.
- During acceptance development, corrected the pixel adapter to distinguish the geometric rounded-rectangle corner from the inset bounding box of fully saturated antialiased pixels. Also added the missing equivalent `its/the 42 date cells` step mapping. No approved Gherkin or expected behavior was weakened or rewritten.
- Actual rendered assertions inspect Compose text layouts, bounds, pixels, painted hour separators, and current-time line/dot position. The clock fixture uses 12:30, so the indicator check exercises minute positioning, not only an integer hour.

### Acceptance infrastructure

Reused `parse-gherkin`; no replacement parser or Cucumber dependency. `:app:generateAcceptance` invokes the existing parser, then the Node-built-in generator expands every Examples row into a JUnit method under the exact task's `acceptance/generated/`. Gradle includes these as Android instrumentation test sources, separate from JVM unit tests. `:app:acceptanceTest` executes them through AndroidX/Compose's existing runner and fails on test failure.

The final test environment was a read-only API-35 AOSP emulator instance (`emulator-5580`), with airplane mode enabled, Wi-Fi disabled, and Google Play services absent. Only this temporary instance was altered; no physical phone was connected. The development adapter injects clock/locale/hour format into the actual app UI, not Android Settings. This is not execution of the external QA procedure.

## Commands

Final successful verification commands:

```sh
./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:assembleDebug
ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest
node --test tools/generate-acceptance.test.mjs
```

- Unit tests — PASS (14).
- Generator tests — PASS (4).
- Generated acceptance — PASS (33).
- Kotlin compilation/debug APK assembly — PASS.
- Android lint — PASS with **0 errors, 8 SDK/library-update warnings**. Correctness errors remain fatal. Version advisories are reported rather than promoted to errors or suppressed.
- `parse-gherkin` — PASS for all three approved feature files, invoked by generation on each acceptance build.
- APK manifest inspection with `apkanalyzer` — minSdk 26, targetSdk 35; no network or calendar permissions. The only reported requested permission is AndroidX's app-scoped signature permission `app.local.calendar.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`.

The initial stricter lint configuration promoted all update advisories to errors. It was changed to standard warning handling while retaining `abortOnError`; actual resource API, icon, and backup-rule issues were fixed. No lint baseline or rule suppression hides correctness failures.

## Evidence and deliverable

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk` (debug-signed, not a production release).
- `coder-verification.json`: counts, environment, APK hash/size, and explicit limits.
- `coder-all-tests.xml`: final actual instrumentation report with all three feature classes.
- `coder-f01-tests.xml`, `coder-f02-tests.xml`, `coder-f03-tests.xml`: sequential development runs.
- `coder-unit-TEST-*.xml`, `coder-generator-tests.txt`, `coder-lint.txt`: actual unit/generator/lint evidence.
- `coder-month.png`, `coder-week.png`, `coder-day.png`: manually inspected development smoke screenshots using the emulator's real local date/time. They are not Proton references or executions of the 2028 QA fixtures.

## Next step

Owner review of the APK and screenshots, then any explicitly requested subsequent role. Implementation is ready for review; the overall task is not marked externally QA-accepted. No other task is started.

## Issues Encountred

- Intended Pixel 9 Pro / Android-17 GrapheneOS execution is **UNVERIFIED**. Available SDK/emulator is API 35; target/compile SDK 35 also produces update/compatibility warnings.
- Proton rendered reference captures, exact visual parity, and the public-interface QA procedure are **NOT_RUN**. No account login or personal data was used.
- Actual device Settings integration, other Android versions, dark/landscape/accessibility configurations, and production signing are not certified by these development tests.
- Project license and eventual production application identity/signing ownership remain to be confirmed before release. Backup/export and all event/persistence requirements remain separate tasks.
- No CRAP, source DRY analysis, mutation tests, property tests, executable QA scripts, or architectural review was run or produced.
