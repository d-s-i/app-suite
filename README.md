# app-suite

Android application monorepo for independent Kotlin/Compose apps with shared presentation code. Calendar is the first and currently only app. Target repository: `d-s-i/app-suite`; the original `d-s-i/calendar` is retained, not renamed or deleted. Existing Git history is preserved.

## Repository layout

```text
app-suite/
├── apps/
│   └── calendar-app/        # :calendar — app, tests, app-specific tools and signing pin
├── shared/
│   └── design/              # :design — existing reusable palette, no app behavior
├── docs/                    # design reuse, adding apps, owner publication
├── .ai/                     # shared task tracking; original task IDs/artifacts preserved
├── gradle/                  # one wrapper for all modules
├── build.gradle.kts         # shared plugin versions
└── settings.gradle.kts      # module registration and repository policy
```

| Module | Guide | Boundary |
| --- | --- | --- |
| `:calendar` | [Calendar build, tests and delivery](apps/calendar-app/README.md) | Own Android identity, data, permissions, version, signing and acceptance tools |
| `:design` | [Reuse the design](docs/design.md) | Public `SuiteColors`, consumed by Calendar; no app navigation, data or signing |

[Add an independent app](docs/adding-apps.md) · [Publish Calendar](docs/publishing-calendar.md) · [Coverage and quality checks](docs/quality.md) · [Architecture](.ai/architecture.md).
No second product, framework, CI or multi-app release scheme is implied.

## Build environment

Run commands from this repository root. Requires JDK 17, Android SDK platform 35/build-tools 35.0.0, Node, the existing `parse-gherkin` command and `ANDROID_HOME` (or local `sdk.dir`). On the owner's NixOS machine:

```sh
nix develop ~/nixos-config#android
./gradlew :calendar:assembleDebug
```

Use the single checked-in wrapper and pinned versions. No per-app Gradle wrapper, replacement parser, Material dependency, Play services or shared private signing state.

## Verify

```sh
./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:generateAcceptance
./gradlew :calendar:lintDebug :design:lintDebug :calendar:assembleDebug
./gradlew :calendar:assembleRelease :calendar:testReleaseArtifact
```

Release commands require the owner's existing external credentials. Run the 49 existing UI acceptance cases with `ANDROID_SERIAL=<dedicated-test-serial> ./gradlew :calendar:acceptanceTest` only after following the [Calendar test-device setup](apps/calendar-app/README.md#development-gherkin-acceptance). Do not use the owner emulator for fixtures. After shared changes, verify every actual consuming app; currently that means Calendar only.

## Migration and delivery status

Calendar now lives at `apps/calendar-app/` and is addressed as `:calendar`, not legacy `:app`. Its generator/artifact tools are in `apps/calendar-app/tools/`; the root has no Calendar-specific tools. APK paths and updated delivery commands are in the Calendar guide. Historical task evidence retains the commands/paths actually used at that time; it is not rewritten to pretend those checks ran on the new layout.

Calendar 0.1.1 / code 2 is published in the **public** `d-s-i/app-suite` repository: [release calendar-v0.1.1](https://github.com/d-s-i/app-suite/releases/tag/calendar-v0.1.1). The agent uploaded the verified release APK/checksum with explicit owner authorization; code was committed/pushed by the owner. The owner made the repository public and confirmed installation through Obtainium. No agent phone/GrapheneOS QA or owner checksum comparison is claimed. Coder delivery is closed. Cleaner is complete for the MONOREPO changed-function scope: fresh regression checks pass, the Kotlin/JaCoCo name matcher is corrected and now verified in the deployed canonical commands, and changed functions meet CRAP <=6. Unchanged pre-existing functions still exceed the global threshold and remain documented, not hidden. The extra owner-emulator startup check currently has a hardware-GPU environment warning; no new app code was changed in the final review and no fresh successful owner launch is claimed. See [Cleaner report](.ai/tasks/MONOREPO-001/checks/cleaner.md). [Architecture review](.ai/tasks/MONOREPO-001/checks/architect.md) is complete: module boundaries retained and documented, with no production changes. [Targeted QA](.ai/tasks/MONOREPO-001/qa/report.md) now verifies S02 through real Android Settings/UI and the documented developer guides. Other full scenario executions were intentionally not repeated; owner release feedback is retained without an invented checksum comparison. This is not a blanket fresh S01–S07 or GrapheneOS PASS. Hardener remains not_run. See `.ai/tasks/MONOREPO-001/checks/github-release.md` for the artifact record. Project license remains undefined.
