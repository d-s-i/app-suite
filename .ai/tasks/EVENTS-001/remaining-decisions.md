# EVENTS-001 — Approved decisions D1–D4

Owner approved D1, D3 and D4, then approved D2 after the explanation of the repeated autumn hour. All four decisions are adopted as recorded here. The resulting specification/Gherkin/QA still requires owner review before coding.

## D1 — Form defaults and navigation

- Creation only through +. Default: one-time, all-day, empty title and description. An hour must be chosen explicitly when switching to timed mode; automatic duration is 60 elapsed minutes with no duration/end-time control.
- Default date: Day uses its displayed date. Month/Week use today when it belongs to the displayed month/week; otherwise use the first date of that month/week (not a spillover date in the month grid).
- Saving a new event returns to the originating calendar view mode and the date of its first resulting occurrence. Saving edits returns to that mode on the selected occurrence if it still exists, otherwise on the first resulting affected occurrence. Cancel/Back abandons uncommitted changes and restores the original calendar period. A discard confirmation is allowed; nothing is committed without Save.
- Existing month-date taps and week-date-header taps keep opening Day. Empty timed-slot taps never create events.

## D2 — Nonexistent/ambiguous local times and editing zone

- For an invalid local time in a time-zone gap, shift it forward by the actual gap duration. Europe/Paris 02:30 in the spring one-hour gap becomes 03:30. Show the adjustment before saving; the recurrence's requested clock time stays unchanged on normal dates.
- For a local time that occurs twice, choose the earlier instant (first occurrence) and create only one start, not two.
- Timed end is start plus 60 elapsed minutes, including across clock changes. All-day events keep their literal calendar date and are not shifted.
- Editing an existing timed event uses its original zone and labels that zone. Calendar display continues to convert to the current phone zone. New timed information uses the phone zone captured when the form is opened (including adding an hour to a previously all-day event). Do not silently change it while editing.

These are owner-approved explicit app policies, not a claim that every special case was verified in Proton. The existing Proton reference only established the general origin-zone model.

## D3 — Existing individual modifications during series editing

- Series-wide title/description changes preserve individual overrides. An explicitly overridden field keeps its local value; fields not locally overridden inherit the changed series value.
- Changes to date, clock time, all-day/timed mode, or recurrence reset individual modifications and cancellations in the affected portion only, after an explicit warning and confirmation. This can restore individually deleted occurrences when the new rule generates them; the warning must say so. Cancelling the warning leaves all saved data unchanged. No warning is needed when no affected individual modifications/cancellations exist.
- One-occurrence editing/deletion never changes other occurrences. This-and-future uses original nominal series order, not the reordered displayed dates of moved instances. Whole series includes past and future. Untouched schedule/end fields must not change merely because an edit was initiated from a later occurrence.

This approved policy avoids silently discarding individual edits for simple text changes, and avoids silently carrying incompatible temporal exceptions into a changed schedule.

## D4 — Automatic hour crossing midnight

- There is no user-entered multi-day date range. An automatic 60-minute timed event may nevertheless finish on the following date: 23:30 becomes 00:30 next day. Never truncate it or reject a valid start merely to keep its end on the starting date.
- This is one occurrence, not two events. In the current phone display zone, Day/Week show the portion overlapping each displayed date; Month includes its title on each overlapped date, without counting two logical occurrences. Reminder delivery remains REMINDERS-001's responsibility; visual fragments must not become extra occurrences. Exact-midnight end is exclusive and does not occupy the next date.
- All-day events remain one calendar date. No manual end-date/duration controls are introduced.

D4 clarifies the distinction between no multi-day entry and an unavoidable automatic midnight crossing. Owner explicitly confirmed this clarification of the prior single-date-entry choice.

## Completion gate

All D1–D4 are approved. Features, criteria/traceability, public-GUI QA and deterministic validation reports are produced under this task. The next gate is explicit owner approval of the ready_for_review specification, not automatic coding. No architecture, storage technology, dependencies, production implementation or executable QA scripts are selected here.
