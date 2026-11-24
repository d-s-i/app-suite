# CAL-001 — End-to-end QA procedure

## Environment and execution rules

- Target: owner-reported Pixel 9 Pro, Android 17 / GrapheneOS; install the future target APK through normal Android installation. No application APK exists yet, so product QA is NOT_RUN at specification time.
- Use only the launcher, app GUI, Android Settings, screenshots, and public device clock. No database writes, test-only APIs, private functions, or synthetic event injection.
- Use a dedicated test profile/device with no personal data. Fresh-install/reset actions require operator approval. Restore original date/time, automatic time/zone settings, locale, hour format, network, theme, display/font scale after the session.
- Default fixture: no events, 2028-02-15 in UTC, English United States, 24-hour time, portrait, light appearance, default display/font scale. Set these through Android Settings before launch and verify the displayed device date. A running clock need not be frozen; only the current-time indicator case compares the observed current minute.
- Device clock changes must not affect the operator's real appointments. If clock control or the requested OS/device is unavailable, record ENVIRONMENT_ERROR rather than substitute an unapproved fixture.
- Dates in these instructions use ISO notation to identify cells unambiguously; do not require ISO strings to appear in the UI. Use the visible localized month/day/year and the known Monday-first positions.
- To reach a fixture date: choose Month in the navigation drawer, swipe the month grid until its month/year matches, then tap the identified date. For Week, subsequently choose Week. View selection is the public drawer interaction specified here, not an internal mode flag.
- For each Scenario Outline, run every row independently and record input row values with the stable scenario/QA ID. No feature may be accepted based on only one row.
- Visual QA needs same-scale empty-calendar reference captures of the pinned Proton 2.34.2 APK for typography comparison. Obtain captures through its public GUI in an operator-approved reference session, with Monday start, same locale/clock/theme/scaling. Do not create/use a Proton account or upload data without permission. The target app must not require such an account. Reference captures are not currently available.
- For density-based geometry, compare screenshots at the documented device density and default scale. Convert dp to physical pixels with device density and allow only unavoidable rounding to the nearest physical pixel, not arbitrary visual tolerances. Nominal sp sizes require the reference typography comparison, not measuring glyph bounding-box height as if it were sp.

## Classification and evidence

Each case/Examples row gets exactly one result:
- PASS: all stated observations were verified through the public GUI.
- FAIL: the environment/preconditions are valid, but product behavior contradicts any expected observation (including a crash).
- ENVIRONMENT_ERROR: setup cannot be established, the requested platform/clock/density/reference capture is unavailable, or external device operation prevents observation. Missing prerequisites do not count as product passes.
- NOT_RUN: the case has not been attempted.

Record feature/scenario/QA IDs, APK build identity, device/OS, fixture settings, row inputs, actions, observed results, screenshots, and any setup failure. Do not turn visual subjective resemblance into a pass. Structural checks do not certify pixel-perfect parity.

# QA-CAL-001-S01 — First launch opens the current month offline

## Purpose

Verify the approved first-launch defaults and use without network/Play services.

Gherkin feature: CAL-001-F01. Gherkin scenario: CAL-001-S01. Acceptance criterion: AC-CAL-01.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

A fresh installation with no prior launch or events; device clock 2028-02-15 12:00 UTC, locale English United States. Network is off and Google Play services are absent.

## Actions

1. Through Android Settings, verify the test date, UTC zone, locale, disabled network, and absence of Google Play services.
2. Launch the target APK from its launcher icon for the first time.
3. Inspect the toolbar, weekday headings, every date cell, and any gating screen.

## Expected observations

- Month is active; toolbar reads February; weekday columns are Monday through Sunday.
- There are exactly 7 equal-width columns and 6 equal-height rows containing the 42 consecutive dates 2028-01-31 through 2028-03-12.
- No event blocks, sign-in gate, or network requirement prevents using the calendar.

## Failure conditions

Any wrong initial mode/date/range, missing or duplicate cell, event block, crash, or account/network gate. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S02 — Month styling follows the pinned reference

## Purpose

Verify the concrete reference-derived visual measurements, not a subjective resemblance.

Gherkin feature: CAL-001-F01. Gherkin scenario: CAL-001-S02. Acceptance criterion: AC-CAL-02.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

Month displays February 2028; device date 2028-02-15, English United States, UTC; default display/font scales and light appearance. Approved reference captures for the same display configuration are available for typography comparison.

## Actions

1. Open Month using its drawer entry; swipe to February 2028 if necessary.
2. Capture the calendar region through the device screenshot interface; record screen density, font scale, and display scale from the test environment.
3. Measure grid/header/outline geometry in the screenshot using density conversion; compare rendered date-number typography against the same-scale pinned reference capture.
4. Inspect the date numbers belonging to January/March and today versus the other February dates.

## Expected observations

- Weekday header height is 24dp; separators are 1dp; date-number baselines are 18dp below row tops.
- Day numbers are horizontally centered with the reference 12sp typography. Pixel glyph height alone is not a valid measurement of the nominal font size.
- Adjacent-month date numbers are muted relative to current-month numbers.
- Only February 15 has the accent-colored rounded outline: 21dp wide, 20dp high, radius 5dp, stroke 1dp.

## Failure conditions

A measurable mismatch to the stated geometry/reference typography, unmuted adjacent-month numbers, or today outline on an incorrect date. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S03 — Navigate month boundaries

## Purpose

Verify leap-year, non-leap-year, and year-boundary month paging.

Gherkin feature: CAL-001-F01. Gherkin scenario: CAL-001-S03. Acceptance criterion: AC-CAL-03.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; date 2028-02-15, UTC, English United States. Run every Examples row of CAL-001-S03 independently.

## Actions

1. Choose Month in the drawer. Reach starting_month by swiping the month grid until its title matches, checking the year as needed.
2. Swipe the month grid exactly once in the row direction, completing a single page transition.
3. Read the resulting month and every date cell; compare with expected_month, first_date, last_date, and boundary_date from that row.

## Expected observations

- The displayed month is expected_month; exactly 42 consecutive dates run from first_date through last_date.
- boundary_date appears exactly once; leap February includes February 29 and non-leap February does not invent February 29.

## Failure conditions

Wrong month, wrong boundary date, skipped/duplicate date, invalid February date, or a missing range endpoint. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S04 — Select an empty month date

## Purpose

Verify that month cells, including spillover dates, open the intended Day.

Gherkin feature: CAL-001-F01. Gherkin scenario: CAL-001-S04. Acceptance criterion: AC-CAL-04.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; February 2028 Month visible. Repeat independently for 2028-02-20, 2028-01-31, and 2028-03-01.

## Actions

1. Open Month and navigate to February 2028.
2. Tap the cell corresponding to selected_date from CAL-001-S04.
3. Inspect the active view and its date column. Return to February Month before the next row.

## Expected observations

- Day is active and has exactly one date column for selected_date.
- The time grid contains no event blocks.

## Failure conditions

A different date/mode opens, an event form opens instead, or event blocks appear. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S05 — Format month titles by device locale

## Purpose

Verify localized titles with a fixed Monday week start.

Gherkin feature: CAL-001-F01. Gherkin scenario: CAL-001-S05. Acceptance criterion: AC-CAL-05.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; date 2028-02-15 UTC. Execute all four Examples rows of CAL-001-S05.

## Actions

1. Set the row locale through Android language/region settings and reopen the app if required for the locale change.
2. Choose Month in the drawer and navigate to month_date.
3. Read the toolbar title and weekday order.

## Expected observations

- Titles are February / février for February 2028 and December 2027 / décembre 2027 for December 2027 in the corresponding English United States / French France rows.
- The current year is omitted, another year is included, and the first weekday stays Monday.

## Failure conditions

Wrong language/case/title/year or week-start changed to Sunday by the locale. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S06 — Switch from Month to an empty timed view

## Purpose

Verify the drawer selector and basic Week/Day geometry.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S06. Acceptance criterion: AC-CAL-06.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; clock 2028-02-15 12:00 UTC, English United States, 24-hour time, default scales. Start each Examples row of CAL-001-S06 in February Month with today selected.

## Actions

1. Choose Month then activate Today to establish the current month/date.
2. Open the navigation drawer and choose the row view, Week or Day.
3. Inspect the drawer state, date headers, hour-label column, and grid. Measure adjacent hour separators at default zoom.

## Expected observations

- The drawer closes and the chosen view becomes active.
- Week has 7 date columns for February 14–20; Day has 1 column for February 15.
- Time labels are on the left with horizontal hour separators, 60dp apart at default zoom.
- No event blocks appear.

## Failure conditions

Wrong mode/range/column count, open obstructing drawer, agenda list instead of grid, missing labels, wrong default hour spacing, or event blocks. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S07 — Scroll the time grid without changing dates

## Purpose

Verify access to early and late hours without accidental horizontal navigation.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S07. Acceptance criterion: AC-CAL-07.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; date 2028-02-15 UTC, English United States, 24-hour time. Run Week February 14–20 and Day February 15 as in CAL-001-S07.

## Actions

1. Open the row view/date range using Today and the view selector.
2. Scroll vertically to the beginning of the day; observe 01:00, the first labeled hour in the pinned reference.
3. Scroll vertically to the end of the day; observe 23:00.
4. Read the date headers again.

## Expected observations

- 01:00 and 23:00 can each be brought into view.
- The mode and first_date/last_date date range remain unchanged throughout the vertical scrolling.

## Failure conditions

An endpoint cannot be reached, dates change during vertical scrolling, or the grid disappears. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S08 — Open a day from a week header

## Purpose

Verify the reference week-date-header interaction.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S08. Acceptance criterion: AC-CAL-08.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; Week displays 2028-02-14 through 2028-02-20.

## Actions

1. Open Week for February 14–20 using Today.
2. Tap the date header for February 18, not an empty hour cell.
3. Read the resulting mode/date header.

## Expected observations

- Day is active with exactly one date column for 2028-02-18.

## Failure conditions

Wrong date/mode or an event editor opens instead of Day. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S09 — Retired

Retired by the owner's arrow/dropdown removal request; see ../ui-revision.md. Do not execute the former picker procedure or reuse this ID.

# QA-CAL-001-S10 — Follow the device time format

## Purpose

Verify exact public hour labels in 24h and 12h settings.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S10. Acceptance criterion: AC-CAL-10.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; Day February 15, 2028; English United States, UTC. Run both Examples rows of CAL-001-S10.

## Actions

1. Use Android Settings to select the row 24-hour or 12-hour time format.
2. Reopen the app if needed, choose Day and activate Today.
3. Scroll to the hour thirteen hours after midnight and read its label.

## Expected observations

- The label is exactly 13:00 for 24-hour time and 1 PM for 12-hour time.

## Failure conditions

The label does not follow the device format or represents the wrong hour. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S11 — Navigate timed periods across date boundaries

## Purpose

Verify full-period Week/Day paging across leap day and year boundaries offline.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S11. Acceptance criterion: AC-CAL-11.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; clock 2028-02-15 UTC, English United States; network off, no Google Play services. Execute all four Examples rows of CAL-001-S11.

## Actions

1. Open Month and navigate to initial_first from the Examples row, then tap that date. For Week rows choose Week through the drawer; for Day rows remain in Day. Confirm initial_first through initial_last.
2. Swipe horizontally across the time grid by one complete period in the row direction.
3. Read all resulting date headers.

## Expected observations

- The original mode remains active and the resulting range is expected_first through expected_last from the row.
- Week moves by seven days and Day by one day. Dates stay consecutive with correct boundary transitions and no event blocks.

## Failure conditions

Wrong range, invalid leap day/year transition, changed mode, unexpected event blocks, or navigation blocked by offline operation. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S12 — Return to today

## Purpose

Verify Today returns each mode to the current date without switching modes.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S12. Acceptance criterion: AC-CAL-12.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; device date 2028-02-15 UTC; offline/no Play services. Repeat for Month, Week, Day per CAL-001-S12, starting each in December 2027.

## Actions

1. Use Month paging to December 2027. For timed modes tap December 15 then choose the row view in the drawer.
2. Activate the Today calendar icon in the toolbar.
3. Read the mode, toolbar Today number, and complete displayed date range.

## Expected observations

- Today displays 15 and keeps the selected mode.
- Month shows the February grid from January 31 through March 12; Week shows February 14–20; Day shows February 15.

## Failure conditions

Wrong Today number/date range, changed view mode, ineffective control, or network/account gate. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S13 — Retired

Retired by the owner's arrow/dropdown removal request; see ../ui-revision.md. Do not execute the former picker procedure or reuse this ID.

# QA-CAL-001-S14 — Return from Day to Month

## Purpose

Verify mode switching keeps the containing month.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S14. Acceptance criterion: AC-CAL-14.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; Day displays 2028-03-01; date clock remains 2028-02-15 UTC, English United States; offline/no Play services.

## Actions

1. Reach March 1 by opening March in Month and tapping its date.
2. Open the navigation drawer and choose Month.
3. Read the active view, title, drawer visibility, and date grid.

## Expected observations

- Month is active, the drawer closes, and the title is March.
- There are 42 consecutive dates from 2028-02-28 through 2028-04-09.

## Failure conditions

Wrong month/title, incorrect grid, open drawer, or mode fails to change. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.

# QA-CAL-001-S15 — Show the current time on today

## Purpose

Verify the source reference current-time line/dot through visible time/date information.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S15. Acceptance criterion: AC-CAL-15.

## Preconditions

The target APK is installed, the shared environment rules above can be established, and the app is operable through its GUI. For outlines, consult the corresponding feature file and execute every Examples row.

## Initial state

No events; date 2028-02-15 UTC with a recorded current hour/minute. Run Week containing February 15 and Day February 15; offline/no Play services.

## Actions

1. Record the device local date/hour/minute using Android public clock UI; do not depend on a private test clock.
2. Open the row view and activate Today, then scroll to the current hour.
3. Capture the current-time line/dot and date/time headers, recording the device time at observation. If observation crosses a minute boundary, repeat against a single recorded minute.

## Expected observations

- The line and dot appear in February 15 at the position representing the recorded current local hour/minute on the visible scale.
- The indicator is not attached to another date or an unrelated hour.

## Failure conditions

Missing line/dot, wrong date, or visibly incorrect current-time position. Classify setup/reference unavailability as ENVIRONMENT_ERROR, not a product failure.

## Cleanup

Return to Month and activate Today before the next independent case; reset the required fixture through public settings/UI. No events should have been created. For the fresh-install case, prepare a separate approved fresh state before rerunning. Remove temporary QA screenshots only after preserving required evidence, and restore all original device settings at session end.


# QA-CAL-001-S16 — System Back from Day opens the containing month

## Purpose

Verify Back returns to the month of the displayed day instead of leaving the app.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S16. Acceptance criterion: AC-CAL-16.

## Preconditions

Use the shared environment rules above: installed target APK, dedicated test environment, UTC, device date 2028-02-15, English United States. Only app GUI and Android public navigation/Settings are allowed.

## Initial state

Execute both CAL-001-S16 Examples rows independently: Day 2028-02-20 and Day 2028-03-01. The drawer is closed.

## Actions

1. Choose Month in the navigation drawer; navigate to the selected_date month and tap that date. Verify Day and its date header.
2. Press Android system Back using its navigation button or back gesture.
3. Read the active view, month title, and every date cell.

## Expected observations

- Month remains inside the Calendar app, with month_title from the row.
- The 42 dates run consecutively from first_date through last_date from that row.

## Failure conditions

App exits, Day remains active, wrong month opens, or its date range differs from the row. Classify product contradictions as FAIL, unavailable setup as ENVIRONMENT_ERROR, all observations verified as PASS, and an unattempted case as NOT_RUN.

## Cleanup

Return to Month and Today. Preserve evidence, create no events, and restore original device settings at session end.

# QA-CAL-001-S17 — Timed views have no month expansion control

## Purpose

Verify the requested arrow/picker removal does not remove normal Week/Day content.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S17. Acceptance criterion: AC-CAL-17.

## Preconditions

Use the shared environment rules above: installed target APK, dedicated test environment, UTC, device date 2028-02-15, English United States. Only app GUI and Android public navigation/Settings are allowed.

## Initial state

Start in Month February 2028 with today selected. Repeat CAL-001-S17 for Week and Day.

## Actions

1. Open the navigation drawer and choose the row view.
2. Inspect the toolbar and area above the time grid.
3. Confirm the date headers and scrollable hour grid are still visible.

## Expected observations

- The chosen view is active; no month-expansion arrow/control or dropdown mini-calendar is displayed.
- The date headers and timed grid remain visible and usable; the Month drawer entry still exists.

## Failure conditions

Expansion control/picker remains, the app changes to an unintended mode, or time-grid content disappears. Classify product contradictions as FAIL, unavailable setup as ENVIRONMENT_ERROR, all observations verified as PASS, and an unattempted case as NOT_RUN.

## Cleanup

Return to Month and Today. Preserve evidence, create no events, and restore original device settings at session end.

# QA-CAL-001-S18 — System Back closes the drawer before leaving Day

## Purpose

Preserve drawer-dismissal priority while adding Day-to-Month Back.

Gherkin feature: CAL-001-F03. Gherkin scenario: CAL-001-S18. Acceptance criterion: AC-CAL-18.

## Preconditions

Use the shared environment rules above: installed target APK, dedicated test environment, UTC, device date 2028-02-15, English United States. Only app GUI and Android public navigation/Settings are allowed.

## Initial state

Day displays 2028-02-20 with the navigation drawer closed.

## Actions

1. Open the navigation drawer from Day.
2. Press Android system Back once.
3. Observe the drawer closes and Day still displays February 20.
4. Press Android system Back again.

## Expected observations

- First Back closes only the drawer; Day and February 20 remain visible.
- Second Back opens Month with the title February without leaving Calendar.

## Failure conditions

First Back leaves Day or does not close the drawer, or second Back fails to open February Month. Classify product contradictions as FAIL, unavailable setup as ENVIRONMENT_ERROR, all observations verified as PASS, and an unattempted case as NOT_RUN.

## Cleanup

Return to Month and Today. Preserve evidence, create no events, and restore original device settings at session end.

# QA-CAL-001-S19 — Day labels occupy the left time column

## Purpose

Verify the source-derived single-day header without changing the global toolbar.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S19. Acceptance criterion: AC-CAL-19.

## Preconditions

Use the shared public-GUI environment rules above, default font/display scale, installed target APK, UTC and English United States. Font/advance-width comparison requires the reference font metrics or same-scale reference capture; if unavailable record ENVIRONMENT_ERROR, not PASS.

## Initial state

Month displays February 2028 with today selected.

## Actions

1. Open the navigation drawer and choose Day.
2. Inspect the weekday/date label above the time column and the blank header area above the event grid.
3. Compare typography at default font/display scale using the pinned reference font metrics or approved reference captures.

## Expected observations

- The weekday and date are centered over the left time column, not the event grid.
- Weekday uses 12sp, date uses 16sp. No extra weekday strip appears above them; the global toolbar still reads February.

## Failure conditions

Centered-over-grid labels, wrong text sizes, extra weekday strip, or missing global month title. Product contradictions are FAIL, unavailable setup is ENVIRONMENT_ERROR, all observations verified is PASS, unattempted is NOT_RUN.

## Cleanup

Return to Month and Today; restore the original device hour format/clock/settings. Do not create events or change app data.

# QA-CAL-001-S20 — Day hour labels follow reference metrics

## Purpose

Verify font size, alignment, and dynamic time-column width in both hour formats.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S20. Acceptance criterion: AC-CAL-20.

## Preconditions

Use the shared public-GUI environment rules above, default font/display scale, installed target APK, UTC and English United States. Font/advance-width comparison requires the reference font metrics or same-scale reference capture; if unavailable record ENVIRONMENT_ERROR, not PASS.

## Initial state

Run both CAL-001-S20 Examples rows independently in English United States; Day displays 2028-02-15.

## Actions

1. Set Android hour format to the Examples row using public Settings, then open the app and select Day.
2. Scroll to the hour thirteen hours after midnight and verify the row hour_label.
3. Inspect hour labels and lines; measure the column against the widest formatted label and reference 12sp typography at the recorded density.

## Expected observations

- 13:00 for 24h; 1 PM for 12h. Hour labels use 12sp and are vertically centered on their corresponding grid lines.
- The time column fits the widest label with 10dp padding on each side, rather than a fixed width.
- Normal Week layout and formatting remain available through the drawer.

## Failure conditions

Wrong format/size, off-line label centering, clipped labels, or a column inconsistent with the stated width/padding. Product contradictions are FAIL, unavailable setup is ENVIRONMENT_ERROR, all observations verified is PASS, unattempted is NOT_RUN.

## Cleanup

Return to Month and Today; restore the original device hour format/clock/settings. Do not create events or change app data.

# QA-CAL-001-S21 — Day initially shows one hour before now

## Purpose

Verify the reference initial scroll without requiring a private clock or state API.

Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S21. Acceptance criterion: AC-CAL-21.

## Preconditions

Use the shared public-GUI environment rules above, default font/display scale, installed target APK, UTC and English United States. Font/advance-width comparison requires the reference font metrics or same-scale reference capture; if unavailable record ENVIRONMENT_ERROR, not PASS.

## Initial state

Month shows today; public device clock is 2028-02-15 12:00 UTC. No vertical scroll has occurred in Day.

## Actions

1. Record the device hour/minute, then choose Day in the navigation drawer.
2. Capture the initial grid before any vertical scrolling and record the observation minute. Repeat setup if a minute boundary is crossed.
3. Compare the visible hour lines/current-time indicator with the initial viewport position.

## Expected observations

- At this midday fixture, the viewport begins one hour before the observed current local time; the current-time marker is approximately one 60dp hour interval below the grid top, subject only to physical-pixel rounding.
- The initial position never goes before midnight or past the available scroll range.

## Failure conditions

Opening two hours before now, an unrelated hour, or a position outside the scroll range. Product contradictions are FAIL, unavailable setup is ENVIRONMENT_ERROR, all observations verified is PASS, unattempted is NOT_RUN.

## Cleanup

Return to Month and Today; restore the original device hour format/clock/settings. Do not create events or change app data.

# QA-CAL-001-S22 — Header dividers continue the time grid boundaries

## Purpose

Verify the two owner-approved line extensions in both Day and Week, retaining today's outline. Gherkin feature: CAL-001-F02. Gherkin scenario: CAL-001-S22. Acceptance criterion: AC-CAL-22.

## Preconditions

Use the shared public-GUI QA environment: installed APK, dedicated device, UTC, date 2028-02-15, English United States, default font/display scale. Run both Examples rows independently. No private app APIs or test-only setup.

## Initial state

Month shows February 2028 with today selected and the navigation drawer closed.

## Actions

1. Open the drawer and choose the row view (Day or Week).
2. Inspect/capture the date header and the top of the time grid.
3. Scroll the hour grid to its beginning; inspect/capture the same header boundaries.
4. Scroll to the end of the day; inspect/capture the same boundaries again.

## Expected observations

- The vertical time-column separator reaches the top of the date header (not the global month toolbar) and aligns with the grid separator below.
- The horizontal divider beneath the date header spans the entire calendar width, including the left time column.
- Both lines remain fixed and continuous at all three scroll positions; dates/view remain unchanged.
- The existing today outline is retained in Day; no change is required to Month or other styling.

## Failure conditions

Missing, partial, displaced or scrolling header dividers, a gap at the header/grid junction, changed date/view, or loss of today's existing outline is FAIL. Unavailable setup is ENVIRONMENT_ERROR, all observations verified is PASS, unattempted is NOT_RUN.

## Cleanup

Return to Month and Today; preserve evidence and restore original device settings. Create no events and clear no data.
