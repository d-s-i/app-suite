# EVENTS-001 — Events behavior specification

## Review status

All clarification decisions D1–D4 are approved, including D2 after the explanation of the repeated autumn hour. This document and the matching Gherkin/QA are **ready for owner review, not approval to code**. No production behavior, architecture or storage technology is selected by these artifacts.

## Confirmed human requirements

| Requirement | Human source |
|---|---|
| R01 Required title, optional description; all-day or timed; + is the only creation entry | Owner field answers and explicit rejection of empty-slot creation |
| R02 Automatic 1-hour timed duration, no duration/end controls; no manual multi-day date range | Latest duration decision superseding the intermediate no-duration model |
| R03 Form defaults, contextual date, explicit hour selection, save/cancel navigation | Approved D1 |
| R04 Events visible in Month/Week/Day; month titles, not dots; overflow exposes the full day | Original calendar/product decisions, carried into event integration |
| R05 Repeat every positive integer X days/weeks/calendar months | Owner recurrence answers; this repeats the event itself |
| R06 Free monthly numbered-date or ordinal-weekday rule, last-corresponding-date fallback without drift | Owner monthly clarification |
| R07 First match on/after the chosen lower bound; interval counted from that first match | Approved January 20 → February 1 → April 5 example |
| R08 End never, on an inclusive date, or after N including first | Owner ending-choice approval |
| R09 Edit/delete one occurrence, this and future, or whole series | Owner scope approval |
| R10 Preserve individual text overrides; confirmed reset for affected temporal/mode/recurrence changes | Approved D3, including warning about reappearing deleted occurrences |
| R11 Retained timed origin zone, current-phone-zone display, literal all-day dates | Owner approval after Proton source explanation |
| R12 Shift gaps, choose earlier overlap, label editing zone; 60 elapsed minutes | Approved D2 |
| R13 Automatic midnight crossing, one logical occurrence; exclusive end boundary | Approved D4 |
| R14 Fully local/private, offline, no Google Play services/shared Android calendars | Original project decisions |

Title and description are the requested content fields. Location, invitations, multiple calendars, synchronization, standalone notifications and other Proton features are not implicitly adopted.

## Vocabulary and exact rules

### Event and time

- A one-time event has a required nonblank title and an optional description. Whitespace-only input is not a title. A description may be empty.
- All-day means one literal calendar date, no requested hour. It keeps that date when the phone zone changes. No user-entered all-day date range.
- Timed means one chosen date and HH:mm start in the event's origin zone. Its end is start + **60 elapsed minutes**, not necessarily the next displayed wall-clock hour during DST. No editable duration, event end time or event end date. This does not forbid the separately approved recurrence ending date/count.
- Read-only event details expose saved content, all-day/timed mode and computed start/end information so the automatic interval can be verified through the public GUI. This is not a duration/end adjustment control.
- Display dates/times and overlap fragments use the current phone zone. A timed event can occupy two displayed dates, even though the creation form has only one starting date. An end exactly at midnight is exclusive and does not occupy the next date.
- One occurrence remains one logical occurrence regardless of how many date fragments render it. Deleting/editing that occurrence affects all its fragments. Notification delivery/deduplication belongs to REMINDERS-001, not this task.

### Creation and navigation

- + opens an empty-title/empty-description, one-time, all-day form. Switching to timed requires an explicit hour selection; do not silently adopt a current/default hour.
- Day defaults to its displayed date. Month/Week default to today if it belongs to the displayed month/week, otherwise the first date of that month/week. A neighbouring spillover date in a Month cell is not the first day of the displayed month.
- Creation Save returns to the originating view mode on the first resulting occurrence's display date. Editing Save returns to that mode on the selected occurrence if it remains, otherwise on the first resulting affected occurrence. Cancel/Back commits nothing and restores the original calendar period. A discard confirmation is allowed; error/discard wording is not fixed.
- Month date taps and Week date-header taps keep opening Day as specified by CAL-001. Empty timed slots do not create events. The + form, not a slot click, is the creation interface.
- Invalid mandatory input keeps the draft available and saves no event. A UI may prevent an invalid numeric value from being selected instead of accepting it and then showing an error; either way the invalid value must not become the saved rule. Do not substitute a default and claim an invalid-input test passed.

### Recurrence, boundaries and counts

- X and N are positive integers. Day recurrence advances X origin-calendar dates; week recurrence advances 7×X origin-calendar dates, keeping the weekday. Timed occurrences keep the requested local clock time in their retained origin zone.
- For monthly recurrence, freely choose day 1–31 or ordinal weekday (first through fifth, or last; Monday through Sunday). Find the first matching date on/after the lower bound, searching successive calendar months. Then use its month as the X-month interval anchor.
- Missing numbered date uses that target month's last date. Missing ordinal weekday uses its last matching weekday. The requested target stays unchanged: January 31 → February 28 → March 31, not March 28; fifth Monday may use a fourth Monday then return to a real fifth Monday.
- The inclusive ending date applies to nominal occurrence start dates in the origin calendar (literal dates for all-day). A midnight tail can therefore appear on the day after the last permitted start without becoming another occurrence.
- N counts scheduled positions, first included. Moving/deleting one instance does not generate a compensating tail. No artificial end is added to Never. The Gherkin tests Never using a finite later window rather than trying to enumerate infinity.
- An ending before the first matching date, including a free monthly rule whose first match is beyond its ending, cannot be saved as an invisible empty series.

### Local-time gaps and repeated hours

- Capture the phone zone when the form is opened for new timed information, including all-day-to-timed conversion. Existing timed editing uses its retained zone, labelled in the editor; do not silently replace it with the current display zone.
- Resolve a missing local start forward by the actual gap duration. Show the adjustment before saving. A series still requests its original clock time on normal dates; resolving one occurrence does not rewrite the rule.
- Resolve a repeated local start to the earlier instant and create one start only. Paris 2026-10-25 02:30 first occurs at 00:30 UTC; its 60-minute end is 01:30 UTC, which is the second local 02:30. All-day dates do not undergo this time resolution.
- These special-time choices are owner-approved app behavior, not a claim that the running Proton app was tested. Source/reference evidence is in reference-time-zones.md and approved decisions in remaining-decisions.md.

### Editing scopes and individual changes

- A series is identified by the user's creation/edit history, not by a title that must remain globally unique. An individual change records only changed fields; unmodified fields continue to inherit series values.
- A nominal occurrence is a scheduled series position before its individual move/customization. This-and-future follows nominal series order, not the chronological position of a moved instance on the current screen. Whole series includes past and future.
- One-occurrence edits/deletions do not change any other occurrence. A moved instance replaces its original position; it does not leave both original and moved copies.
- Series title/description edits keep local overrides: locally changed fields win; other fields inherit the new common value. Existing cancellations remain cancelled.
- Date, time, mode or recurrence edits reset affected individual changes/cancellations only after a warning and explicit confirmation. Warn that deleted instances may return. Rejecting the warning and abandoning the editor leaves the complete saved dataset unchanged. Outside the selected scope, nothing changes.
- Starting an edit from a later occurrence must not silently move the series start or alter its ending. For COUNT-ending future edits, positions before the selected boundary consume the original budget: a 5-position series edited from position 3 has 3 remaining positions unless the owner changes the ending count. An explicitly new future count starts at the first affected position. An unchanged UNTIL date stays the same date.

## Feature slices, criteria and traceability

Feature IDs abbreviate EVENTS-001-F01…F07. Scenario IDs below expand to EVENTS-001-Snn, QA IDs to QA-EVENTS-001-Snn. Process features sequentially in the shared workspace; no feature has started.

| Criterion | Exact acceptance outcome | Requirement | Feature / scenario | QA |
|---|---|---|---|---|
| AC-EVENTS-01 | Contextual date and empty one-time/all-day form, no range controls | R01–R03 | F01 / S01 | S01 |
| AC-EVENTS-02 | All-day save retains content and returns to originating mode/date | R01,R03 | F01 / S02 | S02 |
| AC-EVENTS-03 | Offline timed creation has an automatic 60-minute computed interval | R01,R02,R14 | F01 / S03 | S03 |
| AC-EVENTS-04 | Empty/whitespace title cannot create an event | R01 | F01 / S04 | S04 |
| AC-EVENTS-05 | Cancel/Back draft restores original period and creates nothing | R03 | F01 / S05 | S05 |
| AC-EVENTS-06 | Tapping empty timed slots never creates/opens creation | R01 | F01 / S06 | S06 |
| AC-EVENTS-07 | Saved all-day/timed data can be found in all three views | R04 | F02 / S07 | S07 |
| AC-EVENTS-08 | Simultaneous events remain independently accessible | R04; assumption A1 | F02 / S08 | S08 |
| AC-EVENTS-09 | Visible short titles plus overflow account for every event; full Day exposes all | R04 | F02 / S09 | S09 |
| AC-EVENTS-10 | Day/week recurrence spacing, first-inclusive count and fixed timed duration | R02,R05,R08 | F03 / S10 | S10 |
| AC-EVENTS-11 | Inclusive recurrence ending includes a matching last date, excludes next slot | R08 | F03 / S11 | S11 |
| AC-EVENTS-12 | Never remains present in a later bounded window | R08 | F03 / S12 | S12 |
| AC-EVENTS-13 | Nonpositive/noninteger/missing X cannot be accepted | R05 | F03 / S13 | S13 |
| AC-EVENTS-14 | Nonpositive/noninteger/missing N cannot be accepted | R08 | F03 / S14 | S14 |
| AC-EVENTS-15 | An ending before the first occurrence is rejected | R08 | F03 / S15 | S15 |
| AC-EVENTS-16 | Free numbered-month targets clamp without drift and anchor at first match | R06,R07 | F04 / S16 | S16 |
| AC-EVENTS-17 | Free ordinal/last weekday, including absent fifth, respects X-month spacing | R06,R07 | F04 / S17 | S17 |
| AC-EVENTS-18 | Monthly ending before its first match cannot create an empty series | R07,R08 | F04 / S18 | S18 |
| AC-EVENTS-19 | An inclusive clamped monthly ending is represented once | R06,R08 | F04 / S19 | S19 |
| AC-EVENTS-20 | One-time edit changes saved data without an old copy | R09 | F05 / S20 | S20 |
| AC-EVENTS-21 | All-day/timed conversion creates no duplicate or duration control | R01,R02,R09 | F05 / S21 | S21 |
| AC-EVENTS-22 | Only the selected recurring position moves/customizes | R09 | F05 / S22 | S22 |
| AC-EVENTS-23 | Scoped text changes preserve local fields and deletions | R09,R10 | F05 / S23 | S23 |
| AC-EVENTS-24 | Confirmed temporal reset affects only the chosen portion and can restore cancellations | R09,R10 | F05 / S24 | S24 |
| AC-EVENTS-25 | Rejecting reset and abandoning commits no changes | R10 | F05 / S25 | S25 |
| AC-EVENTS-26 | Interval edits preserve the unchanged remaining count budget | R09,R10 endpoint invariant | F05 / S26 | S26 |
| AC-EVENTS-27 | An explicitly changed future count includes its first affected occurrence | R08,R09 | F05 / S27 | S27 |
| AC-EVENTS-28 | One-time deletion removes only its target in every view | R09 | F06 / S28 | S28 |
| AC-EVENTS-29 | Recurring deletion obeys one/future/all scope without extending the tail | R08,R09 | F06 / S29 | S29 |
| AC-EVENTS-30 | Cancelling deletion preserves all saved occurrences | R09 | F06 / S30 | S30 |
| AC-EVENTS-31 | Moved instances remain members of their nominal deletion scope | R09,R10 | F06 / S31 | S31 |
| AC-EVENTS-32 | Display converts dates/hours but edit uses labelled origin zone | R11,R12 | F07 / S32 | S32 |
| AC-EVENTS-33 | Origin-local recurrence clock stays fixed across different DST calendars | R11 | F07 / S33 | S33 |
| AC-EVENTS-34 | All-day dates stay literal across extreme display-zone shifts | R11 | F07 / S34 | S34 |
| AC-EVENTS-35 | New timed information added to all-day uses the current phone zone | R11,R12 | F07 / S35 | S35 |
| AC-EVENTS-36 | Gap adjustment is visible, preserves the rule clock, and does not add a start | R12 | F07 / S36 | S36 |
| AC-EVENTS-37 | Autumn repeated hour creates only the earlier start, lasting 60 minutes | R02,R12 | F07 / S37 | S37 |
| AC-EVENTS-38 | Midnight overlap/exclusive end renders fragments of one occurrence | R02,R13 | F07 / S38 | S38 |
| AC-EVENTS-39 | Timed hour selection is explicit, not an implicit default | R03 | F01 / S39 | S39 |
| AC-EVENTS-40 | Date/mode/recurrence changes also require consent before resetting exceptions | R10 | F05 / S40 | S40 |

Existing CAL-001-S04/S08 retain the approved date-to-Day navigation; they are not duplicated under new IDs here. They must be included in regression after implementation. All current EVENTS criteria map to the matching scenario and QA procedure. No scenarios are generated for architecture/library choices.

## Assumptions and presentation freedom

- A1: No conflict-prevention or unique-title rule was requested. Independent valid events, including simultaneous ones, are accepted. Distinct fixture titles make their public details identifiable. Grouping is permitted only when every underlying event remains reachable; it is not deduplication.
- A2: Description is plain text. Input precision is minutes, and test language/format are English United States / 24h unless a case changes them. Text/error wording, field widget type and nonessential visual styling are not fixed by this task.
- A3: The month cell's exact visible capacity/order is not fixed here. Short fixture titles must be represented by at least one visible title plus a correct hidden count; when all fit, no overflow is required. QA counts distinct events, not text lines. No arbitrary event-font size, cell capacity or performance threshold is invented.
- A4: Every X weeks uses the starting weekday; multiple weekdays per week and a separate yearly control were not requested. Twelve months is a valid monthly interval, not a new yearly feature.
- A5: Numerical validation may be preventive (e.g. a control cannot select zero) or show an error after entry. Tests must actually verify the requested value is refused, not silently ignore it. No Proton server-specific year/count limits are adopted.
- A6: Time-zone fixtures rely on the device's installed zone rules. Europe/Paris and named conversion zones must be available and match the supplied offset fixtures; if not, classify setup as ENVIRONMENT_ERROR rather than rewriting expected values. No user phone clock/network is changed by this document.

These assumptions are visible for owner review, not hidden architecture decisions. No remaining acceptance-critical product question is open; final specification approval is still required before implementation.

## Dependencies and deliberately separate requirements

- PERSISTENCE-001 owns durable app-private storage, restart/update retention, migrations, storage errors and backup/export. Fully local/private storage is confirmed, not permission to use Android Calendar Provider or an implicit in-memory-only product decision. EVENTS tests assume saved data remains available during their public UI flows, including leaving for Settings. Coordinate the storage contract/implementation before claiming a usable release; no store technology is chosen here. Restart-specific QA belongs to PERSISTENCE-001 rather than a duplicate EVENTS feature.
- REMINDERS-001 owns reminder controls/delivery/rescheduling. Multiple offsets and the product's fixed 30-day "one month before" are not monthly recurrence arithmetic and are not redefined here. No notification-delivery scenario is added to EVENTS.
- CAL-001 owns the empty calendar layout/navigation; this task populates it and adds +/details/edit/delete behavior without unrelated redesign. Release signing, GitHub publication and Obtainium installation are unchanged.

## Verification boundary

The fully local/no-Calendar-Provider/no-network-permission constraints also require the coder's manifest/code review. An offline GUI scenario alone cannot prove absence of a hidden provider write or a declared permission; its behavioral portion is covered by S03, while private-storage/permission verification must be recorded in the implementation/PERSISTENCE checks. This explains why those security constraints do not get a fake private-state Gherkin assertion.

`checks/specifier.md` records actual parser/DRY/static results. Those are specification checks, not app tests. App execution, GrapheneOS external QA and executable QA scripts are not performed by the specifier. Stable feature IDs are registered as not_started, current_feature stays null, and state is ready_for_review only after document/check review.
