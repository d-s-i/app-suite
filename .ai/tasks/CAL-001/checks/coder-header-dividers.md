# CAL-001 — Day and Week header dividers

## Approved change

Owner confirmed both Day and Week. Keep today's existing outline. Match the pinned Proton reference's `HeaderDrawer.drawTimeColumnSeparatorExtension` and full-width header bottom line; no global-toolbar divider or new calendar functionality.

## Implementation and files

- `app/src/main/java/app/local/calendar/TimedCalendar.kt`: one shared header-drawing modifier extends the vertical axis through either date header and draws the bottom horizontal divider across the full width. Aligns the body's axis stroke with the header. Hour lines start at 1, as in the reference's displayed-hours range, avoiding an extra midnight stroke beside the fixed header divider.
- `CalendarSteps.kt` and `features/empty-timed-views.feature`: stable S22 tests both modes using actual painted pixels, header/grid alignment, and full-width coverage before and after scrolling to each end of the day.
- Updated specification/QA prose, README/AGENTS counts, and existing task tracking. No generated entrypoint hand-edited; no dependencies, permissions, date logic, event storage, outline styling, global toolbar, or command configuration changed.

## Verification

The new S22 Day case failed before implementation on the missing vertical header line, then passed after the change.

```sh
nix develop ~/nixos-config#android --command bash -c './gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:assembleDebug && ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest'
```

Results: 17 existing JVM unit tests PASS; 4 generator tests PASS; all 41 generated development acceptance cases / 20 scenarios PASS, no skipped/failures/errors. Kotlin compilation and debug APK build PASS. Lint: 0 errors / 8 existing update warnings. Existing parse-gherkin was reused; static scenario/QA/Examples consistency checked. Repeated S22 assertions intentionally verify different scroll states. No source-quality analysis or external QA scripts run.

Testing used the isolated API-35 calendar-ui-test emulator, then stopped it. User clock/network/data untouched by test fixtures. No new pure date unit test was needed for this pixel-only rendering change; focused rendered acceptance tests were written first.

## Delivery

Verified shared persistent AVD android-dev / emulator-5554. `android start` and `android install` (sourced canonical function) succeeded; APK updated in place, then MainActivity launched with status ok and confirmed RESUMED. Day and Week manually inspected via public GUI; screenshots: coder-header-dividers-day.png and coder-header-dividers-week.png. Today outline is retained in Day. Owner emulator left running with Calendar open on Week. No uninstall, data clearing, signing change, AVD wipe, commits, or pushes.

Evidence: coder-header-dividers-tests.xml and coder-header-dividers.json. Debug APK: app/build/outputs/apk/debug/app-debug.apk. Android-17 GrapheneOS, external QA, and exact rendered Proton parity remain unverified.
