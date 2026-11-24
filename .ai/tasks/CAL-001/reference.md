# CAL-001 — Pinned Proton Calendar reference

## Identity and evidence

Inspected on 2026-11-23 UTC. Owner approved inspecting official public source instead of binary reverse engineering and requested the latest version.

- Official download page: https://proton.me/calendar/download
- Official APK link from that page: https://protonmail.com/download/CalendarAndroid/ProtonCalendar-Android.apk
- Downloaded to a temporary directory outside this project. `apkanalyzer manifest version-name` returned `2.34.2`; `apkanalyzer manifest version-code` returned `361`.
- APK SHA-256: `bec773926aa6d328c4d31857ab017626b6299936ade5d9851f833545ab761b25`.
- Official source: https://github.com/ProtonMail/android-calendar
- Latest published release branch observed with `git ls-remote --heads --tags`: `release/2.34.2`.
- Pinned commit: `113b3df2a64cc2d95443ac962216e93f3710a2ca`.
- Immutable source root: https://github.com/ProtonMail/android-calendar/tree/113b3df2a64cc2d95443ac962216e93f3710a2ca
- README states GPLv3 for code/data. Inspection does not authorize importing code, assets, dependencies, or selecting this project's license.

This freezes "latest" at inspection time, not a continuously changing requirement. No third-party source or APK is stored in this project. The APK was inspected for version metadata only; it was not installed or decompiled. No screenshots or rendered visual comparison were produced.

## Source observations used by this specification

All paths below are relative to the immutable source root. These are reference evidence, not architectural instructions for our implementation.

| Reference surface | Observed public behavior | Evidence |
|---|---|---|
| Month | Seven columns, six rows, 42 consecutive dates including adjacent-month dates, full-height calendar body | `app/src/main/java/me/proton/android/calendar/presentation/calendar/customView/MonthView.kt`, `fragment/ItemCalendarMonthFragment.kt` under the same `calendar` directory; `app/src/main/res/layout/item_calendar_month_fragment.xml` |
| Month styling | Equal cells, dividing lines, centered day numbers, muted adjacent-month numbers, outlined rounded rectangle around today | `MonthView.kt` drawing and paint configuration |
| Month measurements | Header 24dp; grid separators 1dp; day numbers 12sp; date-number baseline 18dp below row top; today's outline 21dp wide, 20dp high, radius 5dp, stroke 1dp | `app/src/main/res/values/dimens.xml` |
| Select a date | Tapping a month date opens Day for that date, including adjacent-month dates | `fragment/ItemCalendarMonthFragment.kt`, `setMonthGridClickableItems` |
| View selector | Drawer entries Day, Week, Month | `app/src/main/res/layout/nav_view_main.xml`, `app/src/main/res/values/strings.xml`, `app/src/main/java/me/proton/android/calendar/presentation/main/MainActivity.kt` |
| Week / Day | Seven / one visible date columns; time labels on left; horizontal hour separators; vertically scrollable time grid rather than an agenda list | `fragment/MonthFragment.kt`, `app/src/main/res/layout/fragment_month.xml`, `app/src/main/java/me/proton/android/calendar/common/Constants.kt`, `week-view-core/src/main/java/com/alamkanak/weekview/CalendarRenderer.kt` |
| Time scale | Default hour height 60dp; formatting follows chosen/device 12h/24h format; current-time line and dot enabled. With time-column hour separators disabled, the first hour label is 01:00, not 00:00 (`ViewState.timeRange`). | `fragment_month.xml`, `values/dimens.xml`, `fragment/MonthFragment.kt`, `week-view-core/.../ViewState.kt` |
| Date header | Tapping a week date header opens Day for that date | `fragment/MonthFragment.kt`, `dateHeaderClickHandler` |
| Date picker | Week/day views have a collapsible mini-calendar, initially collapsed when entering from Month | `fragment/MonthFragment.kt`, `initAgendaPager`, `simulateExpandWithScroll`, `simulateCollapseWithScroll` |
| Period navigation | Month pager and horizontally scrolling timed views navigate dates; week view aligns to configured week start | `fragment/MonthFragment.kt`, month pager callbacks and week-view range handler |
| Today | Calendar icon containing today's day number returns to today; in timed views it also returns toward current time | `fragment/MonthFragment.kt`, `setToolbarListeners` |
| Toolbar title | Localized month name; append year when it differs from the current year | `fragment/MonthFragment.kt`, `setToolbarMonthYearTitle` |

## Product overrides and scope boundaries

Explicit owner decisions override Proton defaults: first launch is Month; week starts Monday; dates follow device regional formatting; the app is fully local and needs no Google Play services. CAL-001 covers empty calendars only. The subsequent owner-requested revision removes Proton's expansion arrow/dropdown picker and makes system Back from Day open its containing Month; see ui-revision.md. Source observations above remain historical reference evidence, not requirements overriding that decision.

Account controls, login, synchronization status, subscription UI, event search/create actions, event blocks, all-day event behavior, 3 Days and Agenda modes, settings, and optional week numbers are not required by CAL-001. Month title/overflow requirements for populated calendars remain deferred, not removed from the product.

## Visual review boundary

Acceptance below concretely specifies the source-observed empty calendar structure, measurements, date presentation, and interactions. It does not certify screenshot equality: no rendered reference baseline exists yet. Before approving implementation, the owner should review whether this extracted contract is sufficient for the requested Proton-like UI or supply/approve reference screenshots for additional exact visual requirements. Do not claim pixel-perfect parity from passing structural scenarios.
