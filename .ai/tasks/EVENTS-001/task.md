# EVENTS-001 — Create, edit, and delete one-time and recurring events

## Original request and objective

"create these 4 tasks CAL-001 EVENTS-001 REMINDERS-001 PERSISTENCE-001". The owner then invoked the specifier for EVENTS-001 and clarified event fields, recurrence, scopes, time zones and the four remaining decisions. Let the owner manage custom personal events locally and find them in the calendar.

## Confirmed decisions

- Required title, optional description; all-day on one date or timed at one selected date/hour. Timed duration is automatically 1 hour, without duration/event-end controls. This is the latest decision and supersedes the intermediate no-duration model.
- Creation only through +. No creation by tapping empty timed slots; existing month-date and week-date-header navigation to Day remains.
- Default creation is one-time/all-day with contextual date. Save/cancel navigation follows approved D1 in remaining-decisions.md.
- Recurrence every positive integer X days/weeks/calendar months. Months are calendar months, not fixed 30-day spans.
- Monthly numbered date or ordinal weekday is freely selected. Missing targets use the last corresponding date without later drift. The lower bound finds the first matching occurrence; its month anchors the X-month interval.
- Ending: never, inclusive date, or N occurrences with the first included.
- Edit/delete scope: one occurrence, this and future, whole series. Approved D3 preserves individual text overrides and requires consent before structural resets of affected changes/cancellations.
- Timed events keep their creation/origin zone and requested local recurrence clock; display converts to current phone zone. All-day dates remain literal. Approved D2 resolves gaps forward and repeated times to the earlier instant, with the original zone shown during editing.
- No manual multi-day date range. Approved D4 allows the automatic hour to cross midnight, with one logical occurrence and an exclusive end.
- Fully local/app-private; no Android Calendar Provider, Google integration or Play services. Free to use, offline, minimal dependencies. Pixel 9 Pro / Android 17 GrapheneOS is owner-reported target, not agent-verified event support.

All D1–D4 decisions are approved. The written specification is still subject to owner review before implementation.

## Behavior slices and artifacts

- EVENTS-001-F01: creation, defaults, mandatory input, explicit time and cancellation.
- EVENTS-001-F02: calendar representation, simultaneous access and month overflow.
- EVENTS-001-F03: day/week recurrence and ending validation.
- EVENTS-001-F04: free monthly patterns, first match and clamping.
- EVENTS-001-F05: one-time/occurrence/series edits, inherited fields and confirmed reset.
- EVENTS-001-F06: deletion scopes, cancellation and moved-occurrence membership.
- EVENTS-001-F07: origin/display zones, DST, mode conversion and midnight overlap.

Source of truth: this task context plus owner-approved decisions. `specification.md` contains the precise contract, criteria, explicit assumptions and requirement → scenario → QA mapping. `features/` contains seven features; `qa/procedure.md` contains public-GUI procedures. `checks/specifier.md` records actual deterministic checks. `remaining-decisions.md` records D1–D4; `reference-time-zones.md` separates inspected Proton evidence from this app's choices.

## Boundaries and dependencies

Populate existing calendar views without unrelated CAL-001 redesign. No reminders, invitations/location fields, multiple calendars, cloud sync, source/assets import, release changes or architecture selection.

PERSISTENCE-001 owns durable storage technology, restart/update retention, migrations, storage errors and export/backup. Fully local durable product data is not waived and no in-memory-only product decision is made here. Coordinate storage before claiming a usable event release. REMINDERS-001 owns reminder controls/delivery; monthly recurrence is not the fixed 30-day reminder offset.

The app currently implements calendar views/navigation only; no event behavior or EVENTS acceptance runtime exists yet. The existing parser/generator/test runner must be extended rather than duplicated by the coder. No Gherkin handlers/runtime or production tests were implemented during specification.

## Assumptions and open questions

Presentation freedom and explicit noncritical assumptions A1–A6 are listed in specification.md. No remaining acceptance-critical product question. No library/store/UI framework decision is inferred from the specification.

Next action: owner reviews the complete artifacts. Status is ready_for_review, never ready_for_coding without explicit approval. All features remain not_started; do not commit or push.
