# S02 public-interface interaction record

No QA runner/script/test file was created. Commands were executed interactively from repository root in the existing Android Nix environment, using `adb -s emulator-5580` only. Screen coordinates below are from the captured public accessibility hierarchy on this dedicated 1080×1920 / 440 dpi AVD; they are not product-private/test selectors.

## Preflight and baseline

- Booted existing `monorepo-001-test` (API 35, no window/snapshot, SwiftShader, one core / 1536 MiB), verified AVD name and device list. No android-dev/phone selected.
- Existing archived `/tmp/monorepo-f01-debug.apk` was preserved in the earlier F01 implementation before production palette extraction/app relocation. The Gradle root naming change had already occurred in F01; no baseline was rebuilt/recreated for QA. Its retained original debug app is the pre-production-migration baseline, not the later `/tmp/app-suite-before-layout-debug.apk` (which already contains initial shared-design work).
- Captured SHA-256, ID, version, debuggability and certificate with public `apkanalyzer` / `apksigner`. Both actual APKs: app.local.calendar / 0.1.1 / code 2 / debug, same certificate `24324e7bff5dd16cb5548385e58829fa9c9486bbd2446e39574953b1e76da89a`.
- Device had no Calendar package before setup; installed the archived baseline once using `install -r`, with no uninstall/clear/wipe. UID 10148 / firstInstallTime observed. The baseline was genuinely installed before the migrated update, not simulated. This checks same-version migration compatibility, not a versionCode increase.
- Read-only package probes: no com.google.android.gms or com.android.vending. `pm path` returns 1 for an absent package; initial `set -e` preflight stopped at that expected result, then was continued with explicit absence handling. Not a product/environment failure.
- Original public settings recorded: Europe/Paris, auto_time=1, auto_time_zone=1, time_12_24=null (Use locale default), en-rUS, font_scale=1.0, airplane=1, Wi-Fi=0.

## Android Settings fixture (no shell writes)

Opened Settings using `am start -a android.settings.DATE_SETTINGS`. Used only visible toggles/pickers and public `input tap`/`input swipe` events; `settings get`, `getprop`, `am get-config` and `date` were read-only verification.

1. Initial Settings: tap time auto toggle `(950,640)`, zone auto toggle `(950,1370)`, zone row `(400,1560)`.
2. Zone selector: More `(1020,140)` → Select by UTC offset `(800,140)` → Coordinated Universal Time `(600,660)` → Back. Captures show GMT+00:00 / UTC; OS identifier Etc/UTC.
3. Date row `(350,820)` → year heading `(265,325)` → year 2028 `(520,1090)` → Previous month `(246,606)` nine times (0.4 s between taps) → public day `15 February 2028` `(440,1058)` → OK `(815,1520)`.
4. Swipe Settings list `(540,1620) → (540,800)` to show time-format options. Tap Use locale default `(950,1490)` off, Use 24-hour format `(950,1680)` on.
5. Time row `(400,645)` → public hour 12 on dial `(540,762)` → public minute 00 at the same dial top after hour selection → OK `(815,1390)`.
6. Final Settings XML/PNG retained. Public clock read immediately before update: `Tue Feb 15 12:00:03 UTC 2028`, config en-rUS, time_12_24=24; auto time/zone disabled for controlled fixture. Network flags unchanged/offline.

## Product workflow

- Recorded baseline package UID / firstInstallTime, ran `install -r apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk`, observed `Success`, same single package / UID 10148 and firstInstallTime `2026-11-25 17:10:51` under UTC before/after. Earlier CET rendering `18:10:51` represents the same instant, not a changed installation time.
- Launched normal public activity with `am start -W -n app.local.calendar/.MainActivity`; Status ok. No instrumentation runner, fake clock, test API, internal function or database access used for S02.
- Captured `month.xml` / `month.png`: February, 7 columns / 6 rows, all 42 public full-date descriptions from Monday January 31 through Sunday March 12 2028, no duplicates/gaps. Checked native accessibility bounds and screenshot, not Compose test tags.
- Tapped February 20 cell center `(1003,923)` from its public date-description bounds. Captured `day.xml` / `day.png`: Sun 20 and single-date time grid.
- Sent public system Back (`input keyevent KEYCODE_BACK`). Captured `month-after-back.xml` / PNG: same February 42-cell grid restored.
- Opened Calendar views `(95,150)`, captured drawer, tapped visible Week row `(350,555)`. Captured `week.xml` / PNG: seven aligned headers Mon14, Tue15, Wed16, Thu17, Fri18, Sat19, Sun20, no account/network prompt.
- Network state and package continuity recorded again: airplane=1, Wi-Fi=0, UID 10148 and firstInstallTime unchanged. No manual uninstall/clear/wipe command executed during S02.

A first read-only numeric check of `week.xml` selected every matching number, including the unrelated toolbar Today=15. It reproduced `15,14,15,16,17,18,19,20`. Corrected the QA observation to the visible date-header bounds, checked each date's alignment with its weekday, and retained the unchanged expected 14–20 range. The screenshot already showed correct product behavior; no product code/expectation was changed. `public-observations.log` records the successful checks and reproduced probe error.

## Cleanup and separate development regression

Restored via Android Settings:

- Date picker: year 2026, Next month nine times to November, day 25, OK.
- Time zone: More → Select by region → public search France → select France (Paris).
- Re-enabled auto time, auto zone and Use locale default. Device clock resynchronized to host: `Wed Nov 25 18:20:26 CET 2026`; Europe/Paris, 1/1/null, en-rUS, airplane=1/Wi-Fi=0 exactly as original. No direct clock/settings write.

Then ran the ordinary existing developer regression (not S02 proof): `ANDROID_SERIAL=emulator-5580 ./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:acceptanceTest --rerun-tasks`, PASS (18 JVM / 4 generator / 49 UI cases). Its documented test input injection and AGP-managed fixture APK lifecycle occur after the recorded external S02 check and do not replace its public-interface evidence. Restored settings were read again after regression and remained correct.

Removed only the owned temporary `/sdcard/monorepo-qa.xml` dump, then stopped the dedicated test instance. Original archived APK was also copied unchanged to ignored `qa/evidence/baseline-debug.apk` for fixture preservation; metadata/checksums are versionable, APK is not committed. Owner emulator/phone/data/signatures and host settings were untouched throughout.
