# CAL-001 — Approved navigation/UI revision

## Human request

"quand je suis sur une journée et que je reviens en arrière, je reviens au mois. Ensuite retire la flèche qui affiche le mois stp"; subsequently confirmed to continue.

## Current behavior contract

- System Back from Day returns to Month containing the currently displayed day, rather than closing the app.
- If the navigation drawer is open, Back closes the drawer first and keeps Day active; another Back returns to Month.
- Remove the month-expansion arrow and its dropdown mini-calendar from Week/Day. Month remains accessible through the existing navigation drawer and, from Day, system Back.
- No dependency, permission, event, storage, or notification change.

This explicit owner decision overrides the earlier Proton-reference picker behavior. It is a product revision, not a test assertion weakened to hide a failure.

## Stable IDs and traceability

- CAL-001-S09 / AC-CAL-09 / QA-CAL-001-S09 (expand/collapse picker): retired; do not reuse these IDs or execute their old procedures.
- CAL-001-S13 / AC-CAL-13 / QA-CAL-001-S13 (select date via picker): retired for the same reason.
- CAL-001-S06 / AC-CAL-06: retain timed-view checks but remove the obsolete mini-calendar expectation.
- AC-CAL-16 → CAL-001-S16 → QA-CAL-001-S16: Back opens the containing month; February and March examples.
- AC-CAL-17 → CAL-001-S17 → QA-CAL-001-S17: Week/Day have no expansion control/dropdown and retain their time grid.
- AC-CAL-18 → CAL-001-S18 → QA-CAL-001-S18: drawer dismissal has priority over Day-to-Month Back navigation.

Unchanged requirements retain their IDs. This Back/arrow revision had 16 scenario definitions and 35 expanded cases across the same three feature IDs. The subsequent Day source correction (`day-reference-correction.md`) extends the active suite to 19 scenarios / 39 cases. Earlier 33-case test/DRY reports are historical evidence, not results for this revision. Parser and development regression results for this revision are recorded in checks/coder-ui-revision.md and state.json. External QA and Android-17 GrapheneOS verification remain unperformed.
