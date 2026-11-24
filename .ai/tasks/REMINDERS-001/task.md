# REMINDERS-001 — Deliver multiple local advance reminders for events

## Original request

"create these 4 tasks CAL-001 EVENTS-001 REMINDERS-001 PERSISTENCE-001" — REMINDERS-001 represents the previously discussed reminders capability.

## Objective and affected user

Notify the owner before personal event occurrences, with several selectable reminders per event, without a network connection or Google Play services.

## Verified context and confirmed constraints

- No application implementation or build/test configuration exists at initialization; AGENTS.md records product decisions.
- Kotlin + Jetpack Compose; Pixel 9 Pro with Android 17 as reported by the owner; device behavior is not yet tested.
- Multiple reminders per event. Advance offsets: 1 hour, 4 hours, 1 day, 3 days, 1 week, 2 weeks, and 1 month.
- "1 month before" means subtract 30 days, not a calendar month.
- Every-X-days repetition belongs to event recurrence in EVENTS-001, not a separate repeating-notification feature.
- Fully local, app-private storage; no Android Calendar Provider, Google integration, or Google Play services.
- Prioritize privacy, offline operation, minimalism, free use, and APK installation.
- Obtain approval before implementation or adding dependencies; no agent commits or pushes.

## Proposed scope

- Configure multiple advance reminders for an event and its recurring occurrences.
- Local notification delivery and clear handling of required permissions.
- Update/cancel scheduled reminders when an event or reminder changes or is deleted.
- Restore appropriate scheduling after restart and address time changes without duplicate notifications.
- Define behavior for denied permissions, unavailable scheduling capabilities, and elapsed reminder times during specification.

## Proposed out of scope

Standalone repeating notifications, event recurrence authoring, remote notifications, email/SMS, cloud services, and calendar layout.

## Assumptions

- This is a capability-sized task; the specifier may split it into sequential features within this directory.
- Recurring occurrences come from EVENTS-001; reminder settings are retained through PERSISTENCE-001.

## Unresolved questions for specification

- What delivery timing tolerance is acceptable, and what should the app show if reliable scheduling is unavailable?
- Should past-due reminders be skipped or delivered after downtime or event creation?
- How should day/week/30-day offsets behave across daylight-saving changes?
- What notification content and sound behavior should be used for privacy and minimalism?

## Architecture question

Which local scheduling mechanism meets the agreed timing and restart behavior on the target device with the fewest justified dependencies? Do not choose a mechanism during initialization.

These details remain undefined; they do not prevent beginning specification. Seek owner decisions before finalizing their behavior.

## Relationships and next role

Coordinate with EVENTS-001 for occurrences and PERSISTENCE-001 for durable reminder settings. Suggested next role: specifier, to define sequential features, acceptance criteria, Gherkin, and QA procedure. No acceptance criteria or architecture is finalized here.
