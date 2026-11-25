# MONOREPO-001 — Initial local implementation (historical layout)

Superseded for current layout, commands, APK paths/hashes and delivery by [coder-organized.md](coder-organized.md). This report preserves the results actually observed before the final owner-requested organization; its `:app` / `app/` paths are historical, not current instructions.

## Approval and scope

Owner resolved the initial workflow blocker with « oui non pas besoin de nouveaux tests »: reuse existing tests and the approved delivery checklist, without adding tests/runner/handlers. No MONOREPO Gherkin automation was invented. F01 was implemented/checked/delivered first, then F02, then F03 publication preparation. F01/F02 implementation is complete; overall task remains blocked on F03 owner inputs/actions.

## Implementation

- Root Gradle identity `app-suite`; keep Calendar `:app` and its paths/commands, application ID, versions (0.1.1 / code 2), signing pin, permissions and existing work/history. No physical workspace move or GitHub rename.
- `:design`, mapped to `shared/design/`, is an Android library using the already pinned AGP/Kotlin/Compose UI versions. Public `SuiteColors` holds the five original color values. Calendar imports this API under the alias `CalendarColors`; no duplicate palette remains. No new external runtime dependency, permission, Material layer, behavior or speculative component.
- README, AGENTS and three developer guides cover actual consumption, independent apps and owner publication. No second app, CI, release key, executable QA script, parser or runner added.
- Added local remote `app-suite = git@github.com:d-s-i/app-suite.git`; `origin` remains `git@github.com:d-s-i/calendar.git`. No remote repository operations, commits, pushes or tags were performed.
- Changed implementation files: `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts` (only project dependency in this task), `CalendarApp.kt` (palette extraction/import), `TimedCalendar.kt` (import only), `shared/design/build.gradle.kts`, `shared/design/src/main/java/app/suite/design/SuiteColors.kt`, `README.md`, `AGENTS.md`, `docs/{design,adding-apps,publishing-calendar}.md`, and MONOREPO tracking/evidence. Pre-existing signing, hour-label, CAL/EVENTS task and other user changes were preserved; the full Git diff includes those earlier changes and must not be attributed to this task.

## Executed checks

Commands used `nix develop ~/nixos-config#android --command bash -c '<command>'` (existing canonical environment; no Nix configuration edited).

| Command / check | Result |
| --- | --- |
| `./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:generateAcceptance :app:lintDebug :app:assembleDebug :app:assembleRelease :app:testReleaseArtifact` after F01 | PASS |
| `ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest` after F01, hardware-rendered retry | PASS, all 49 |
| `./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :design:lintDebug :app:assembleDebug :app:assembleRelease :app:testReleaseArtifact` after F02 | PASS; 18 JVM, 4 generator, 3 artifact tests |
| `ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest` after F02 | PASS, 49 cases, 0 skipped/failures/errors; includes fractional-density/large-font hour coverage |
| `parse-gherkin <feature> <matching acceptance json>` for all three MONOREPO features | PASS (syntax/IR only, not executable MONOREPO acceptance) |
| `CALENDAR_SIGNING_DIR=<new empty temp dir> ./gradlew :app:assembleRelease` | Expected exit 1 with explicit missing-credentials error; empty dir remained empty and was removed; no fallback/key creation. Repeated after F02. |
| `git diff --check` | PASS |
| Local limited signing-secret review | 144 intended paths and 84 historical blobs checked for private signing-file extensions and PEM private-key markers; no findings. Not a general secret/security audit; owner must review final staged files/history before publication. |

Final debug lint: zero errors, 8 existing dependency/SDK update warnings; shared design lint: zero errors, 2 version update warnings. No mutation/property/CRAP/DRY campaigns or new tests were added.

Evidence: `coder-build.log`, `coder-acceptance.log`, `coder-acceptance-tests.xml`; normal Gradle reports remain under `app/build/` and `shared/design/build/`.

### Environment incident

First UI attempt used software rendering; emulator logged hanging QEMU threads, disappeared, and the run failed after 14 completed cases (49 expected). This attempt was not a PASS. Restarting the dedicated test emulator with hardware rendering and two cores produced complete passing F01 and F02 runs. No assertions were weakened. The precise cause of the emulator hang was not established.

## Device delivery

Dedicated fixture AVD `monorepo-001-test`, API 35, serial emulator-5580: 1080×2400 / 440 dpi, airplane mode enabled, Wi-Fi disabled, no `com.google.android.gms`/`com.android.vending` package found. Test fixtures were not applied to the owner emulator.

Owner persistent visible AVD `android-dev`, serial emulator-5554, verified with `adb devices -l` and `adb -s emulator-5554 emu avd name`. Started normally using `android start` when stopped. After both implementation slices:

```sh
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 shell am start -W -n app.local.calendar/.MainActivity
```

Both install/launch succeeded. Final installed version 0.1.1 / code 2; firstInstallTime remains 2026-11-24 01:54:10, unchanged from baseline. No uninstall, clear-data, wipe or signing workaround used. Calendar left open on the running owner emulator.

Additional dedicated-device update check: installing F01 debug then the shared-design debug via `install -r` both succeeded with identical firstInstallTime. This and generated UI regression are development checks, not execution of the exact public-Settings S02 QA fixture.

## Local APKs (not yet selected as a publication update)

- Debug `app/build/outputs/apk/debug/app-debug.apk`: SHA-256 `68f60c469352a5259f9db3ed2436b62cdb93cc689e8b769ccd98423a0a89f9e8`.
- Release `app/build/outputs/apk/release/app-release.apk`: SHA-256 `5c1b66560e87d00f943a69a832f2a988e9bb20f8030370389eec1bdf1d478d96`.
- Release app.local.calendar, 0.1.1 / code 2, non-debuggable; existing public signing pin `2c7e0aee6bf1d8dbebc0e2741e7a5578c88700724e4dfb6ea739a4e1f04360b4`; debug certificate distinct. No network/calendar permission added; manifest lists the existing app-scoped dynamic-receiver permission.
- Release version was preserved, not bumped without the owner-provided baseline. A later approved version change requires rebuilding/rechecking and recording new hashes.

## Limits and next actions

F03 is `needs_input`: owner must choose visibility, release tag and highest release versionCode to replace; then create the new repository and commit/push reviewed work. Follow `docs/publishing-calendar.md`. Repo target URL is https://github.com/d-s-i/app-suite (not verified created/published). No release URL or owner APK result exists yet. S06 and S07 remain NOT_RUN. The old GitHub repository was not modified or remotely verified in this run.

S04/S05 guides and referenced implementation were reviewed locally; no second-app integration claimed. Exact external public-GUI QA S02, Android-17 GrapheneOS, external key backup and Obtainium remain unverified. Existing generated UI acceptance does not substitute for those claims. No task-complete or published-release claim is made.
