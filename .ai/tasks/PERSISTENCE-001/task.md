# PERSISTENCE-001 — Preserve events and reminder settings in app-private local storage

## Original request

"create these 4 tasks CAL-001 EVENTS-001 REMINDERS-001 PERSISTENCE-001" — PERSISTENCE-001 represents the previously discussed local data capability.

## Objective and affected user

Keep the owner's calendar data available offline and retain it across app restarts, device restarts, and compatible APK updates.

## Verified context and confirmed constraints

- No application implementation or build/test configuration exists at initialization; AGENTS.md records product decisions.
- Kotlin + Jetpack Compose; Pixel 9 Pro with Android 17 as reported by the owner.
- Fully local app-private event storage. Do not use Android Calendar Provider, Google Calendar integration, synchronization, or Google Play services.
- Persist one-time/recurring event definitions and multiple reminder settings, with semantics owned by EVENTS-001 and REMINDERS-001.
- Prioritize privacy, offline operation, minimalism, free use, and APK installation.
- Storage technology and dependencies are not selected.
- Obtain approval before implementation or adding dependencies; no agent commits or pushes.

## Proposed scope

- Store and retrieve events, recurrence definitions, and reminder settings within the app's private data.
- Support durable creation, update, and deletion for the other capabilities.
- Preserve data through normal process/device restarts and compatible app updates.
- Define visible failure behavior for unsuccessful storage operations and a policy for data-format changes during specification.

## Proposed out of scope

Android shared calendar storage, cloud/network synchronization, event UI, notification delivery, and backup/export unless separately approved. Do not promise recovery after uninstall or clearing app data.

## Assumptions

- This is a capability-sized task; the specifier may split it into sequential features within this directory.
- Data preservation across APK updates depends on compatible installation identity/signing; this task does not authorize managing release keys.

## Unresolved questions for specification

- Should backup/export be added, or remain outside this task?
- What platform backup policy is consistent with the owner's fully local privacy requirement?
- What should the user see after a failed write, unreadable data, or an unsupported data version?

## Architecture question

Which app-private storage technology provides the agreed durability with minimal dependencies? No database or library is chosen here.

These details remain undefined; they do not prevent beginning specification. Seek owner decisions before finalizing their behavior.

## Relationships and next role

EVENTS-001 owns event semantics; REMINDERS-001 owns reminder semantics; CAL-001 displays the resulting data. Suggested next role: specifier, to define sequential features, acceptance criteria, Gherkin, and QA procedure. No acceptance criteria or architecture is finalized here.
