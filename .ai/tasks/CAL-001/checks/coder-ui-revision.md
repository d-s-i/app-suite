# CAL-001 — System Back and month-arrow revision

## Approved change and implementation

The owner's request is recorded in `../ui-revision.md`.

- `CalendarApp.kt`: system Back from Day opens the containing Month. An open drawer is dismissed first, leaving Day active until another Back. Month/Week system-Back behavior is otherwise unchanged.
- Removed the expansion arrow, picker state, dropdown mini-calendar, and its empty collapsed strip from `CalendarApp.kt` and `TimedCalendar.kt`. No unreachable picker UI or alternative expansion control remains.
- `CalendarSteps.kt`: added actual system Back input with UI-idle synchronization, drawer-priority checks, and absence checks for the removed control/picker.
- Updated the existing feature files, QA procedures, traceability, README, AGENTS instructions, and task state. IDs S09/S13 are explicitly retired rather than reused; new S16/S17/S18 cover the changed behavior. No generated Kotlin entrypoint was hand-edited.
- No dependencies, permissions, event functionality, or storage added. No user/system configuration changed; no branch, commit, or push created. Test AVD configuration was temporary and isolated.

## Verification actually performed

Tests were added before the production changes:
- S16 first failed because Back left the app instead of showing Month.
- S17 first failed because the expansion control was still displayed.
- Back tests synchronize Compose before injecting the external key event, avoiding a race with BackHandler registration; expected behavior was not weakened.

Final results:
- JVM unit tests: PASS, 14.
- Node acceptance-generator tests: PASS, 4.
- Generated development acceptance: PASS, 35 cases / 16 scenarios, 0 skipped/failures/errors.
- Kotlin compilation, debug APK assembly: PASS.
- Android lint: 0 errors, 8 existing SDK/dependency-update warnings.
- `parse-gherkin`: PASS, all three current feature files through the existing generator. Static checks confirmed active scenario-to-QA mapping and used/supplied Examples parameters.

Commands executed with JDK/SDK environment supplied for the current shell:

```sh
./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:assembleDebug
ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest
```

This NixOS shell also supplied the existing SDK's `aapt2` via `-Pandroid.aapt2FromMavenOverride`; no machine paths were added to project files. Focused F03/F02 and red-test invocations were also run.

Testing used a separate temporary `calendar-ui-test` AVD (API 35, offline, no Play services). The owner's emulator was not used for destructive acceptance runs or clock/network fixtures. The test emulator has been stopped. Historical 33-case reports and Gherkin DRY reports describe the previous contract; no Gherkin/source DRY or mutation analysis was rerun for this revision.

## Owner-emulator delivery

The persistent visible `fake-msg` emulator was stopped during the work. It was restarted using the existing local launcher. Its first launch attempt reported a missing SDK environment; supplying the verified SDK environment resolved it without changing user/system configuration.

- AVD identity verified: `fake-msg`, serial `emulator-5554`.
- `adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk`: Success.
- `adb -s emulator-5554 shell am start -W -n app.local.calendar/.MainActivity`: Status ok; Calendar confirmed RESUMED.
- Manual development smoke: opened a day through the month grid, captured the arrow-free Day screen, pressed system Back, and captured Month while Calendar stayed foreground.
- No uninstall, data clear, AVD wipe, signing change, or owner clock/network mutation. Emulator left running with Calendar open.

Evidence: `coder-ui-revision-tests.xml`, `coder-ui-revision.json`, `coder-ui-revision-day.png`, `coder-ui-revision-back.png`. APK: `app/build/outputs/apk/debug/app-debug.apk`.

## Limits

Android-17 GrapheneOS/Pixel 9 Pro execution, external QA procedures, and exact Proton screenshot parity remain unverified. The APK is debug-signed. This revision intentionally overrides the reference's dropdown picker behavior; it does not change the scope of later event/reminder/persistence tasks.
