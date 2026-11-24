# Instructions for Coding Agents

## Project Context

Personal calendar application for the owner's use on GrapheneOS. Address the owner's difficulty creating custom events and dissatisfaction with the current month display; the exact UX is not yet defined.

Confirmed scope:
- Month, week, and day views. Month cells display event titles (e.g. "Dentist"), not just dots.
- Creation of one-time and recurring events, including recurrence every X days. This repeats the event, not merely its notification.
- Multiple reminders per event. Advance choices: 1 hour, 4 hours, 1 day, 3 days, 1 week, 2 weeks, and 1 month. "1 month before" means subtract 30 days, not a calendar month.
- Fully local, app-private event storage. Do not use Android Calendar Provider or integrate Google Calendar in the current scope.
- Kotlin with Jetpack Compose is approved. CAL-001 uses Activity Compose + Compose UI/Foundation, with no Material3; this minimal build/runtime/test dependency set was explicitly approved. Storage technology is not selected.
- Target device reported by the owner: Pixel 9 Pro, Android 17 with GrapheneOS. Device behavior has not been tested.
- Work without Google Play services; installable by APK.
- Free to use.

## Repository State

- `app/src/main/java/app/local/calendar/`: device-input adapter (`MainActivity`), pure date calculations (`CalendarDates`), month UI (`CalendarApp`), and timed UI (`TimedCalendar`).
- `app/src/test/`: deterministic JVM unit tests, separate from acceptance.
- `app/src/androidTest/`: regex-driven Compose acceptance step handlers.
- `tools/`: Node-built-in acceptance entrypoint generator and its tests; reuse the existing `parse-gherkin`, never reimplement it.
- `.ai/tasks/CAL-001/`: approved specification, features, public-interface QA procedure, generated acceptance output, and task evidence. Do not hand-edit `acceptance/generated/`.
- `app/build.gradle.kts` and Gradle wrapper: pinned build/dependencies. No CI or Git repository exists at this stage.

Current implementation is CAL-001 empty views/navigation only. System Back from Day opens its containing Month (closing an open navigation drawer first); the owner removed the expansion arrow/dropdown mini-calendar. The product scope above includes later tasks; do not implement events, persistence, or reminders as incidental calendar work.

## Development and Verification Commands

- `./gradlew :app:testDebugUnitTest`: pure calendar/date tests.
- `./gradlew :app:testAcceptanceGenerator`: generator tests using Node built-ins.
- `./gradlew :app:generateAcceptance`: existing parser IR to generated Kotlin entrypoints.
- `./gradlew :app:acceptanceTest`: all 41 development UI acceptance cases on a connected dedicated offline AOSP test device without Play services.
- `./gradlew :app:acceptanceTest -PacceptanceFeature=CAL-001-F01`: run one feature; valid selectors end in F01, F02, F03.
- `./gradlew :app:lintDebug`: Android lint; correctness errors remain fatal, update advisories remain warnings.
- `./gradlew :app:assembleDebug`: build the debug APK.

Requires JDK 17, SDK platform/build-tools 35, and Node + the existing `parse-gherkin` on PATH for acceptance generation. See README for setup/output paths rather than duplicating them here. The API-35 development emulator is not Android-17 GrapheneOS verification.

## Development Conventions

- Work one task feature at a time and preserve stable feature/scenario/QA IDs. Update the existing task state, not a parallel task copy.
- Unit-test date logic before implementation; generated development acceptance must drive real UI actions/assertions and fail on unknown steps.
- Development acceptance may inject device inputs via `CalendarEnvironment`. External QA must use only the public GUI and device Settings; do not conflate the two.
- Keep the app free of network/calendar permissions and event storage in CAL-001. No Proton code/assets or new dependencies without approval.
- Compile/target SDK is currently 35 and minSdk is 26; do not describe either as owner-confirmed platform support. Only the API-35 emulator has been exercised.

## Values and Quality Criteria

- Prioritize privacy, offline operation, and minimalism.
- Keep dependencies minimal; ask before adding any dependency.
- Prefer simple solutions over speculative abstractions or decorative features.
- Keep event data local to the app. No cloud dependency, synchronization, or shared Android calendar storage in the current scope.

## Change Workflow

- Ask for confirmation before implementation changes; the precise boundary of approval is not yet defined. Creation of this instruction file was requested explicitly.
- Present the proposed change, dependencies, permissions, and verification approach before seeking approval.
- Do not commit or push; the owner handles both.
- Distinguish verified repository facts, owner-approved decisions, and proposals. Never silently turn a proposal into a requirement.
- After approved changes, report changed files, checks actually performed, and anything not verified. Do not claim device testing without performing it.

### Deliver every implementation to the owner's emulator

- After each approved app implementation, run the relevant checks, rebuild the debug APK, install it on the owner's persistent visible emulator, and open Calendar so the owner can test immediately. A temporary acceptance emulator does not satisfy this delivery requirement.
- The owner's current shared AVD is `android-dev` (renamed from `fake-msg`; it is not application-specific). Discover the live serial with `adb devices -l` and confirm its AVD name; do not assume a fixed port or accidentally target a read-only test instance or physical phone. If multiple matching instances exist, ask which one to use.
- Update in place with `adb -s <serial> install -r app/build/outputs/apk/debug/app-debug.apk`, then launch with `adb -s <serial> shell am start -n app.local.calendar/.MainActivity`. Replace `<serial>` with the verified owner-emulator serial. Check both commands succeed.
- Preserve emulator/app data: never uninstall, clear data, wipe the AVD, or change the signing identity to work around an installation failure without permission. Keep destructive tests and clock/network fixture changes on a separate test instance.
- Leave the owner's emulator running with the app open. If it is stopped, start the normal persistent visible AVD using `android start` (load `~/nixos-config/common/android-shell.sh` when needed), not a temporary/read-only instance. If the launcher is unavailable or deployment fails, report the blocker rather than claiming the app is ready to test.
- In the completion report, state which emulator received the build and whether installation and launch succeeded.

## Surfaces to Check

When implementing calendar behavior, include these in the verification plan:
- Consistency between month, week, and day views and event creation.
- Recurring versus one-time events, including recurrence edits and exceptions once their semantics are agreed.
- Reminder timing across month boundaries, time zones, daylight-saving changes, device restarts, and offline use.
- Notification and scheduling permissions, and behavior without Google Play services on the target GrapheneOS device. Do not request calendar permissions for app-private storage.
- Multiple reminders for recurring event occurrences: avoid duplicates and cancel or reschedule correctly when an event changes.

These are verification surfaces, not claims of implemented functionality or existing tests.

## Glossary

- "Every X days": event recurrence, not a separate repeating-notification feature.
- "1 month before": reminder offset of 30 days; do not subtract a calendar month.

## To Complete

- Select app-private storage technology during PERSISTENCE-001; obtain approval before adding any dependency beyond the approved initial set.
- Define month-cell overflow behavior, event fields, recurrence start/end rules, editing/deletion behavior, and all-day/time-zone handling.
- Specify day-based offset and recurrence behavior across daylight-saving changes.
- Confirm the minimum supported Android version; target device is recorded above.
- Clarify the granularity of approval and expected reporting.
- Establish CI, production APK signing, and Android-17 GrapheneOS device verification. Development commands exist above; external QA and Proton rendered comparison remain unverified.
- Decide backup/export needs and APK signing/key ownership before relying on the app for personal data or distributing updates.
