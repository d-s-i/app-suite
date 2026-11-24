# CAL-001 — Day source correction and unified Android commands

## Implementation

Approved scope and source evidence are in `../day-reference-correction.md`.

- `TimedCalendar.kt`: Day weekday/date move to the left time column, with 12sp/16sp medium text and the source-derived today outline/header layout. Hour labels use 12sp, vertical text-layout centering, widest-label column width plus 10dp padding per side, and short hour ticks. Day starts one hour before now. Week keeps its previous styling/scrolling, and the global month toolbar remains.
- `CalendarDates.kt`: small pure `dayInitialHour` calculation, tested for midday, midnight, and late minutes.
- Added stable scenarios S19/S20/S21 and matching public-GUI QA prose; adjusted existing development geometry assertions to measure the actual grid/time axis now that Day's date header is not over the event column.
- No new app dependency, permission, storage, event, or reminder behavior. No Proton code/assets imported.

## Tests and commands

Written before implementation: three new JVM tests and the Day-header acceptance scenario. Red runs failed on the missing date function and old header placement. Final checks:

```sh
nix develop ~/nixos-config#android --command bash -c './gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:assembleDebug && ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest'
```

- 17 JVM unit tests: PASS.
- 4 generator tests: PASS.
- 39 generated acceptance cases / 19 scenarios: PASS, no skipped/failures/errors.
- Kotlin compilation / debug APK: PASS.
- Lint: 0 errors, 8 existing SDK/library-update warnings.
- Existing parse-gherkin parser invoked by generation for all three feature files. No replacement parser, executable QA scripts, mutation, CRAP, or source DRY analysis.
- Separate API-35 `calendar-ui-test` emulator used for acceptance; stopped afterward. Owner data/clock/network untouched by tests.

## Android command configuration

Canonical files changed under `~/nixos-config/common/`:
- `android-shell.sh` and its built-in Node tests: one Bash function for `android`, `android start`, `android stop`, `android install [APK]`, and help. Quoted arguments and current project directory are preserved.
- `android-commands.sh` and tests: same guarded `android-dev` backend, updated usage/help; legacy hyphenated wrappers remain compatible.
- `android-dev.nix`: also exposes an `android` binary inside the SDK shell.
- `home-base.nix`: removes the old alias and sources the new function for future Home Manager activation.
- Android docs/AGENTS updated. No system rebuild, Home Manager activation, commit, or push performed; pre-existing unrelated changes preserved.

Verification: 13 Node tests PASS; Bash syntax PASS; new SDK entrypoint built and `android help` executed successfully. Updated Home Manager `.bashrc` derivation was built and checked to contain the function source line without the old alias. It was **not activated**. Existing owner terminals must run `source ~/nixos-config/common/android-shell.sh`; automatic loading in future terminals requires the usual Home Manager/NixOS activation. This limitation is explicit, not a claim that parent-shell state was changed by the agent.

Real sourced-function smoke: `android start` verified the already-running `android-dev`; `android install` updated the current project's debug APK successfully.

## Delivery

Verified owner AVD `android-dev`, serial `emulator-5554`. Installed in place with the new command; Calendar launched with `am start -W`, status ok, confirmed RESUMED. Left running and open on today's Day view. No uninstall, data clear, signing change, or AVD wipe. Screenshot: `coder-day-reference.png`.

## Limits

Source-guided correction, not rendered Proton screenshot parity. Android-17 GrapheneOS/Pixel 9 Pro execution and external QA remain unverified. Debug signing only. Command persistence is configured but awaits normal Home Manager activation; manual sourcing works immediately in an existing Bash terminal.
