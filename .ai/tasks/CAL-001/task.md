# CAL-001 — Browse empty month, week, and day calendar views

## Original request and scope history

"create these 4 tasks CAL-001 EVENTS-001 REMINDERS-001 PERSISTENCE-001" — CAL-001 represents the minimalist calendar. The initial task proposed displaying events; during specification the owner explicitly reduced CAL-001 to empty calendar views/navigation, leaving events for later tasks. The stable task ID is unchanged.

## Objective and affected user

Let the owner browse dates through empty Month, Week, and Day views using Proton Calendar Android's calendar UI as the reference.

## Confirmed requirements and constraints

- First launch opens Month; weeks start Monday; dates follow the device regional format.
- The owner requested the same month/week/day calendar views as Proton Calendar Android and approved inspecting its official public source rather than reverse engineering its binary.
- Use the latest reference, pinned at inspection to the verified official APK 2.34.2 (361) and matching source commit. See reference.md for evidence and inspection limits.
- CAL-001 covers empty calendar views/navigation only; event creation, event rendering, recurrence, and reminders are deferred.
- For later populated views, month cells show event titles and +N more opens the full day. These remain product requirements but are not accepted by this task.
- Kotlin + Jetpack Compose; Pixel 9 Pro / Android 17 GrapheneOS as reported by the owner, not device-tested.
- Fully local operation without Android Calendar Provider, Google integration, or Google Play services; free to use and installable by APK.
- Prioritize privacy, offline operation, minimalism, and minimal dependencies.
- Ask before implementation changes or adding dependencies. The owner handles commits and pushes.

## Specification scope

- Empty Monday-first month grid and its date presentation, including adjacent-month dates and today.
- Empty Week and Day time grids, localized dates/time labels, and current-time indicator.
- Drawer view selection, period paging, date selection, Today, and system Back from Day to its containing Month.
- Owner explicitly removed the expansion arrow/dropdown mini-calendar after implementation; see ui-revision.md for the approved override and retired scenario IDs.
- The approved Day-only source correction places labels over the time column, uses source-derived hour typography/width/centering, and initially scrolls one hour before now; Week and the global month toolbar are retained. See day-reference-correction.md.
- Version 0.1.1 follows the owner's phone feedback: complete single-line hour labels with dynamic, pixel-safe widths in both timed views, plus capital initials for localized month headings. See hour-labels-and-headings-v011.md.
- Three sequential behavior slices: CAL-001-F01, CAL-001-F02, CAL-001-F03. See specification.md for acceptance criteria and complete scenario-to-QA traceability.

## Out of scope

Event functionality, populated-calendar behavior, notification delivery, persistence implementation, accounts, synchronization, Proton branding, subscription/search UI, 3 Days and Agenda modes, optional week numbers, and source/asset imports. No architecture is chosen here.

## Verified repository state

An Android implementation, Gradle build, unit tests, generated Compose acceptance runtime, and debug APK now exist. See README.md for commands. External QA remains separate from development acceptance.

## Assumptions and unresolved items

- The pinned release freezes "latest" for reproducibility.
- Source-derived calendar behaviors operationalize the approved external reference and require owner review of the extracted contract before coding.
- UTC/Gregorian fixture dates, portrait/light appearance, and default display/font scales are QA setup conditions, not invented product platform restrictions.
- No blocking behavior question remains for the extracted contract. The owner approved implementation and requested the documented Back/arrow revision. Rendered reference captures, pixel-level visual comparison, and target-device execution have not been performed; see reference.md and specification.md for the visual review boundary.
- Do not infer data persistence or saved-view behavior from the empty-view specification.

## Artifacts and next role

- reference.md: pinned reference and independently inspected source observations.
- specification.md: confirmed scope, acceptance criteria, slices, assumptions, and traceability.
- features/: three Gherkin features with stable IDs.
- qa/procedure.md: public-GUI QA procedures, one per stable scenario ID.
- checks/specifier.md: validation commands, actual results, and advisory finding review.
- ui-revision.md: approved Back/arrow change and retired scenario IDs.
- state.json: tracking source of truth for implementation and verification status.

Next action: owner review. The latest APK is verified and installed on the shared android-dev emulator; see state.json and checks/coder-v011.md. External QA and GrapheneOS device checks remain unperformed.
