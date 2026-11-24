# EVENTS-001 — Create, edit, and delete one-time and recurring events

## Original request

"create these 4 tasks CAL-001 EVENTS-001 REMINDERS-001 PERSISTENCE-001" — EVENTS-001 represents the previously discussed events capability.

## Objective and affected user

Let the owner manage custom personal events, including events that occur every X days, for display in the calendar and use by reminders.

## Verified context and confirmed constraints

- No application implementation or build/test configuration exists at initialization; AGENTS.md records product decisions.
- Kotlin + Jetpack Compose; Pixel 9 Pro with Android 17 as reported by the owner.
- Fully local app-private data, no Android Calendar Provider, Google integration, or Google Play services.
- One-time and recurring events are confirmed, including every X days. This repeats the event, not only a notification.
- Multiple advance reminders per event are confirmed; REMINDERS-001 owns their configuration and delivery.
- Prioritize privacy, offline operation, minimalism, and free use; install by APK.
- Obtain approval before implementation or adding dependencies; no agent commits or pushes.

## Proposed scope

- Event creation, details, editing, and deletion.
- Define event fields and input validation during specification.
- Configure recurrence every X days and determine occurrences for calendar views and reminders.
- Define recurrence start/end behavior and the effects of editing/deleting recurring events.
- Coordinate persistence with PERSISTENCE-001 and reminder controls with REMINDERS-001.

## Proposed out of scope

Calendar layout, notification scheduling/delivery, storage technology, synchronization, shared calendars, and unconfirmed recurrence patterns.

## Assumptions

- This is a capability-sized task; the specifier may split it into sequential features within this directory.
- Event rules are shared by views and reminder behavior rather than independently redefined by each task.

## Unresolved questions for specification

- Which event fields are required or optional, and are all-day events needed?
- Does editing/deleting a recurring event affect one occurrence, future occurrences, or the whole series?
- What recurrence end options are needed?
- How do event time zones, daylight-saving changes, and every-X-days recurrence interact?

These details remain undefined; they do not prevent beginning specification. Seek owner decisions before finalizing their behavior.

## Relationships and next role

CAL-001 displays occurrences, PERSISTENCE-001 stores event data, and REMINDERS-001 attaches reminders. Suggested next role: specifier, to define sequential features, acceptance criteria, Gherkin, and QA procedure. No acceptance criteria or architecture is finalized here.
