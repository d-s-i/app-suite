# MONOREPO-001 — Initial coder blocker (resolved)

Owner subsequently confirmed: « oui non pas besoin de nouveaux tests ». The proposed exception is approved; this initial blocker no longer applies. See `coder.md` for the implementation and remaining owner-publication prerequisites. The following is the historical pre-implementation record.

## Inspection

Read task/state, approved specification, clarification, all three feature files, matching QA procedure, root instructions, README, Gradle configuration and existing acceptance generator. No `.ai/architecture.md` exists. Existing worktree contains extensive tracked and untracked owner work; no implementation files were modified.

The existing pipeline is `.feature` → `parse-gherkin` → `tools/generate-acceptance.mjs` → generated Kotlin → `CalendarSteps` → connected Android tests. Its stable feature/scenario IDs, output directory and runtime are specific to CAL-001. It cannot execute MONOREPO-001 features unchanged.

## Decision required

The approved MONOREPO specification explicitly requests reuse of existing commands and a delivery checklist, with no new runner or handlers. The coder workflow requires generated executable development acceptance for the approved Gherkin. Reusing CAL-001 verifies Calendar regression behavior but does not execute MONOREPO-001 scenarios as generated tests. Do not label parser success or checklist review as generated acceptance success.

Requested clarification: authorize the task-specific exception of using existing CAL-001 acceptance plus the approved MONOREPO delivery controls, without adding a new MONOREPO automation layer. Alternatively, explicitly approve the required automation scope before implementation. Publication and S07 remain owner actions, never automatically passed.

## Verification status

No tests, builds, parser invocation, installation or external operations performed. Tool discovery found Node, pnpm and parse-gherkin on PATH; Java and adb are absent from the current PATH. The canonical Nix Android shell helper was read; Android environment availability was not tested and is not established as a blocker.

F01: needs_input; F02/F03: not started. Repository visibility, release tag, publication version baseline and owner commits/pushes remain delivery prerequisites, not presumed approvals.
