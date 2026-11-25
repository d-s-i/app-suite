# Current Task

- Task: MONOREPO-001 — Transformer Calendar en monorepo multi-apps avec un design réutilisable
- Role: coder
- Status: local implementation verified; blocked on owner publication inputs/actions
- Current feature: MONOREPO-001-F03 (needs_input)

Specification: tasks/MONOREPO-001/specification.md. Three features F01–F03, seven scenarios S01–S07, matching QA delivery checklist in qa/procedure.md. F01/F02 implementation completed; F03 publication pending. parse-gherkin passed again on all MONOREPO features; no new tests or runner were added, as expressly approved by the owner. Prior specifier advisory review remains in checks/specifier.md.

Owner decisions: create new GitHub repository d-s-i/app-suite; retain d-s-i/calendar without renaming/deleting it; publish Calendar as part of the migration; owner personally verifies the release APK. No second/fake application required for QA. No production/architecture changes or external publication performed during specification. Commits/pushes remain the owner's responsibility.

Implementation: workspace /home/anon/Documents/Workspace/app-suite; Calendar is now :calendar in apps/calendar-app/ with its own tools, README, AGENTS and public signing pin; shared :design remains in shared/design/ and is consumed without copying. Root contains only common build/configuration, guides and task tracking. F01/F02 revalidated after the final organization: clean debug/release builds, 18 JVM tests, 4 generator tests, 49 UI acceptance cases, lint and 3 signature checks pass. Missing secrets correctly fail packaging. Final debug installed in place and launched on android-dev / emulator-5554, data/firstInstallTime preserved. See tasks/MONOREPO-001/checks/coder-organized.md for current commands, paths, hashes and verification limits; earlier coder.md is historical.

Next: owner chooses repository visibility, release tag and update-version baseline, creates d-s-i/app-suite and commits/pushes reviewed work to the explicit app-suite remote (origin still points to calendar). No commit/push or GitHub operation performed. Publication/owner APK control S06/S07 remain NOT_RUN. No complete-task delivery claim until those actions and feedback are recorded.

EVENTS-001 update: owner approved D2 after the repeated-hour explanation; all D1–D4 decisions are now approved. Its written specification is ready_for_review: seven features, 40 scenario definitions / 95 cases, matching public-GUI QA, parser checks passed and advisory findings reviewed. See tasks/EVENTS-001/specification.md, checks/specifier.md and state.json. No EVENTS implementation started; complete-spec owner approval is still pending. This MONOREPO-001 global tracking context is otherwise preserved.
