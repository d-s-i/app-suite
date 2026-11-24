# CAL-001 — Empty calendar views specification

## Status and confirmed scope

Implementation and the subsequent Back/arrow revision were explicitly requested by the owner. `ui-revision.md` records the later decision, retired IDs, and its precedence over the initial Proton picker reference. The owner explicitly confirmed empty calendar views/navigation only, use of official Proton Calendar source, and the latest release as reference. `reference.md` pins the observed latest official APK/source to 2.34.2 (361), commit 113b3df2a64cc2d95443ac962216e93f3710a2ca.

Confirmed requirements:
- Month, Week, Day with Proton Calendar Android as the calendar UI reference.
- Initial Month view, Monday week start, and device-locale date formatting.
- Fully local operation without Google Play services.
- No event functionality in this task. Event titles and +N overflow remain future product requirements, not acceptance checks in CAL-001.

## Independently testable slices and acceptance criteria

Stable scenario IDs occur in feature scenario names; matching QA IDs are in `qa/procedure.md`. Scenario Outline rows are test cases of the same stable scenario; reports should also record the input row values, not invent new feature IDs.

| Human requirement / reference evidence | Acceptance criterion | Feature / scenario | QA procedure |
|---|---|---|---|
| Initial Month, Monday, offline; source month geometry | AC-CAL-01: Fresh launch without network or Play services opens an empty current month with a Monday-first 7x6 consecutive-date grid and no account/network gate. | F01 / CAL-001-S01 | QA-CAL-001-S01 |
| Same empty month UI; pinned month drawing/resources | AC-CAL-02: Equal date cells, separators, typography dimensions, muted adjacent-month dates, and today's outlined number follow the extracted reference measurements. | F01 / CAL-001-S02 | QA-CAL-001-S02 |
| Calendar navigation; source month pager | AC-CAL-03: Horizontal month paging produces exactly the adjacent month and its correct 42-date range, including leap/non-leap February and year transitions. | F01 / CAL-001-S03 | QA-CAL-001-S03 |
| Same date interactions; source month click handler | AC-CAL-04: Tapping any visible date, including an adjacent-month date, opens its empty Day view. | F01 / CAL-001-S04 | QA-CAL-001-S04 |
| Device-locale dates; source title formatter | AC-CAL-05: Month titles use device-locale names, omitting the current year and including another year, while Monday remains first. | F01 / CAL-001-S05 | QA-CAL-001-S05 |
| Same Week/Day views; pinned layout/constants | AC-CAL-06: Drawer selection opens 7-column Week or 1-column Day, with dates, left-side time labels, hour separators at default 60dp spacing. | F02 / CAL-001-S06 | QA-CAL-001-S06 |
| Same timed grid; source renderer | AC-CAL-07: Scrolling to the top exposes 01:00, the reference's first labeled hour, and scrolling to the bottom exposes 23:00 without changing dates. | F02 / CAL-001-S07 | QA-CAL-001-S07 |
| Same week header interaction | AC-CAL-08: Tapping a Week date header opens that date's Day view. | F02 / CAL-001-S08 | QA-CAL-001-S08 |
| Owner removed the picker | AC-CAL-09 / CAL-001-S09 / QA-CAL-001-S09 retired; see ui-revision.md. | — | — |
| Same device-aware time formatting | AC-CAL-10: Hour labels follow the device 12h/24h setting. | F02 / CAL-001-S10 | QA-CAL-001-S10 |
| Same period navigation; Monday-first override | AC-CAL-11: Complete horizontal timed-period paging moves by seven days in Week or one day in Day, correctly across month/year boundaries. | F03 / CAL-001-S11 | QA-CAL-001-S11 |
| Same Today control | AC-CAL-12: Today returns the active mode to the current day/containing week/containing month and shows today's day number. | F03 / CAL-001-S12 | QA-CAL-001-S12 |
| Owner removed the picker | AC-CAL-13 / CAL-001-S13 / QA-CAL-001-S13 retired; see ui-revision.md. | — | — |
| Same mode switching | AC-CAL-14: Choosing Month from Day opens the month containing the Day date and closes the drawer. | F03 / CAL-001-S14 | QA-CAL-001-S14 |
| Same current-time indicator; pinned layout | AC-CAL-15: Today's timed column has a line/dot at the device's current local time. | F03 / CAL-001-S15 | QA-CAL-001-S15 |
| Owner's Back request | AC-CAL-16: System Back from Day opens the containing Month without exiting the app. | F03 / CAL-001-S16 | QA-CAL-001-S16 |
| Owner's arrow-removal request | AC-CAL-17: Week/Day show no month-expansion control or dropdown picker and retain their time grids. | F02 / CAL-001-S17 | QA-CAL-001-S17 |
| Preserve drawer Back behavior | AC-CAL-18: Back closes an open drawer before returning Day to Month. | F03 / CAL-001-S18 | QA-CAL-001-S18 |
| Approved Day source correction | AC-CAL-19: Day weekday/date use the left time column with 12sp/16sp typography, no extra weekday strip, and the global month title retained. | F02 / CAL-001-S19 | QA-CAL-001-S19 |
| Approved hour-axis correction | AC-CAL-20: Day hours use 12sp, center on hour lines, and determine the time-column width with 10dp padding each side. | F02 / CAL-001-S20 | QA-CAL-001-S20 |
| Approved initial-time correction | AC-CAL-21: Day initially shows one hour before now, bounded by midnight and the scroll range. | F02 / CAL-001-S21 | QA-CAL-001-S21 |
| Owner approved header dividers in Day and Week | AC-CAL-22: The time-column separator extends through the full date header, aligned with the grid boundary; the header bottom line spans the full width including the time column, independently of vertical scrolling. Today outlines are retained. | F02 / CAL-001-S22 | QA-CAL-001-S22 |

Feature IDs above abbreviate CAL-001-F01, CAL-001-F02, CAL-001-F03. Process sequentially in one workspace. These are behavior slices, not a module or implementation design.

## Boundaries

The approved Day-only correction and its pinned-source evidence are in `day-reference-correction.md`. Week styling and the global month toolbar are retained. No screenshot is required to establish the verified source-level behavior; rendered parity remains a separate unperformed check.

No event creation/editing/deletion, event rendering, recurrence, notification delivery, storage implementation, cloud integration, account UI, 3 Days or Agenda mode, optional week numbers, or search is specified here. Event-populated requirements cannot yet be exercised through a public interface and are explicitly deferred to subsequent specification work, not counted as passing.

No requirement for persisting a selected date/view across relaunch is established. First-launch defaults do not imply that every later launch resets to Month. No requirement to retain an exact selected day when switching into Week is invented; Week must show the containing Monday-Sunday range.

## Assumptions and review needs

- Reference-derived behaviors in the traceability table operationalize the owner's explicit request to use Proton's views; they have not been individually approved as a finished design. Owner review of this extracted contract is required before coding.
- "Latest" is frozen to the verified version rather than changing underneath acceptance runs.
- Gregorian date fixtures and a UTC device clock make date-boundary tests reproducible. These fixtures do not add a user-facing clock/date-setting feature.
- Primary QA uses a portrait Pixel 9 Pro / Android 17 GrapheneOS environment, light appearance, default display/font scaling, with automatic date/time disabled temporarily. These are QA conditions, not claims that other settings are unsupported.
- Reference account/event controls are excluded because the owner limited this task to empty local calendar views/navigation. No Proton branding, source, or asset reuse is authorized by this specification.
- Source inspection established structural behavior, not rendered pixel parity. A reference capture session is still needed to execute visual QA; passing Gherkin parsing is not evidence of visual fidelity. If review requires additional exact colors/assets/spacing beyond this extracted contract, amend the specification before coding rather than treating broad visual similarity as a pass.

## Unresolved questions

No remaining blocking product question for the extracted empty-calendar contract. Implementation and the documented UI revision are approved; rendered reference parity remains unverified. Screenshot comparison and device execution are not performed and must not be reported as passed. Backup, storage, event semantics, and reminder questions belong to their respective tasks.

## Validation

See `checks/specifier.md` and state.json for actual Gherkin checks. Parser output is validation IR only, not an executable acceptance runtime. No production code, QA scripts, source-quality checks, or architecture decisions are part of this phase.
