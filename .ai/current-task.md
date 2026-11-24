# Current Task

- Task: CAL-001 — Browse empty month, week, and day calendar views
- Role: coder (completed)
- Current feature: none
- Status: ready_for_review

Latest approved correction: vertical time-column separator extends through the full date header, with a full-width horizontal header bottom divider, in both Day and Week. Today's outline is retained. Earlier Day typography/positioning and one-hour initial offset, Back-to-Month, global toolbar, and removed expansion control remain unchanged.

Verification: 17 JVM unit tests, 4 generator tests, 41 generated acceptance cases / 20 scenarios passed. Lint 0 errors / 8 existing update warnings; debug APK built. Report: tasks/CAL-001/checks/coder-header-dividers.md and JSON/XML evidence. External QA, rendered Proton parity and Android-17 GrapheneOS execution remain unverified.

Installed in place on shared persistent AVD android-dev (emulator-5554), verified by AVD identity. Calendar is running and open on Week; data preserved. The isolated test emulator is stopped. No commits/pushes; no other task started.

Android CLI configuration is managed under ~/nixos-config/common. `android`, `android start`, `android stop`, and `android install [APK]` share the existing guarded backend; legacy hyphenated commands remain compatible. Thirteen command tests and SDK entrypoint build passed. Updated Home Manager .bashrc built and checked, NOT activated. Owner can source ~/nixos-config/common/android-shell.sh in the current Bash terminal; automatic startup in new terminals awaits usual Home Manager/NixOS activation. No broad system rebuild was performed.
