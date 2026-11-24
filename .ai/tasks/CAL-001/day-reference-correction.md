# CAL-001 — Day reference correction

The owner requested use of the existing Proton source instead of waiting for screenshots, then approved the concrete Day-only correction. No new dependencies or permissions.

## Independently verified reference

Pinned Proton 2.34.2 source commit: 113b3df2a64cc2d95443ac962216e93f3710a2ca. Paths below are within that source, not our implementation requirements for private APIs:

- `week-view-core/.../HeaderRenderer.kt`, single-day label branch: weekday/date use the time-column bounds on the left, not the center of the event column. Weekday 12sp, date 16sp, medium typeface; header padding 12dp and 2dp between weekday and date outline. Today's outline: 24dp square, 5dp corner radius, 1.5dp stroke.
- `app/src/main/res/layout/fragment_month.xml`: hour labels 12sp; time-column padding 10dp each side; default hour height 60dp. Short ticks extend 10dp into the time column.
- `week-view-core/.../ViewState.kt`: time-column width is widest formatted label plus both paddings, not a fixed 52dp.
- `week-view-core/.../TimeColumnRenderer.kt`: label layouts are vertically centered on hour lines. Text layout excludes font padding.
- `ViewState.renderCurrentTime` and `DateTimeUtilsImpl.getTimeWithPadding`: initial position is one hour before now; within the first hour, start at midnight. Scroll is bounded by the available range.
- The 12h `h a` and 24h `HH:mm` formatting already matches our formatter; it is retained.
- `MonthFragment.setToolbarMonthYearTitle`: the global month toolbar remains present in the reference. It is retained here, as explicitly stated in the approved proposal.

The Week implementation is intentionally unchanged by this Day-only correction. Month, system Back, and the removed expansion control remain unchanged.

## Traceability

- AC-CAL-19 → CAL-001-S19 → QA-CAL-001-S19: Day weekday/date above the left time axis, reference text sizes, no extra weekday strip, global month title retained.
- AC-CAL-20 → CAL-001-S20 → QA-CAL-001-S20: 12sp hour labels, vertical centering, dynamic width plus 10dp padding each side; 12h and 24h examples.
- AC-CAL-21 → CAL-001-S21 → QA-CAL-001-S21: initial Day scroll one hour before the current local time, bounded by scroll limits.

These extend CAL-001-F02 without renumbering existing IDs. This Day correction had 19 scenarios / 39 expanded cases. The later owner-approved Day/Week header-divider extension (S22) brings the active suite to 20 scenarios / 41 cases; see checks/coder-header-dividers.md. Previous revision reports remain historical evidence. This correction is source-guided, not a claim of completed rendered Proton screenshot parity or Android-17 GrapheneOS verification.
