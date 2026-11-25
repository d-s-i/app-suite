# Calendar GitHub publication

Owner explicitly requested the agent upload the release, then completed GitHub CLI device authorization. This authorizes the release operation only; no agent code commit/push or old-repository modification was performed.

## Verified code and build

- GitHub repository: `d-s-i/app-suite`, private at publication, subsequently made **PUBLIC** by the owner; default branch `main`. Public release-page access was verified without authentication after the owner's change.
- Owner's pushed commit: `39ac557211188e3bbc03c39406e1914c0e8c7c6d` (`Organize app-suite monorepo and shared design`), same as local HEAD and remote main. Working tree clean immediately before publication; earlier history retained.
- Existing local APK metadata: app.local.calendar, release 0.1.1 / versionCode 2. No version increase or new functionality claimed; this is the initial publication of the existing version in the new repository, not an update over an installed code 2.
- Rebuilt/reverified with `nix develop ~/nixos-config#android --command bash -c './gradlew :calendar:assembleRelease :calendar:testReleaseArtifact'`: PASS; three existing artifact checks passed (existing release certificate pin, application ID/non-debuggability, debug signature distinct).
- Final release SHA-256: `1952c5d328e6fa749f660c42e360b92467ad274b83e08ac6c4b86cd2977c0e39`.

## Published release

- URL: https://github.com/d-s-i/app-suite/releases/tag/calendar-v0.1.1
- Tag: `calendar-v0.1.1`; title: `Calendar 0.1.1`.
- Target commit: `39ac557211188e3bbc03c39406e1914c0e8c7c6d`.
- Published, not draft, not prerelease.
- Assets confirmed by GitHub metadata:
  - `calendar-release.apk`, 5,102,845 bytes, state `uploaded`; server digest equals the local SHA-256 above.
  - `calendar-release.apk.sha256`, 87 bytes, state `uploaded`.
- `gh release create` used explicit repository/target commit/title/notes and the verified APK plus public checksum. GitHub CLI was run with `nix shell nixpkgs#gh`; no project dependency/configuration added. No `git commit` or `git push` executed by the agent. No credentials uploaded or recorded in task evidence.

## Owner feedback and coder closure

The owner reported « ok j'ai mit en public », then followed the Obtainium setup/installation instructions and confirmed « oui installé ». This is recorded as owner-confirmed release installation for S07; no agent phone installation, owner checksum comparison or completed GrapheneOS QA is claimed. The owner subsequently requested « ok go et cloture » for tracking update and coder closure.

The release is now public; authenticated access is no longer needed merely because of repository visibility. Obtainium installation is confirmed by the owner, not by agent-operated phone testing. No modification/deletion of `d-s-i/calendar` was performed. The owner stated that repository is not needed; this does not mean it has been deleted.

F03 and the coder role are completed, with no pending coder delivery inputs. Cleaner/architect/hardener/QA roles remain not_run; coder closure does not mark those reviews or the entire pipeline completed. Existing verification limitations (including exact public-Settings S02 QA) remain as recorded in `coder-organized.md`.
