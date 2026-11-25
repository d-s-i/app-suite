# EVENTS-001 — Specifier validation report

## Result

Specification **ready_for_review**, not ready_for_coding. Seven APS-subset feature files, 40 scenario definitions / 95 expanded cases, 40 matching public-GUI QA procedures. This is syntax/document validation only, not event implementation or a device PASS.

## Commands actually executed

For every feature stem below, executed both commands:

```sh
parse-gherkin .ai/tasks/EVENTS-001/features/<stem>.feature .ai/tasks/EVENTS-001/acceptance/<stem>.json
check-gherkin-dry .ai/tasks/EVENTS-001/acceptance/<stem>.json .ai/tasks/EVENTS-001/acceptance/<stem>-dry-report.json
```

The actual stems were `creation`, `calendar-display`, `recurrence-days-weeks`, `recurrence-months`, `editing`, `deletion`, and `time-zones-and-boundaries`. `<stem>` documents those concrete invocations, not a literal argument. All 14 final invocations exited 0. Both checks were rerun on all files after Gherkin edits; the final QA observation blocks were resynchronized from the final parser IR.

## Advisory Gherkin DRY review

Final findings: **93 advisory findings**, reviewed, not suppressed or claimed absent:

| Feature | Findings |
|---|---:|
| F01 creation | 16 |
| F02 calendar display | 6 |
| F03 day/week recurrence | 9 |
| F04 monthly recurrence | 5 |
| F05 editing | 27 |
| F06 deletion | 7 |
| F07 zones/boundaries | 23 |

Kinds: 3 duplicate-in-scenario, 18 near-duplicate, 72 possible-synonym; no placeholder-variant finding.

Review and revisions:
- Removed constant/redundant title-validation Examples columns. Missing-hour confirmation is a distinct story S39, not an assumption that every widget permits an invalid raw timed state.
- Normalized accidental `still represented` / `represented` and series-absence vocabulary; preserved the distinction between named event titles and the originally created series after titles change.
- Split the combined cancel/zone-change action in S35 and made reopening Edit an explicit When before the zone assertion.
- Made editing Save attempts explicit, especially before reset warnings; did not silently assume typing already commits.
- Kept the three duplicate-in-scenario findings intentionally: S03 checks Day before/after creation; S05 checks Month before/after abandoning; S32 checks converted representation before/after cancelling Edit. These are different transitions, not repeated accidental actions.
- Literal versus parameterized dates/zones/views are the same canonical vocabulary. Given setup, When UI action and Then observation must not be merged merely because their words overlap.
- End-by-count, end-by-date, Never, numbered-month and ordinal-weekday creation have different behavior despite similar setup text. Separate expectations remain meaningful.
- S40's structural-field actions resemble text editing lexically but must test the different warning/reset policy. Do not merge those cases into a handler that ignores field/value or scope.
- Presence/absence, old/new dates, original/display zones and interval/count controls are deliberately different inputs/assertions, not synonyms to combine.

## Static consistency and fixture checks

An inline Node assertion check consumed parser IR plus the authored documents; no utility, QA runner or production code was added. It verified:
- Unique feature/scenario IDs; exactly one fully structured QA section per scenario.
- Criterion/feature/scenario/QA references match.
- Every Examples parameter is supplied and used; no constant Examples columns.
- ISO dates, day/week spacing and inclusive end fixtures.
- Monthly spacing, clamping, actual weekdays and ordinal/last-weekday fixtures.
- Midnight +60-minute intervals and exclusive end-date visibility.
- Named-zone offsets/DST conversion fixtures using host Intl, including Paris gap/fold and Honolulu previous-date display.

Details: `specification-consistency.json`. Host date/time checks do not prove a phone's installed zone database or UI behavior.

## Scope and unverified items

No application changes, unit tests, acceptance runtime/handlers, executable QA scripts, architecture, library/store choice, CRAP/source DRY, mutation tests, device installation or app QA were performed. No commits/pushes. Existing CAL-001/signing work is untouched.

PERSISTENCE-001 remains responsible for durable storage/restart/migration/error/backup behavior and must be coordinated before a usable event release. REMINDERS-001 owns notification behavior. Manifest/code review must verify private/no-provider/no-network constraints that UI tests alone cannot prove. No in-memory-only product decision is implied.

Next gate: owner reviews the complete specification, assumptions A1–A6 and feature/QA files. Only explicit approval permits implementation. All seven features are registered not_started, current_feature null; coder remains previously blocked until approval and adaptation of the existing CAL-only acceptance adapter. No second parser/runner is proposed.
