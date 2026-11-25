# CAL-001 — 0.1.1: complete hour labels and capitalized month headings

## Scope / changed files

Owner approved matching Proton's hour measurement in Day/Week and capital initials for localized month headings. No dependencies, permissions, events, storage, signing-key regeneration, commit, push or publication.

- `TimedCalendar.kt`: both views measure unwrapped 12sp hour labels, add each side's independently rounded physical padding, and render one complete line with no clipping. Removed fixed Week width; hour centering/padding now consistent in both views.
- `CalendarDates.kt`: locale-aware capital initial for month toolbar titles, retaining accents/language/year rules.
- `CalendarDatesTest.kt`: revised French expectations and an explicit twelve-month accented-name table.
- `CalendarSteps.kt` / existing feature files: rendered line-count/overflow/character checks; density/font-scale injection only in the development adapter; explicit phone-sized fixture check; updated French heading Examples.
- `app/build.gradle.kts`: versionName 0.1.1, versionCode 2; application ID and existing release key/pin unchanged.
- Specification/QA prose, README/AGENTS and task tracking updated. See `../hour-labels-and-headings-v011.md` for source evidence/traceability.

## Tests first and verified cause

On the old implementation at API 35 / 440 dpi, the new S23 Day/24h case failed: `01:00` had lineCount 2. The Day container could lose one pixel because round(20dp) differs from 2×round(10dp); Week also used a fixed 52dp column. This is a reproduced defect, not a measurement of the owner's unknown phone density. Enforcing one line alone would hide clipping; tests also require every character, no ellipsis, and no horizontal/vertical overflow.

Capitalization tests were changed/added before implementation and failed on the old lowercase French headings. The owner explicitly requested the new presentation; expected behavior was not weakened to hide a failure.

Features processed sequentially: F02 hour sizing passed its 23 cases, then F01 heading work passed its 13 cases, then the complete regression ran.

## Final verification

In the existing Android Nix environment:

```sh
./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:lintRelease :app:testReleaseArtifact
ANDROID_SERIAL=emulator-5580 ./gradlew :app:acceptanceTest
```

- 18 JVM unit tests PASS.
- 4 Node generator tests PASS.
- 49 generated acceptance cases / 21 scenarios PASS, zero skipped/failures/errors.
- New S23 matrix: Day/Week × 12h/24h × font scale 1.0/1.6 at density 2.75; all displayed hours checked.
- Dedicated test viewport 1080x2400 at 440 dpi. UI-density overrides are at the real app's composition boundary, not production flags. Ordinary test fixture font scaling was restored to 1.0 after release inspection.
- Existing `parse-gherkin` reused for all feature files; no parser/runtime duplication. Active QA mapping and supplied/used Examples parameters checked.
- Debug/release compilation and APK builds PASS; 3 release-artifact tests PASS with the same public certificate pin.
- Debug and release lint: 0 errors / 8 existing SDK/library-update warnings each.

No CRAP, source DRY, mutation, property-test framework, or executable external QA script was run/added. External QA procedures remain NOT_RUN.

## Signed upgrade and release smoke

Preserved the previously signed 0.1.0 APK temporarily before rebuilding. On isolated calendar-ui-test / API 35: fresh old-release install, then `install -r` of 0.1.1 succeeded without an uninstall between versions. Android reports versionCode 2, and firstInstallTime stayed identical. This verifies an in-place update with the existing release certificate/application ID.

The signed updated app was launched with actual test-device 24h and enlarged-font (1.6) Settings. Day/Week screenshots were inspected: 04:00 and surrounding labels remain complete on one line (`v011-large-font-day.png`, `v011-large-font-week.png`). Only this newly installed fixture app was removed afterward; font/time-format overrides restored, test emulator stopped. Owner phone/emulator data were not cleared.

## Delivery and limits

Owner AVD android-dev / emulator-5554 verified. Rebuilt debug APK installed in place via the shared Android helper; MainActivity launched with status ok. Owner emulator remains running with Calendar open and its original debug signing identity.

Signed update: `app/build/outputs/apk/release/app-release.apk` (0.1.1 / code 2), checksum beside it. Same release key; private credentials untouched and outside Git/Nix store. No key backup or GitHub Release was performed by the agent. Owner must commit/push public changes and publish a new v0.1.1 GitHub Release for Obtainium to detect it; replacing an old release asset alone is not the intended update workflow.

The owner reported initial GrapheneOS use, but this post-fix build has not been revalidated on that phone by the agent. Actual GrapheneOS external QA and rendered Proton screenshot parity remain unverified.
