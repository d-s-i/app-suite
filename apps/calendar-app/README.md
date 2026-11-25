# Calendar

Personal, offline Android calendar for GrapheneOS. Current implementation: **empty month/week/day views and navigation only** (CAL-001). System Back from Day opens its containing Month; the expansion arrow/dropdown is removed. Hour labels remain complete at enlarged fonts/fractional densities; localized month headings have a capital initial. Events, reminders, persistence and synchronization are not implemented.

Kotlin + Compose UI/Foundation; no Material components, Google Play services, calendar access or network permission. Proton Calendar Android 2.34.2 is the inspected UI reference; no Proton code/assets are bundled and exact rendered parity is not established.

## Module boundaries

- `src/main/`: Calendar activity, environment adapter, date logic and Compose views.
- `src/test/`: deterministic JVM unit tests.
- `src/androidTest/`: regex-driven Compose acceptance step handlers.
- `tools/`: the existing Node-built-in acceptance generator, its tests and release-artifact tests. These are Calendar-specific, not shared tooling.
- `release-certificate.sha256`: public certificate pin only.
- `build.gradle.kts`: Calendar identity, versions, dependencies, signing and verification tasks.

Calendar consumes `implementation(project(":design"))` and imports the public `SuiteColors` palette from [`shared/design`](../../shared/design/). Calendar's date/navigation/measurement logic is not shared. See [design reuse](../../docs/design.md).
Task sources and evidence stay at repository-root `.ai/tasks/`, without copies inside the app. CAL-001 acceptance is `.feature` → existing `parse-gherkin` IR → this app's `tools/generate-acceptance.mjs` → `.ai/tasks/CAL-001/acceptance/generated/` → real Compose UI and explicit regex handlers. Generated files must not be hand-edited.

## Build

**All commands below run from the app-suite repository root**, not this directory. Use its single wrapper. Requires JDK 17, SDK platform 35/build-tools 35.0.0 and `ANDROID_HOME` or a local `sdk.dir`; see the [root environment guide](../../README.md#build-environment).

```sh
./gradlew :calendar:assembleDebug
```

Debug APK: `apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk`. Keep this development signature on the owner emulator; it is distinct from the release signature. Gradle project name `calendar` determines the APK basename, not the physical `calendar-app` folder.

### Signed release for GitHub / Obtainium

```sh
./gradlew :calendar:assembleRelease
./gradlew :calendar:testReleaseArtifact
```

Signed APK: `apps/calendar-app/build/outputs/apk/release/calendar-release.apk`. Publish this APK in `d-s-i/app-suite`, never a private signing file. Follow the [owner publication checklist](../../docs/publishing-calendar.md). No publication is presumed complete. Obtainium follows its configured repository's release assets; changing that repository remains an unverified owner action.

Keep application ID `app.local.calendar` and the existing release key. Current 0.1.1 / code 2 is preserved pending the owner's publication baseline and tag choice. Choose an approved higher `versionCode` than every release being replaced, with the matching `versionName`, before publishing an update.

The owner-approved key is PKCS12, alias `calendar-release`, RSA-4096, 10,000-day certificate validity. Private state remains outside Git and the Nix store:

- `~/.local/share/calendar-signing/calendar-release.p12`
- `~/.local/share/calendar-signing/calendar-release.password`

Directory mode 0700; key/password 0600. Back up the **whole directory** securely, preferably encrypted on a separate offline device. No external backup is verified. Never upload credentials to GitHub, release assets, logs or chat. `CALENDAR_SIGNING_DIR` selects a restored directory with these filenames. Missing credentials explicitly fail release packaging; no replacement key or debug-signing fallback.

`apps/calendar-app/release-certificate.sha256` is only the public certificate fingerprint and is safe to commit. The existing artifact tests use SDK tools to verify that pin, non-debuggability, application ID and debug/release signature separation.

A release cannot replace a debug-signed installation with the same app ID. Never uninstall, clear data, change identity or regenerate keys as an automatic workaround. First phone installation should use release; owner emulator development continues with debug.

Compile/target SDK 35, minSdk 26 (`java.time`). Development tests use API 35, **not** the owner's Android-17 GrapheneOS phone. Older Android and Android 17 remain unverified. The platform Android theme is not a Material dependency.

## Verify

```sh
./gradlew :calendar:testDebugUnitTest
./gradlew :calendar:testAcceptanceGenerator
./gradlew :calendar:lintDebug :design:lintDebug
```

Unit tests cover dates, localization, hour formats and navigation. Lint correctness errors remain fatal; dependency/SDK-update advisories remain warnings.

### Development Gherkin acceptance

Requires Node (built-ins only), existing `parse-gherkin` and a connected **dedicated offline AOSP test device** without Play services, airplane mode enabled and Wi-Fi disabled. Do not apply fixtures to the owner's phone or persistent emulator.

```sh
./gradlew :calendar:generateAcceptance
ANDROID_SERIAL=<test-serial> ./gradlew :calendar:acceptanceTest
ANDROID_SERIAL=<test-serial> ./gradlew :calendar:acceptanceTest -PacceptanceFeature=CAL-001-F01
```

Feature selectors: `CAL-001-F01`, `CAL-001-F02`, `CAL-001-F03`; without a selector, all 49 cases run. The hour-label matrix uses density 2.75 and font scales 1.0/1.6. Give the dedicated test device a phone-sized physical viewport (verified: 1080×2400 at 440 dpi):

```sh
adb -s <test-serial> shell wm size 1080x2400
adb -s <test-serial> shell wm density 440
```

Clock/locale/hour format and density/font-scale inputs are injected at the real UI environment boundary. Actions/assertions operate on rendered Compose content, geometry and pixels. This is **not** external Settings/GrapheneOS QA or rendered Proton parity. The public-GUI QA procedure remains at `.ai/tasks/CAL-001/qa/procedure.md`; its execution is not implied by these tests.

Reports: `apps/calendar-app/build/test-results/`, `apps/calendar-app/build/reports/androidTests/`, `apps/calendar-app/build/reports/lint-results-debug.html`. Historical evidence remains under `.ai/tasks/CAL-001/checks/`; current migration evidence under `.ai/tasks/MONOREPO-001/checks/`.

## Owner emulator delivery

Discover live serials using `adb devices -l` and confirm `adb -s <serial> emu avd name` returns `android-dev`. If stopped, use the canonical `android start` helper. Never assume a fixed port or use a temporary/read-only test instance as owner delivery. If multiple matching instances exist, ask which to use.

```sh
adb -s <owner-serial> install -r apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk
adb -s <owner-serial> shell am start -n app.local.calendar/.MainActivity
```

Both commands must succeed; leave Calendar open on the running visible emulator. Preserve data/signature, do not clear/uninstall/wipe, and keep clock/network/density fixture changes on a separate test emulator. The shared Android helper's old default APK path does not apply; supply this explicit path if using `android install`.

## Limits

No event data is saved; backup/cloud/device-transfer extraction is disabled. Decide export/backup during PERSISTENCE-001. External signing-key backup, GitHub publication, Obtainium installation and GrapheneOS verification remain owner actions/unverified. No release was uploaded by the agent.
