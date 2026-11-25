# Publishing Calendar from app-suite — owner actions

Destination: **`d-s-i/app-suite`**, a new repository. Keep **`d-s-i/calendar`** at
its existing address; do not rename, delete, archive or publish changes there as
part of this migration. No GitHub publication has been performed by the agent.

## Before publishing

- Choose repository visibility and the Calendar release tag with the owner.
- Confirm every release/installed version that the new release must replace.
  The checkout still has `versionName = "0.1.1"`, `versionCode = 2`; these were
  preserved, not selected as a new publication version. Do not publish an update
  over code 2 until an approved higher code/name is set in `apps/calendar-app/build.gradle.kts`,
  rebuilt and verified. The tag must agree with that version.
- Verify a secure external backup of the existing Calendar signing credentials.
  Do not create a new key for this repository or copy credentials into it.
- Review **all** intended files: the checkout includes pre-existing uncommitted
  work and untracked task documents. Preserve `.ai/tasks/{CAL-001,EVENTS-001,
  PERSISTENCE-001,REMINDERS-001,MONOREPO-001}` and existing Git history; do not
  initialize a new unrelated history or publish only an exported snapshot.
- Review the final staged diff and history for secrets before pushing. Publish
  the public certificate pin, never keystores/passwords, local SDK configuration,
  APK build directories or private signing state. A limited local content review
  is not a general security audit.

## Publish the reviewed code

Create `d-s-i/app-suite` on GitHub with the chosen visibility, **without** an
initial README/license/commit so the existing history can be pushed directly.
The local workspace already has:

```text
origin     git@github.com:d-s-i/calendar.git
app-suite  git@github.com:d-s-i/app-suite.git
```

Remotes are local Git configuration, not versioned files. In another clone, add
`app-suite` explicitly if absent. The owner reviews/stages/commits the intended
work, then pushes the reviewed branch to the **explicit `app-suite` remote**:

```sh
git remote -v
git status --short
git diff --cached --check
git diff --cached --stat
# After the owner's review and commit:
git push -u app-suite HEAD
```

Do not use a bare `git push` while the old branch may still track `origin`.
Do not force-push or change the old repository. Confirm on GitHub that the new
repo contains the prior commit history, Calendar, shared design and task files.
The agent has made no commits/pushes and has not created either remote repository.

## Build and attach the signed APK

After selecting the update version and committing the reviewed source, follow
[the Calendar build/signing instructions](../apps/calendar-app/README.md#signed-release-for-github--obtainium).
Run the full documented verification set including the existing CAL-001 UI
acceptance on a separate offline emulator. Then:

```sh
./gradlew :calendar:assembleRelease :calendar:testReleaseArtifact
sha256sum apps/calendar-app/build/outputs/apk/release/calendar-release.apk
```

Create the approved tag/release in `d-s-i/app-suite` at the reviewed commit and
attach **only the verified release APK** (and optionally its public checksum),
not `calendar-debug.apk`, an unsigned APK, an AAR or any signing files. Application ID
must remain `app.local.calendar`, non-debuggable and signed by the existing pin
in `apps/calendar-app/release-certificate.sha256`. Do not reuse a tag for a different build.

The owner personally downloads/checks the attached APK; comparing its SHA-256 to
the verified local APK is sufficient without reinstalling the phone. Record the
repo URL, release URL, commit/tag, APK version/code/hash and owner's actual
verification result in the task delivery record. Until then S06/S07 are NOT_RUN,
not PASS. Obtainium's tracked repository can then be changed by the owner to
`d-s-i/app-suite`; no Obtainium configuration or installation was tested here.
Future multi-app release selection is a separate decision.

If an installation has a debug certificate, a release cannot update it in place.
Never uninstall/clear data, change `applicationId`, regenerate a key or fall back
to debug signing to make publication appear successful.
