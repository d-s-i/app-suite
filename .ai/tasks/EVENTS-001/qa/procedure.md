# EVENTS-001 — Public-interface end-to-end QA

## Environment, determinism and evidence

- All procedures are NOT_RUN at specification time; no EVENTS implementation/candidate exists yet. Test a supplied candidate claiming the feature: missing/broken product functionality is FAIL, not a setup excuse. No executable QA scripts are created here.
- Use a dedicated offline device/test environment without Play services or accounts, preferably the target Pixel 9 Pro / Android 17 GrapheneOS. Record device/OS/APK identity, locale, density/font scale, hour format, phone zone, clock date/time and installed zone-rule data. API-35 development acceptance is not GrapheneOS external QA.
- Initial calendar must be empty. Establish it using app GUI deletion of identified QA data; a fresh installation or Android Settings storage reset is allowed only on an operator-approved dedicated fixture. Never clear/uninstall a personal phone or owner emulator as a default setup.
- Default observation language: English United States, 24h, portrait, default display/font scale; all events/data in these procedures are synthetic. F01 requires phone date 2027-01-20 UTC for default-date assertions. Configure dates/zones/hour format only through public device Settings on the dedicated fixture; restore originals afterward. Do not mutate the owner's real appointment clock/network.
- Set the feature's starting zone: UTC for F01–F06, Europe/Paris for F07; cases that explicitly change it override this. No Google account, internet or shared Android calendar may be required to create the data.
- Run each Scenario Outline row independently. Inputs and exact expected values come from that scenario's Examples table; substitute identically named row values in these instructions/observations. Record the row values alongside the stable QA/scenario ID. Do not skip a row or ignore an input.
- Dates use ISO notation here to identify public GUI dates, not to mandate literal ISO UI strings. View labels and widgets may be localized; use their public meaning. There is no time limit/performance benchmark.
- All observations require the rendered app GUI: calendar, details, editor or public scope/confirmation dialog. No database reads/writes, injected event repositories, internal flags, test-only APIs, private functions, direct recurrence-engine calls or app-private CLI. Even Given fixtures must be created through +.
- Computed interval/duration assertions are read-only event-details observations; if necessary open/close details for each occurrence. This does not require editable duration/end controls. Representation assertions may navigate the public calendar to the stated dates; navigation-return assertions must be checked immediately after Save/Cancel before inspecting other dates.
- Preserve shared app-private data during the flow, including Settings trips. A candidate that loses saved data during required UI flows fails integration; do not silently reseed it. Restart/migration/storage failures are separately owned by PERSISTENCE-001, whose implementation must be coordinated before a usable release.

## Public setup/navigation vocabulary

1. To reach a date: select Month in the navigation drawer, swipe previous/next until month/year matches, tap the date to open Day, and select Week when required. Do not assume a private date-jump command. For Month input view_date, display that month; for Week, its Monday-Sunday range; for Day, that exact date.
2. Create one event: open +, enter the exact title/description (empty if unspecified), choose date/mode, explicitly confirm the given hour for timed, choose recurrence/ending if specified, then Save. A setup phrased timed without a given hour (Switch/Remove fixtures) uses 09:30; this is a fixture value, never an app default.
3. Create Course series: through + set Course, description Base unless otherwise stated, date/start 09:00, daily interval 1 and count 5 for June fixtures. Inspect the result before testing edits. Use the scenario's date/interval/ending for other series.
4. Choose edit/delete scope through the app's public detail/action UI, at the point the UI offers it. For edits, set only the fields stated; do not re-enter unrelated values and accidentally turn them into individual overrides. Save means complete the public Save flow. For reset tests stop at the warning until the procedure explicitly confirms/cancels.
5. Named Course original-series assertions track the fixture's history even after titles change or an instance moves. Navigate to the known actual dates/times to inspect it, not an internal ID or an old-title-only search. Nominal dates mean original positions before individual moves.
6. Numeric invalid-input rows: empty means clear the field if possible; other cells are literal values. If the public control prevents an invalid entry, verify that restriction/options/disabled boundary; do not invent an invalid private state or substitute a valid value. Capture rejection evidence.

## Course exception fixture (S23–S25)

Create Course daily at 09:00 UTC from 2027-06-01, ending after 5, description Base. Then through the GUI:

1. Edit only June 2: title Custom, description Local; leave date/hour unchanged.
2. Edit only June 3: hour 11:00; leave title/description/date unchanged.
3. Delete only June 4 and confirm.

Verify the baseline before each case:

| Nominal date | Visible date/time | Title | Description |
|---|---|---|---|
| 2027-06-01 | June 1 09:00 | Course | Base |
| 2027-06-02 | June 2 09:00 | Custom | Local |
| 2027-06-03 | June 3 11:00 | Course | Base |
| 2027-06-04 | absent | — | — |
| 2027-06-05 | June 5 09:00 | Course | Base |

The June 3 hour-only change must not freeze its inherited title/description. No June 6 start exists. Use a fresh fixture per Examples row.

## Classification and cleanup rules

- PASS: every expected observation for that case/row verified publicly.
- FAIL: setup is valid and a product result contradicts any observation, including a crash, saved invalid input, silent data loss, hidden event or wrong scope.
- ENVIRONMENT_ERROR: wrong/unavailable device/build installation, required Settings/time-zone control unavailable, or initial fixture cannot be established for a reason external to the claimed candidate. Missing product controls/behavior are not automatically environment errors.
- NOT_RUN: not attempted.

Record feature/scenario/QA ID, row values, initial/setup state, actual actions/results, screenshots/details and exact failure/setup reason. No PASS from parser success or private-state assertions. Cleanup uses public GUI: delete only the known QA-created one-time events; delete the whole original QA series from a remaining occurrence where necessary, including moved/customized members. If a case deleted everything, verify absence. Restore device settings and record cleanup failures; never erase unrelated user data.

# QA-EVENTS-001-S07 — Find all-day and timed events in each calendar view

## Purpose

Read saved all-day and timed data from all views.

Gherkin feature: EVENTS-001-F02. Scenario: EVENTS-001-S07. Criterion: AC-EVENTS-07. Feature source: [calendar-display.feature](../features/calendar-display.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Through + create Picnic, all-day 2027-02-06, description Outside; Dentist, timed 2027-02-06 09:30 UTC, description Checkup. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open the row calendar view and February 6; inspect both events and neighbouring dates.
2. Open Picnic details and read its date/mode; return to the calendar.
3. Open Dentist details and read its interval.

## Expected observations

- Picnic and Dentist are represented on 2027-02-06.
- neither event is represented on 2027-02-05 or 2027-02-07.
- the details show one all-day date 2027-02-06 without a requested hour.
- the computed interval is 2027-02-06 09:30 to 2027-02-06 10:30 in UTC.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S08 — Keep simultaneous events independently accessible

## Purpose

Keep simultaneous appointments distinct and reachable.

Gherkin feature: EVENTS-001-F02. Scenario: EVENTS-001-S08. Criterion: AC-EVENTS-08. Feature source: [calendar-display.feature](../features/calendar-display.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Through + create Alpha/First and Beta/Second at 2027-02-06 09:30 UTC. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open the row view and inspect the 09:30 representation.
2. If grouped, open the visible group/list; open Alpha and verify First, then return and open Beta and verify Second.
3. Record that neither appointment was rejected, merged or hidden without a public route.

## Expected observations

- both events can be opened from the representation at 09:30.
- Alpha details show description First.
- Beta details show description Second.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S09 — Account for every event in a populated month cell

## Purpose

Verify title/overflow accounting without inventing a cell capacity.

Gherkin feature: EVENTS-001-F02. Scenario: EVENTS-001-S09. Criterion: AC-EVENTS-09. Feature source: [calendar-display.feature](../features/calendar-display.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Through + create exactly 18 one-time all-day events E01, E02, ..., E18 on 2027-02-06; no other data. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open Month February 2027. Count distinct visible E-title labels C in the February 6 cell, not glyph lines.
2. Require at least one short title. If C is less than 18, require a visible +N where N = 18 - C; if all fit, no overflow is required.
3. Tap overflow when present; otherwise tap the date cell. In Day, open/inspect every distinct E01-E18 once; use any public grouping/scrolling, not database inspection.

## Expected observations

- the date cell shows at least one of those event titles.
- its visible titles and any +N overflow account for exactly 18 distinct events.
- an overflow control is shown whenever any of those titles are hidden.
- Day displays 2027-02-06.
- every event E01 through E18 can be opened exactly once from that day.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S39 — Timed events require an explicitly chosen hour

## Purpose

Require explicit timed-hour confirmation rather than an implicit current hour.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S39. Criterion: AC-EVENTS-39. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; + draft Dentist on 2027-02-06, no confirmed hour. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Choose timed mode but do not confirm/select any hour; cancel the time chooser if one opens.
2. Verify that no implicit hour has been adopted as a saved timed value and a timed save cannot complete without explicit hour confirmation (the UI may return to all-day instead).
3. Abandon the complete draft and inspect that no event was created.

## Expected observations

- no implicit timed hour is adopted.
- a timed event cannot be saved until an hour is chosen.
- no event was created on 2027-02-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S01 — Open the form with contextual defaults

## Purpose

Inspect contextual defaults.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S01. Criterion: AC-EVENTS-01. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; phone date 2027-01-20 UTC. Run every S01 row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Use calendar navigation to establish the row view and view_date (Month means that month; Week means its Monday-Sunday range; Day means that date).
2. Tap + and inspect date, title, description, mode, repetition and available controls without entering values.

## Expected observations

- the form date is <default_date>.
- the title and description are empty.
- all-day and no repetition are selected.
- no duration, event end-time or event end-date adjustment is offered.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S02 — Save an all-day event and return to the originating view

## Purpose

Save and reopen an all-day event from every mode.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S02. Criterion: AC-EVENTS-02. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; establish each row view in January 2027. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open +, set title Picnic, description Bring papers, date 2027-02-06, all-day and no repetition; Save.
2. Immediately verify the returned view mode/date before further navigation.
3. Open Picnic from its date and inspect all saved details.

## Expected observations

- <view> displays the period containing 2027-02-06.
- Picnic is represented on 2027-02-06.
- the details show title Picnic and description Bring papers.
- the details show one all-day date 2027-02-06 without a requested hour.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S03 — Create a timed event offline with an automatic hour

## Purpose

Create a timed event offline and inspect its automatic hour.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S03. Criterion: AC-EVENTS-03. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

No accounts, no Play services, network off; Day 2027-02-06; UTC. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Tap +, enter Dentist, choose timed and explicitly select 09:30; keep description empty and no repetition; Save.
2. Inspect its calendar placement, open details and read start/end/duration.
3. Verify no login, network or shared calendar prerequisite was requested.

## Expected observations

- Day displays the period containing 2027-02-06.
- Dentist is represented at 09:30 on 2027-02-06.
- the details show an empty description.
- the computed interval is 2027-02-06 09:30 to 2027-02-06 10:30 in UTC.
- the computed duration is 60 elapsed minutes.
- no sign-in, network or shared Android calendar is required.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S04 — Reject missing mandatory event input

## Purpose

Reject blank titles rather than creating an untitled event.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S04. Criterion: AC-EVENTS-04. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; + draft date 2027-02-06, timed 09:30. Run both title_input rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Enter no title for empty, or exactly three ASCII spaces for three spaces.
2. Attempt Save (if disabled, inspect the indicated required title state); observe that the editor remains.
3. Cancel/Back out, accepting discard if offered, then inspect February 6 for any accidental event.

## Expected observations

- the editor remains open and identifies the title as required.
- no event was created on 2027-02-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S05 — Abandoning a draft restores the original calendar period

## Purpose

Abandon a draft without changing the original view or data.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S05. Criterion: AC-EVENTS-05. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Month January 2027, then + draft Draft on 2027-02-06 10:00 UTC, description Unsaved. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Use the row action: Cancel or Android system Back. Confirm discard if offered.
2. Inspect the returned Month period before navigating.
3. Navigate to February 6 and verify the draft was never saved.

## Expected observations

- Month displays the period containing 2027-01-20.
- no event named Draft exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S06 — Empty time slots do not open event creation

## Purpose

Prevent accidental event creation from an empty timed area.

Gherkin feature: EVENTS-001-F01. Scenario: EVENTS-001-S06. Criterion: AC-EVENTS-06. Feature source: [creation.feature](../features/creation.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

No events; open each row view for 2027-02-06. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Tap the empty 09:30 area, not a date header or +.
2. Inspect the mode, editor visibility and date for any new event.

## Expected observations

- <view> remains active with no event editor open.
- no event was created on 2027-02-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S28 — Delete a one-time event without deleting its neighbour

## Purpose

Delete one-time data, not a same-date neighbour.

Gherkin feature: EVENTS-001-F06. Scenario: EVENTS-001-S28. Criterion: AC-EVENTS-28. Feature source: [deletion.feature](../features/deletion.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Remove in row mode on 2027-02-06 (timed fixture 09:30 UTC); Keep all-day same date, description Unrelated. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open Remove, Delete and confirm.
2. Inspect Month, Week, Day for absence of Remove; open Keep and verify Unrelated.

## Expected observations

- Remove is absent from Month, Week and Day for 2027-02-06.
- Keep is still available with description Unrelated.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S29 — Delete the selected recurring scope

## Purpose

Delete exactly one/future/all recurring positions.

Gherkin feature: EVENTS-001-F06. Scenario: EVENTS-001-S29. Criterion: AC-EVENTS-29. Feature source: [deletion.feature](../features/deletion.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Course daily June 1-5, 09:00 UTC, Base, count 5; fresh per row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open June 3 details; Delete, select row scope and confirm.
2. Inspect every date June 1-10, count remaining_count starts, and check June 1/3/5 presences from the row.
3. Check June 6: removing a position must not add a replacement past the original count ending.

## Expected observations

- exactly <remaining_count> Course starts remain from 2027-06-01 through 2027-06-10.
- the 2027-06-03 occurrence is absent.
- the 2027-06-01 occurrence is <first_presence>.
- the 2027-06-05 occurrence is <last_presence>.
- no Course occurrence starts on 2027-06-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S30 — Cancel a deletion without changing the series

## Purpose

Cancel deletion at every scope.

Gherkin feature: EVENTS-001-F06. Scenario: EVENTS-001-S30. Criterion: AC-EVENTS-30. Feature source: [deletion.feature](../features/deletion.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Course daily June 1-5, 09:00 UTC, Base, count 5; fresh per row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. From June 3 request Delete with row scope, but cancel before final confirmation.
2. Inspect every original June 1-5 position at 09:00 and verify no June 6 start.

## Expected observations

- Course still starts at 09:00 on each date from 2027-06-01 through 2027-06-05.
- no occurrence starts on 2027-06-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S31 — Scope follows original series order after a move

## Purpose

Use nominal series membership, not moved display chronology.

Gherkin feature: EVENTS-001-F06. Scenario: EVENTS-001-S31. Criterion: AC-EVENTS-31. Feature source: [deletion.feature](../features/deletion.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Course daily June 1-5 at 09:00 UTC; edit only original June 5 to May 31 11:00 before running each row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open the still-nominal June 3 occurrence; Delete with row scope and confirm.
2. Inspect May 31-June 10. The moved original June 5 is in the future portion despite its earlier displayed date, so it must be removed.
3. Inspect June 1/2 and count remaining_count for the row.

## Expected observations

- exactly <remaining_count> Course starts remain from 2027-05-31 through 2027-06-10.
- no Course occurrence remains on 2027-05-31, 2027-06-03, 2027-06-04 or 2027-06-05.
- the 2027-06-01 and 2027-06-02 occurrences are <past_presence>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S20 — Edit a one-time event without creating a duplicate

## Purpose

Edit one-time data without retaining its old copy.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S20. Criterion: AC-EVENTS-20. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Dentist on 2027-02-06 09:30 UTC, Checkup. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open Dentist details, Edit; change title to Consultation, description Followup, date February 7 and hour 11:00; Save.
2. Inspect the new event/details and old February 6 placement; count one event across February 6-7, not an old plus a new copy.

## Expected observations

- Consultation is represented at 11:00 on 2027-02-07.
- its details show description Followup.
- the computed interval is 2027-02-07 11:00 to 2027-02-07 12:00 in UTC.
- no event named Dentist exists.
- exactly 1 event exists from 2027-02-06 through 2027-02-07.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S21 — Change between all-day and timed modes

## Purpose

Convert a one-time event between modes.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S21. Criterion: AC-EVENTS-21. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Switch on 2027-02-06 in original_mode; timed fixture starts 09:30 UTC. Run both rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open Switch for Edit, choose target_mode; explicitly choose 09:00 only when switching to timed; Save.
2. Inspect one saved event and its mode/date. Timed computes one hour; all-day has no requested hour. Check that no editable duration/end range appeared.

## Expected observations

- Switch details show <target_mode> on 2027-02-06.
- exactly 1 event named Switch exists.
- no duration, event end-time or event end-date adjustment is offered.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S22 — Move and customize only one recurring occurrence

## Purpose

Customize only the selected recurring position.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S22. Criterion: AC-EVENTS-22. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Course daily June 1-5, 2027, 09:00 UTC, Base, count 5. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open June 3, Edit this occurrence; change to June 4 11:00, title Custom, description Local; Save.
2. Inspect original June 3, both June 4 times and all other original positions. There must not be an extra tail on June 6.

## Expected observations

- no Course occurrence starts on 2027-06-03.
- Custom starts at 11:00 on 2027-06-04 with description Local.
- Course starts at 09:00 on 2027-06-01, 2027-06-02, 2027-06-04 and 2027-06-05.
- no occurrence of the original series starts on 2027-06-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S23 — Series text edits preserve individual fields and cancellations

## Purpose

Preserve field overrides and cancellations on scoped text changes.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S23. Criterion: AC-EVENTS-23. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Prepare the Course exception fixture defined above. Run each scope on a fresh fixture. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open the June 3 occurrence and Edit the row scope. Change only common title to Renamed and description to Shared; Save.
2. Inspect all nominal June 1-5 positions against the row and fixed expected observations, including the preserved custom fields and 11:00 move.
3. Inspect June 6 for no silent extension. Do not find the original series by its old title alone after renaming.

## Expected observations

- the 2027-06-01 occurrence has title <first_title> and description <first_description> at 09:00.
- the 2027-06-02 occurrence keeps title Custom and description Local at 09:00.
- the 2027-06-03 occurrence inherits title Renamed and description Shared but stays at 11:00.
- the 2027-06-04 occurrence remains deleted.
- the 2027-06-05 occurrence has title Renamed and description Shared at 09:00.
- no occurrence of the original series starts on 2027-06-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S24 — Confirm before resetting temporal exceptions

## Purpose

Warn before resetting temporal overrides/cancellations.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S24. Criterion: AC-EVENTS-24. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Prepare the Course exception fixture defined above, fresh for each row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. From June 3 choose Edit with row scope; set common start time 10:00 and press Save.
2. Read the warning; require reset information and possible restoration of individually deleted occurrences before confirming.
3. Confirm, then inspect June 1-5 and June 6. Outside-scope data must remain; June 3 resets from 11:00 and June 4 is restored if generated.

## Expected observations

- a warning identifies that affected individual modifications will reset and deleted occurrences may return.
- the 2027-06-01 occurrence starts at <first_time>.
- the 2027-06-02 occurrence has title <second_title> at <second_time>.
- Course starts at 10:00 on 2027-06-03, 2027-06-04 and 2027-06-05.
- no affected occurrence remains at 11:00.
- no occurrence of the original series starts on 2027-06-06.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S25 — Cancelling a reset warning changes no saved event

## Purpose

Reject a reset without any saved change.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S25. Criterion: AC-EVENTS-25. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Fresh Course exception fixture for each scope. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. From June 3 choose Edit with scope, set 10:00 and press Save.
2. Cancel the reset warning, then abandon the editor/discard its draft.
3. Compare every fixture field/date/time/cancellation to the baseline matrix above.

## Expected observations

- the Course exception fixture remains unchanged.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S26 — Recurrence edits keep the remaining occurrence budget unless changed

## Purpose

Keep the original remaining count budget on interval changes.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S26. Criterion: AC-EVENTS-26. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Course daily June 1-5, 09:00 UTC, Base, count 5, no exceptions. Fresh for each row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. From June 3 edit row scope; change only interval to 2 days. Do not change the ending; Save.
2. Visit June 1-10 and compare the complete start-date set with expected_dates; inspect absent_date.
3. For future scope, June 1/2 remain historical and only the three remaining scheduled positions are regenerated.

## Expected observations

- Course starts at 09:00 on exactly <expected_dates>.
- no Course occurrence starts on <absent_date>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S27 — A new future count starts with the first affected occurrence

## Purpose

Count an explicitly changed future ending from its first position.

Gherkin feature: EVENTS-001-F05. Scenario: EVENTS-001-S27. Criterion: AC-EVENTS-27. Feature source: [editing.feature](../features/editing.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Course daily June 1-5, 09:00 UTC, Base, count 5. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. From June 3 choose Edit this and future; set the ending to after 2 occurrences, keep daily/time, Save.
2. Inspect June 1-5 for starts on 1/2/3/4 only; the new future count starts at June 3, not at June 1.

## Expected observations

- Course starts at 09:00 on exactly 2027-06-01, 2027-06-02, 2027-06-03 and 2027-06-04.
- no Course occurrence starts on 2027-06-05.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S10 — Count the first occurrence in a day or week series

## Purpose

Verify day/week spacing and first-inclusive count.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S10. Criterion: AC-EVENTS-10. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; run all S10 Examples rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Through + create timed Course 09:00 UTC on start_date, custom repeat every interval unit, end after 3; Save.
2. Visit the three expected dates and all intermediate dates; count exactly three starts in that span.
3. Inspect next_date for no extra start and each saved computed interval for 60 elapsed minutes.

## Expected observations

- Course starts at 09:00 on exactly <start_date>, <second_date> and <third_date>.
- Course has no occurrence on <next_date>.
- each timed occurrence lasts 60 elapsed minutes.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S11 — Include the recurrence end date

## Purpose

Use an inclusive recurrence ending.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S11. Criterion: AC-EVENTS-11. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; run all S11 Examples rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Create Course 09:00 at start_date, repeat every interval unit, end on end_date; Save.
2. Visit the full start_date-to-end_date span, count three and inspect the last start at 09:00.
3. Visit next_date and inspect saved ending details; no later slot should be generated beyond the saved bound.

## Expected observations

- exactly 3 Course occurrences start from <start_date> through <end_date>.
- Course starts at 09:00 on <end_date>.
- Course has no occurrence on <next_date>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S12 — A never-ending series remains available in a later window

## Purpose

Verify Never using a finite later observation window.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S12. Criterion: AC-EVENTS-12. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

No events; create Course 09:00 UTC from 2027-01-01, every day, end Never. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Using Month navigation, visit January 2030 (36 forward month transitions from January 2027), then inspect January 1-3.
2. Open each of the three starts and recurrence details to verify 09:00 and Never. Do not attempt to enumerate an infinite series.

## Expected observations

- Course starts at 09:00 on each of those 3 dates.
- its recurrence details still show no end.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S13 — Reject a nonpositive or noninteger recurrence interval

## Purpose

Reject invalid recurrence intervals through the public control.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S13. Criterion: AC-EVENTS-13. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Open + for Course on 2027-02-06; daily recurrence, all-day sufficient. Execute each interval_input row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Attempt the exact row input; empty means clearing a text field if allowed, three other rows are literal 0, -1, 1.5.
2. Verify the value is not accepted. If a selector prevents it, inspect its valid options/disabled boundary instead; never ignore the requested value or replace it with 1 and call that a test.
3. Abandon the form and inspect that Course was not created.

## Expected observations

- recurrence interval input <interval_input> is not accepted.
- no event named Course exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S14 — Reject a nonpositive or noninteger occurrence count

## Purpose

Reject invalid occurrence counts.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S14. Criterion: AC-EVENTS-14. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Open + for Course on 2027-02-06; daily repeat, ending after N. Execute all count_input rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Attempt the exact row count; empty means clear if the public control allows it.
2. Verify rejection through validation or a public selector which cannot accept the requested invalid count; record that evidence.
3. Abandon and inspect that Course was not created.

## Expected observations

- occurrence count input <count_input> is not accepted.
- no event named Course exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S15 — Reject an end date before the first occurrence

## Purpose

Reject an ending before the first date.

Gherkin feature: EVENTS-001-F03. Scenario: EVENTS-001-S15. Criterion: AC-EVENTS-15. Feature source: [recurrence-days-weeks.feature](../features/recurrence-days-weeks.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Open + for Course from 2027-02-10. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Select daily recurrence, inclusive end 2027-02-09, then try Save.
2. Inspect the identified invalid ending and retained editor; abandon and verify no Course.

## Expected observations

- the end date is identified as invalid and the editor remains open.
- no event named Course exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S16 — Repeat a freely chosen numbered day without date drift

## Purpose

Verify free numbered-month targets, clamping and first-match anchoring.

Gherkin feature: EVENTS-001-F04. Scenario: EVENTS-001-S16. Criterion: AC-EVENTS-16. Feature source: [recurrence-months.feature](../features/recurrence-months.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; open Month containing the row lower_bound; execute all S16 rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open +, choose Course, all-day, starting lower_bound; custom repeat every interval calendar months, freely choose month_day, end after 3; Save.
2. Immediately check Month now contains first_date; then inspect all three expected dates and intervening dates from lower_bound through third_date, requiring exactly that three-date set.
3. Open saved recurrence details/editor without saving and verify the requested numbered target did not drift after a clamped occurrence.

## Expected observations

- the calendar returns to the originating view on the first occurrence date <first_date>.
- Course is all-day on exactly <first_date>, <second_date> and <third_date>.
- its monthly rule still requests day <month_day>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S17 — Repeat a freely chosen ordinal weekday

## Purpose

Verify ordinal/last weekdays and absent-fifth fallback.

Gherkin feature: EVENTS-001-F04. Scenario: EVENTS-001-S17. Criterion: AC-EVENTS-17. Feature source: [recurrence-months.feature](../features/recurrence-months.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; open Month containing lower_bound; run all S17 rows. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Create all-day Course through + with lower_bound, interval months, the row ordinal and weekday, count 3.
2. Inspect exactly the expected three dates in the lower_bound-to-third_date span, including absence of an extra start on a nonmatching lower_bound.
3. Inspect the saved rule; fifth must not silently become fourth merely because one target month had only four.

## Expected observations

- Course is all-day on exactly <first_date>, <second_date> and <third_date>.
- its monthly rule still requests the <ordinal> <weekday>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S18 — Reject a monthly window with no matching occurrence

## Purpose

Reject a free monthly rule with no eligible date before its ending.

Gherkin feature: EVENTS-001-F04. Scenario: EVENTS-001-S18. Criterion: AC-EVENTS-18. Feature source: [recurrence-months.feature](../features/recurrence-months.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Open + for Course from 2027-01-20. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Configure every 2 months on first Monday, end on 2027-01-31; try Save.
2. Observe the no-occurrence/ending error, abandon the editor and inspect that no Course exists.

## Expected observations

- the form identifies that no occurrence fits before the end date.
- no event named Course exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S19 — Include a clamped monthly occurrence on the end date

## Purpose

Include a clamped target that matches the ending.

Gherkin feature: EVENTS-001-F04. Scenario: EVENTS-001-S19. Criterion: AC-EVENTS-19. Feature source: [recurrence-months.feature](../features/recurrence-months.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Empty calendar; UTC. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Create all-day Course from 2027-01-30, every month on day 31, ending 2027-04-30.
2. Inspect January 31, February 28, March 31, April 30 and all intermediate dates for exactly four instances.
3. Inspect May 31 for no start and the saved original day-31 rule.

## Expected observations

- Course is all-day on exactly 2027-01-31, 2027-02-28, 2027-03-31 and 2027-04-30.
- Course has no occurrence on 2027-05-31.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S32 — Convert display without changing the editing zone

## Purpose

Convert viewing time while retaining labelled origin editing time.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S32. Criterion: AC-EVENTS-32. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

While phone zone Europe/Paris, create Dentist 2027-01-15 09:00, Checkup. Fresh per zone row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Using public device Settings, choose display_zone and return to Calendar; navigate to display_date.
2. Inspect Dentist at display_time, then open Edit without changing fields.
3. Read origin date/hour/zone, cancel and inspect that display_date/display_time remain unchanged.

## Expected observations

- Dentist is represented at <display_time> on <display_date>.
- the editing date and hour are 2027-01-15 and 09:00.
- the editing zone is identified as Europe/Paris.
- Dentist is represented at <display_time> on <display_date>.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S33 — Keep the origin clock time when DST offsets change

## Purpose

Preserve origin-local clock recurrence across different DST transitions.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S33. Criterion: AC-EVENTS-33. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

In Europe/Paris create Course Sundays 09:00 from 2027-03-21, weekly count 3. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Set phone display zone America/New_York through Settings, return and visit March 21, March 28 and April 4.
2. Inspect local start times 04:00, 03:00, 03:00 and computed one-hour durations.
3. Open each for editing without saving; inspect 09:00 Europe/Paris, then Cancel.

## Expected observations

- Course is represented at 04:00 on 2027-03-21.
- Course is represented at 03:00 on 2027-03-28 and 2027-04-04.
- each occurrence still requests 09:00 in Europe/Paris.
- each computed duration is 60 elapsed minutes.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S34 — All-day dates do not shift with the display zone

## Purpose

Keep all-day literal dates in extreme zones.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S34. Criterion: AC-EVENTS-34. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Picnic all-day 2027-01-15, Outside, while phone zone Europe/Paris. Fresh per row. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Change phone zone to display_zone through Settings and return to Calendar.
2. Inspect January 14-16; Picnic stays on January 15 as all-day.

## Expected observations

- Picnic is all-day on 2027-01-15.
- Picnic is not represented on 2027-01-14 or 2027-01-16.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S35 — Adding an hour to an all-day event captures the current phone zone

## Purpose

Capture the current zone for newly added timed information.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S35. Criterion: AC-EVENTS-35. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Create Picnic all-day January 15, 2027 in Europe/Paris, Outside; then change phone zone to Asia/Tokyo. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Open Edit in Tokyo, switch Picnic to timed and explicitly choose 09:00, same date; Save.
2. Reopen Edit, verify Asia/Tokyo zone label, then cancel.
3. Change phone zone to UTC, inspect January 15 00:00 and exactly one Picnic.

## Expected observations

- the editing zone is identified as Asia/Tokyo.
- Picnic is represented at 00:00 on 2027-01-15.
- exactly 1 event named Picnic exists.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S36 — Shift a nonexistent hour without drifting the recurrence clock

## Purpose

Resolve a spring gap visibly without shifting the future rule clock.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S36. Criterion: AC-EVENTS-36. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Europe/Paris; open + Course from 2027-03-28. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Select timed 02:30, repeat daily, end after 2. Before Save inspect the 03:30 first-start adjustment and retained requested rule clock 02:30.
2. Save and inspect origin-zone intervals March 28 03:30-04:30 and March 29 02:30-03:30.
3. Change phone zone to UTC; inspect starts 01:30 and 00:30 respectively and exactly two starts through March 30.

## Expected observations

- the editor shows that the first start is adjusted to 03:30 in Europe/Paris.
- the requested recurrence clock remains 02:30.
- the computed interval is 2027-03-28 03:30 to 2027-03-28 04:30 in Europe/Paris.
- the next occurrence is 2027-03-29 02:30 to 2027-03-29 03:30 in Europe/Paris.
- Course starts at 01:30 on 2027-03-28 and at 00:30 on 2027-03-29.
- exactly 2 Course starts exist from 2027-03-28 through 2027-03-30.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S37 — Choose the first repeated local hour only once

## Purpose

Use the earlier repeated start once, with a real elapsed hour.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S37. Criterion: AC-EVENTS-37. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Europe/Paris, no events; use October 25, 2026 (past date is intentional). Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Create Night through +, timed 02:30 on October 25, no description.
2. Inspect the original date for one start, then change phone zone to UTC.
3. Open details and verify 00:30-01:30 UTC, 60 minutes, with no second start at 01:30.

## Expected observations

- exactly 1 Night start exists on 2026-10-25.
- the computed interval is 2026-10-25 00:30 to 2026-10-25 01:30 in UTC.
- the computed duration is 60 elapsed minutes.
- no second Night start exists at 01:30 on 2026-10-25 in UTC.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S38 — Automatic midnight overlap is one occurrence

## Purpose

Render an overnight tail without creating two events.

Gherkin feature: EVENTS-001-F07. Scenario: EVENTS-001-S38. Criterion: AC-EVENTS-38. Feature source: [time-zones-and-boundaries.feature](../features/time-zones-and-boundaries.feature).

## Preconditions

Apply the shared environment/public-interface rules and the feature's starting zone. Candidate APK installed; no previous case's data. For an outline, execute every row of its Examples table independently.

## Initial state

Run every S38 row independently. Set phone zone UTC; establish row view on January 15, 2027. Setup is through the public GUI vocabulary above, never direct data injection.

## Actions

1. Create Night through + on January 15 at start_time; inspect read-only computed interval against end_date/end_time.
2. Inspect January 15 and January 16 in the row view. Require next_day_presence; an exact-midnight end must not make a next-day representation.
3. Open every visible fragment: its start remains January 15 start_time.
4. Delete Night once from the starting date, confirm, then inspect both dates for absence.

## Expected observations

- the computed interval is 2027-01-15 <start_time> to <end_date> <end_time> in UTC.
- Night is represented on 2027-01-15.
- its representation on 2027-01-16 is <next_day_presence>.
- opening any visible fragment identifies the same start 2027-01-15 <start_time>.
- Night is absent from both 2027-01-15 and 2027-01-16.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

Any missing/contradictory observation, wrong value/date/count/scope, unintended saved copy, clipping of required data or crash is FAIL. Use the shared rules to separate setup ENVIRONMENT_ERROR and unattempted NOT_RUN; PASS requires every observation.

## Cleanup

Delete only this case's synthetic events/series through the GUI, including renamed/moved instances and restored cancellations. Confirm the next case's empty calendar, return to Month/Today, preserve evidence, and restore original phone zone/date/language/hour settings at session end. No personal-device reset.

# QA-EVENTS-001-S40 — Other structural fields also require reset consent

## Purpose

Verify date, mode and recurrence changes use the same reset-consent protection as a time change. Feature: EVENTS-001-F05. Scenario: EVENTS-001-S40. Criterion: AC-EVENTS-40. Source: [editing.feature](../features/editing.feature).

## Preconditions

Use the shared dedicated public-GUI environment and UTC. Run each Examples row independently on a fresh Course exception fixture.

## Initial state

Prepare and verify the exact Course exception baseline above through +, one-occurrence editing and deletion.

## Actions

1. Open the June 3 occurrence for Edit with scope whole series.
2. Change only the row field to value: starting date 2027-06-02, mode all-day, or recurrence interval 2 days.
3. Attempt Save; read the reset/restoration warning before any confirmation.
4. Cancel the warning, abandon the editor and compare every baseline occurrence/date/time/title/description/cancellation.

## Expected observations

- a warning identifies that affected individual modifications will reset and deleted occurrences may return.
- the Course exception fixture remains unchanged.

Substitute row values exactly. Check the complete stated date spans, including explicit absent/next dates, through the public GUI.

## Failure conditions

A missing reset warning, silent reset, restored deletion or any saved baseline change after cancellation is FAIL. External setup failure is ENVIRONMENT_ERROR; unattempted is NOT_RUN; PASS requires every row's observations.

## Cleanup

Delete only known fixture data using the public GUI, restore original Settings at session end, preserve evidence and clear no personal data.

