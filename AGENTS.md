# app-suite — Instructions for Coding Agents

## Layout and ownership

- `apps/calendar-app/` (`:calendar`): Calendar source, tests, tools, public signing pin, README and app-specific AGENTS.md. Read both app documents before changing Calendar or its integration.
- `shared/design/` (`:design`): reusable presentation consumed by Calendar. Public API: `app.suite.design.SuiteColors`. No app behavior, data, permissions or signing credentials here.
- `docs/`: developer guides for design consumption, independent apps and publication.
- `.ai/`: shared workflow tracking and task sources of truth. Preserve existing task/scenario/QA IDs and history; no per-app copies of tasks.
- Root Gradle files and wrapper: common plugin versions, module registration and repository policy. No app-specific scripts or second wrapper at root.

Only Calendar currently exists. See README.md, docs/design.md and docs/adding-apps.md. New apps require separate Android identities, private data, permissions, versions and externally managed signing credentials. Share existing presentation by module dependency, not by copying it. Do not introduce speculative frameworks/components or share business logic merely because another app might need it.

## Changes

- Implement only approved behavior, one task feature at a time. Ask before implementation changes outside approved scope or before adding any external dependency. Preserve existing user work.
- Reuse existing code, tests, parser and acceptance pipeline. No second parser/runner, hand-editing generated acceptance, or executable external-QA scripts as incidental work.
- MONOREPO-001 explicitly reuses existing tests; owner declined new tests. No second/fake app, CI, events, storage, reminders or visual redesign in this migration.
- Never commit or push. The owner handles commits, pushes and publication. Retain `d-s-i/calendar` without renaming/deleting/modifying it; new destination is `d-s-i/app-suite`.
- Never publish credentials or place them in Git or the Nix store. Never regenerate Calendar's release key, change its identity, fall back to debug signing or erase data to bypass a failure.
- Do not run broad CRAP/DRY cleanup, source/Gherkin mutation or property tests unless explicitly requested under the appropriate role.

## Verification and delivery

Run commands from the repository root; use the one checked-in wrapper. See apps/calendar-app/README.md for environment, outputs, test-device setup and exact delivery commands.

```sh
./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:generateAcceptance
./gradlew :calendar:lintDebug :design:lintDebug :calendar:assembleDebug
./gradlew :calendar:assembleRelease :calendar:testReleaseArtifact
ANDROID_SERIAL=<dedicated-test-serial> ./gradlew :calendar:acceptanceTest
```

After shared changes, check all actual consuming apps (currently Calendar only). Keep unit and generated acceptance tests separate. Use a dedicated offline AOSP test emulator without Play services for fixtures, never the owner's emulator/phone.

After each approved app implementation, rebuild debug, discover live devices and verify the owner's persistent visible AVD name `android-dev`. Install in place with the exact APK path and launch Calendar; both must succeed. Preserve all data/signatures, never uninstall/clear/wipe, and leave the owner emulator running with Calendar open. If stopped, use `android start` from the canonical `~/nixos-config/common/android-shell.sh`, not a temporary/read-only instance. Do not assume a fixed serial; ask if multiple matching owner instances exist.

Report changed files, actual command results, delivery serial/install/launch status and unverified surfaces. Build/UI regression success is not external QA, GrapheneOS verification or published release. MONOREPO S07 is exclusively the owner's release-APK check; never mark it PASS without owner feedback. Historical evidence remains historical; record fresh results after moves rather than rewriting old commands.
