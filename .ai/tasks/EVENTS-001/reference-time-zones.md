# EVENTS-001 — Proton time-zone reference and approved policy

Owner asked how Proton handles time zones, then approved the explained policy. No adoption of unrelated Proton features or limits.

## Independently inspected source

Official Proton Android 2.34.2, commit 113b3df2a64cc2d95443ac962216e93f3710a2ca:
https://github.com/ProtonMail/android-calendar/tree/113b3df2a64cc2d95443ac962216e93f3710a2ca

- `app/src/main/java/me/proton/android/calendar/presentation/calendar/viewModel/EventViewModel.kt`: display zone comes from the calendar's configured primary time zone; a timed event defaults to that zone unless another event zone is supplied. Editing uses the event's own zone; viewing uses the display zone.
- `common/utils/ICalUtilsImpl.kt`: timed start/end carry time-zone assignments. All-day handling uses dates without a time component.
- `domain/model/Event.kt`, getOccurrenceStart/End: timed occurrences convert with the same instant to the requested display zone; all-day occurrences retain local dates.
- `app/src/test/java/me/proton/android/calendar/domain/model/EventTest.kt`, DST recurrence test around lines 205–290: a recurring Zurich event keeps the same local time there; Auckland display times change as each zone changes DST on different dates.

These files/tests were read, not executed as a Proton app test suite. No Proton app/device runtime verification is claimed.

## User-visible interpretation

A recurring 09:00 Europe/Paris appointment remains 09:00 in Paris across normal summer/winter offset changes. Viewing it in a different calendar display time zone converts the displayed hour; it does not redefine the appointment as 09:00 everywhere. All-day events preserve their calendar dates.

Proton's calendar display zone is a configured setting: this source observation is not evidence that it always follows the phone automatically when travelling. Owner-approved simplification for this app: use the phone's zone at creation as the event origin zone, retain the event's local clock time there, and display converted times in the current phone zone.

The inspected general Proton policy did not settle nonexistent/ambiguous DST times. The owner subsequently approved this app's special-time decisions in remaining-decisions.md; they are not claimed as verified Proton behavior. Monthly missing-date handling was subsequently decided by the owner and is recorded in task.md.

## Supporting official documentation

https://proton.me/support/protoncalendar-create-modify-events documents event time-zone selection and all-day/timed creation. https://proton.me/support/protoncalendar-create-update-and-delete-recurring-events documents custom day/week/month recurrence and recurrence ending controls. Unrelated feature sets, server limits, participant rules and storage architecture are not adopted by referencing these pages.
