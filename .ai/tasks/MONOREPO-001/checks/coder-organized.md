# MONOREPO-001 — Final local organization and verification

Supersedes the layout/delivery status in `coder.md`. Owner explicitly requested an organized maintainable monorepo following the specification and code-sharing requirements, following the approved root rename and retention of Git history. No new tests were requested or added.

## Final layout and boundaries

Workspace: `/home/anon/Documents/Workspace/app-suite`.

```text
apps/calendar-app/              :calendar
  src/main/                     unchanged application behavior/identity
  src/test/                     existing deterministic JVM tests
  src/androidTest/              existing regex-driven Compose handlers
  tools/                        existing generator, generator tests, artifact tests
  build.gradle.kts              app identity/version/signing/verification
  release-certificate.sha256     unchanged public pin
  README.md / AGENTS.md          app-specific setup and rules
shared/design/                  :design, existing SuiteColors public API
  build.gradle.kts / src/        reusable presentation, no app business logic
README.md / AGENTS.md            common overview/rules, links to scoped app docs
docs/                           shared developer and publication guides
.ai/                            original tasks, IDs, features, QA and history retained
gradle/ + root Gradle files      single wrapper and shared plugin/repository setup
```

- `:calendar` explicitly maps to `apps/calendar-app/`; APK basenames are `calendar-debug.apk` / `calendar-release.apk`. No legacy `:app` adapter or duplicate active Calendar tree/tools remains.
- App Gradle Exec tasks run app-local tools from the app directory. The existing generator resolves root task artifacts from its new location; no parser, runner or parallel task convention added.
- Existing artifact-test assertions are unchanged; only artifact/pin paths adapted. Generator unit tests and production sources were moved unchanged. README/instructions now separate shared responsibilities from Calendar requirements.
- `:design` remains the one production palette definition; Calendar depends on it and imports `SuiteColors` without a competing palette. Date logic/navigation/hour sizing remain app-specific. No speculative component catalogue or second app.
- No dependency, permission, application ID, signing key/pin, version, feature/scenario ID or behavior changes in this organization pass. Version still 0.1.1 / code 2 pending publication baseline.

## Preservation review

Captured SHA-256 hashes for all 20 non-build files under the old `app/` and `tools/` immediately before moving. After mapping to final paths, all 20 were present: 17 byte-for-byte identical, only the app Gradle file and the two path-dependent tooling files adapted. No application sources, existing assertions, resources or signing pin changed in this pass. Owner's pre-existing edits/untracked files were included in the move and preserved.

Git history remains at original commit `328ad10` (`first commit`); no commit, staging, push, tag or history rewrite performed. Git status shows old tracked files deleted and destination files untracked until the owner stages the moves; this is not lost code. The history-based diff includes earlier owner work and must not be attributed entirely to MONOREPO. Original task IDs and existing evidence stay in root `.ai/tasks/` and are not duplicated/rephrased. Historical command paths are intentionally left historical, with current replacements in the live guides.

Limited signing-secret review of 154 existing intended files found no private signing-file extension or PEM private-key markers. The earlier 84-blob historical review remains applicable because history was not changed. Neither is a general secret/security audit; owner still reviews the final staged files/history before publication.

## Verification from final workspace

All Android commands used the existing `nix develop ~/nixos-config#android --command bash -c '…'` environment. No Nix configuration was changed.

```sh
./gradlew :calendar:clean :design:clean \
  :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:generateAcceptance \
  :calendar:lintDebug :design:lintDebug :calendar:assembleDebug \
  :calendar:assembleRelease :calendar:testReleaseArtifact
ANDROID_SERIAL=emulator-5580 ./gradlew :calendar:acceptanceTest
```

PASS, from clean module build outputs:

- 18 JVM tests, 4 existing Node generator tests, all 49 generated CAL-001 UI cases (zero skipped/failures/errors), 3 existing release-artifact checks.
- Lint: zero errors, Calendar 8 existing version-update warnings, design 2 version-update warnings.
- Kotlin/Android compilation and debug/release packaging.
- `parse-gherkin` again on all three MONOREPO feature files to their existing IR paths (syntax/IR only, not automated MONOREPO acceptance).
- Public guide review: 21 relative link targets in seven live guide/instruction files, all present; code-consuming link references and independent-app instructions updated for final module layout.
- `git diff --check`.
- Missing-secret fixture: empty external temporary directory selected via `CALENDAR_SIGNING_DIR`; `./gradlew :calendar:assembleRelease` exited 1 with explicit missing-credentials explanation. Temporary directory remained empty, existing release APK hash unchanged, no replacement key or debug fallback; only the created empty directory removed.

No tests, QA scripts, dependency frameworks, property/mutation or broad CRAP/DRY analysis added/run. All existing assertions preserved. Reports/logs: `organized-build.log`, `organized-acceptance.log`, `organized-acceptance-tests.xml`, `organized-missing-signing.log`; normal reports now under `apps/calendar-app/build/` and `shared/design/build/`.

## Devices and delivery

Dedicated test AVD `monorepo-001-test` / emulator-5580, API 35, no Play services/vending packages found, airplane mode on/Wi-Fi off, 1080×2400 at 440 dpi. Used hardware rendering, no snapshot, two CPU cores. UI acceptance passed in this pass without emulator failure. Only this dedicated instance received test fixtures.

After UI regression, installed preserved pre-layout debug APK and then final debug APK using `install -r` on the dedicated instance: both succeeded and firstInstallTime remained `2026-11-25 04:18:26`. This verifies update compatibility across the physical/module move, not the complete exact public-Settings S02 fixture.

Owner device discovered via `adb devices -l` and AVD name confirmed as `android-dev` / emulator-5554 (not the test instance). Executed:

```sh
adb -s emulator-5554 install -r apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk
adb -s emulator-5554 shell am start -W -n app.local.calendar/.MainActivity
```

Install `Success`, launch `Status: ok`; firstInstallTime stayed `2026-11-24 01:54:10` before/after. No uninstall, clear-data, wipe or signing workaround. Owner emulator left running with Calendar open. Dedicated test emulator stopped after verification.

## Local artifact record

| Artifact | Path | SHA-256 |
| --- | --- | --- |
| Debug | `apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk` | `0c254b5b3c3c2b64159dafd394ad0478f513a72e8b3ba3e032715edd82ffc2cd` |
| Release | `apps/calendar-app/build/outputs/apk/release/calendar-release.apk` | `9a3492fa9b44bb61ad4d1345f069ae193ed141df901e6105261165bce6428018` |

Release tests verify existing public pin `2c7e0aee6bf1d8dbebc0e2741e7a5578c88700724e4dfb6ea739a4e1f04360b4`, app.local.calendar, non-debuggable, debug signature distinct. Same app version (0.1.1 / code 2), not approved as a new update version. Rebuild/reverify and update hashes after owner-selected version changes.

## Remaining task scope (not declared complete)

F01/F02 local implementation and regression verified on the final layout. F03 remains needs_input: repository visibility, release tag/update-version baseline, owner repository creation, commits/pushes and release publication. Local remotes: `app-suite` → `git@github.com:d-s-i/app-suite.git`, `origin` → `git@github.com:d-s-i/calendar.git`. No GitHub operations performed; no old-repository modification. See `docs/publishing-calendar.md`.

S06/S07 publication checks remain NOT_RUN, especially the owner's exclusive attached-APK check. Exact S02 public-Settings QA, Android-17 GrapheneOS, external key backup and Obtainium remain unverified. Existing development regression is not a claim of those results. No full-task completion or published-release claim.
