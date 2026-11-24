# Calendar

Personal, offline Android calendar for GrapheneOS. The current implementation covers **empty month/week/day views and navigation only** (CAL-001). System Back from Day opens its containing Month; the month-expansion arrow/dropdown has been removed. Day's labels sit above the left time axis with reference-derived typography and initial hour positioning; the global month toolbar and Week layout are retained. Events, reminders, storage, and synchronization are not implemented.

Kotlin + Compose UI/Foundation, without Material components, Google Play services, calendar access, or network permission. Proton Calendar Android 2.34.2 is the inspected UI reference; no Proton source or assets are bundled. Exact rendered parity has not been established.

## Build

Requires JDK 17, Android SDK platform 35/build-tools 35.0.0, and `ANDROID_HOME` or a local `sdk.dir`. Use the checked-in Gradle wrapper:

```sh
./gradlew :app:assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`. This is a development/debug-signed APK, not a production-signed release. Preserve application ID and signing identity when eventually distributing updates.

Build baseline: compile/target SDK 35, minimum SDK 26 (native `java.time`). Development tests ran on API 35, **not** the owner's Android-17 GrapheneOS phone. Older Android versions and Android 17 remain unverified. The platform Android theme is not an added Material UI dependency.

## Verify

```sh
./gradlew :app:testDebugUnitTest
./gradlew :app:testAcceptanceGenerator
./gradlew :app:lintDebug
```

Unit tests cover date ranges, localization, hour formats, and period navigation. Lint correctness errors fail the build; SDK/library-update warnings remain visible, not suppressed.

### Development Gherkin acceptance

Requires Node (built-ins only), the existing `parse-gherkin` command from the owner's tooling, and a connected **dedicated test** Android device/emulator. Use an offline, non-Google-Play AOSP emulator with airplane mode enabled and Wi-Fi disabled. Do not run tests against a personal-data phone by default.

```sh
./gradlew :app:generateAcceptance
./gradlew :app:acceptanceTest
./gradlew :app:acceptanceTest -PacceptanceFeature=CAL-001-F01
```

Use `ANDROID_SERIAL` to select a device if necessary. Feature selectors are `CAL-001-F01`, `CAL-001-F02`, or `CAL-001-F03`; without a selector, all 41 cases run.

Pipeline: approved `.feature` files → existing `parse-gherkin` IR → `tools/generate-acceptance.mjs` → generated Kotlin tests in `.ai/tasks/CAL-001/acceptance/generated/` → actual Compose UI and explicit regex step handlers on Android. No replacement Gherkin parser or second runner is used. Generated files are disposable and must not be hand-edited.

These development tests inject clock/locale/hour-format inputs at the same UI's environment boundary. UI actions and assertions operate on rendered Compose content, geometry, and pixels. They are **not** external end-to-end QA, proof that device Settings integration was tested, or a Proton screenshot comparison. The separate public-interface QA procedure is `.ai/tasks/CAL-001/qa/procedure.md`; it has not been executed.

Reports: `app/build/test-results/`, `app/build/reports/androidTests/`, and `app/build/reports/lint-results-debug.html`. Task-scoped development evidence is under `.ai/tasks/CAL-001/checks/`.

## Limits

No event data is saved. Backup/cloud/device-transfer extraction is disabled as a privacy-preserving default; decide an explicit export/backup policy during PERSISTENCE-001. No production signing keys or release process have been configured. Project license remains undefined.
