# CAL-001 — Specifier validation report

## Result

Three APS-subset feature files parsed successfully: 15 scenario definitions, 33 expanded cases, 15 matching public-GUI QA procedures. This validates specification syntax/consistency, not application behavior.

## Commands actually executed

For each name `empty-month`, `empty-timed-views`, and `calendar-navigation`, executed:

```sh
parse-gherkin .ai/tasks/CAL-001/features/<name>.feature .ai/tasks/CAL-001/acceptance/<name>.json
check-gherkin-dry .ai/tasks/CAL-001/acceptance/<name>.json .ai/tasks/CAL-001/acceptance/<name>-dry-report.json
```

`<name>` documents the three actual invocations, not a literal command argument. All six final invocations exited 0. Both checks were rerun on all features after Gherkin revisions.

No project build, source tests, source-code DRY/CRAP/mutation checks, acceptance runtime, or executable QA scripts were created or run.

## Advisory Gherkin DRY review

Final reports contain 7 findings for empty-month, 9 for empty-timed-views, and 6 for calendar-navigation. Findings are advisory, not a claim of zero duplication.

Changes made after first review:
- Unified weekday-header assertions and 42-date-range vocabulary.
- Removed a 24-hour Background setting that was not genuinely shared with the 12-hour scenario.
- Unified setup/assertion range wording without conflating their purposes.
- Independently inspected reference hour-label generation: the configured reference starts its visible hour labels at 01:00, not 00:00. Updated the scenario and QA expectation before the final check run.

Reviewed and intentionally retained findings:
- `duplicate-in-scenario` for S07: the same date range occurs in setup and in the final assertion. This checks preservation after vertical scrolling, not a duplicated action.
- `duplicate-in-scenario` for S09: mode is checked after expansion and again after collapse, two distinct state transitions. Removing either would weaken coverage.
- `placeholder-variant` findings distinguish initial versus expected months/ranges. Those values have different input/output roles and must not be collapsed into one parameter.
- Literal and parameterized values share canonical templates (month opening, toolbar titles, date ranges, hour formats, view mode). These are intentional reusable vocabulary, not redundant scenarios.
- Expand/collapse, start/end scrolling, Day/Month/Week, and first/last hour labels are different actions or expectations despite lexical similarity. Do not merge opposite behaviors.
- Specific Day assertions and parameterized mode assertions have equivalent vocabulary but different scenario scope. No additional Examples columns were added merely to silence the report.

## Additional consistency verification

An inline Node assertion check (not a saved utility or QA script) read the final parser IR and documents and verified:
- Unique feature and scenario IDs.
- Exactly one QA heading per scenario and a matching acceptance-criterion row.
- Every Examples column is used and each step placeholder has a value.
- Fixture range lengths, Monday week starts, and timed paging offsets.

Result: passed; details in `specification-consistency.json`. This is a static specification check, not a product test result.

## Reference verification

Official release source was inspected outside the repository. The official download APK manifest reports version 2.34.2, code 361, matching the latest published source release branch observed. Reference commit, URLs, hash, relevant source surfaces, and visual evidence limits are documented in `../reference.md`.

## Not verified and approval needs

- App execution and device QA: NOT_RUN; no target app exists yet.
- Rendered Proton reference capture and target screenshot comparison: NOT_RUN.
- Pixel-perfect UI parity: not established by source inspection or these structural checks.
- Owner must review the extracted contract in `../specification.md`, including the visual boundary, before implementation. No dependency/source/asset import or project license choice is approved here.

Task status: ready_for_review, not ready_for_coding. No feature has been started.
