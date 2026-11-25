# Local release-signing preparation

## Authorization and scope

Owner approved creation of a durable release key and preparation of the signed APK for a later GitHub Release / Obtainium installation. No GitHub publication, commit, push, new dependency, app permission, UI, or event behavior change.

## Key handling

- Private credential state created only under `~/.local/share/calendar-signing/`, outside this repository and the Nix store.
- PKCS12 alias `calendar-release`, RSA-4096, SHA256withRSA self-signed certificate, validity 10,000 days, public subject `CN=Calendar release` (no personal identifying fields).
- Directory mode 0700; keystore and randomly generated password file mode 0600. Password never printed, put on the command line, or written to repository files.
- Only independently exported **public certificate SHA-256** is committed-candidate material: `app/release-certificate.sha256`.
- Public certificate fingerprint: `2c7e0aee6bf1d8dbebc0e2741e7a5578c88700724e4dfb6ea739a4e1f04360b4`.
- No external backup has been performed or verified. Owner must securely back up the entire credential directory and never upload it as a GitHub asset.

## Changed source/configuration

- `app/build.gradle.kts`: separate release signing configuration; external credential directory override; explicit packaging guard for missing credentials; `testReleaseArtifact` task. Debug identity/signature remains unchanged.
- `tools/release-artifact.test.mjs`: three reproducible SDK-based artifact tests using Node built-ins only.
- `app/release-certificate.sha256`: public signature pin, safe to publish.
- `.gitignore`: additional private-material filename exclusions.
- README / AGENTS: signed build, backup, publication, signature separation, and actual Git repository state.

## Verification performed

Artifact tests were written first and initially failed because no signed APK/public pin existed. Final checks:

```sh
./gradlew :app:testDebugUnitTest :app:testAcceptanceGenerator :app:lintDebug :app:lintRelease :app:testReleaseArtifact
```

Run in the existing Android Nix development environment:
- 17 JVM unit tests PASS.
- 4 acceptance-generator tests PASS.
- 3 release artifact tests PASS: signature matches the independently exported certificate pin; release is non-debuggable with unchanged application ID; debug keeps a distinct development certificate.
- Debug and release compilation / APK packaging PASS.
- Debug/release lint: 0 errors, 8 existing SDK/library-update warnings each.
- `apksigner verify --verbose --print-certs`: valid APK Signature Scheme v2, one RSA-4096 signer matching the pin.
- Manifest: application ID `app.local.calendar`, versionName `0.1.0`, versionCode `1`; release non-debuggable; only the existing app-scoped signature permission, no network/calendar permission.
- Original debug certificate compared before/after configuration: unchanged.

A negative credential probe exposed AGP's omission of signing tasks when signing configuration is incomplete. Added an explicit `validateReleaseCredentials` packaging dependency, then verified `CALENDAR_SIGNING_DIR=/tmp/calendar-signing-missing ./gradlew :app:assembleRelease` fails with the intended missing-credentials error and produces no unsigned APK. There is no silent unsigned/debug fallback.

Credential hygiene audit checked Git-visible files for the generated password without printing its value and verified no private-material filename is tracked. `.p12` and `.password` ignore rules checked. Do not include private keys/passwords in any subsequent commit, log, chat, release asset, or Nix expression.

The 41 previously passing UI acceptance cases were not rerun for this packaging-only change; application source/behavior is unchanged. The signed release was freshly installed and launched offline on the isolated API-35 calendar-ui-test AVD; MainActivity was RESUMED and the month UI manually inspected (`release-smoke.png`). Only that fresh fixture installation was removed during cleanup, then the test emulator stopped, preventing a release/debug signature conflict in future tests. The owner's emulator was not uninstalled or cleared. No physical GrapheneOS execution, Obtainium installation, GitHub publication, or external QA is claimed.

## Deliverables and emulator

- Signed APK: `app/build/outputs/apk/release/app-release.apk`.
- Public checksum: `app/build/outputs/apk/release/app-release.apk.sha256`.
- Do not attach credential files to a GitHub Release. Publish only the APK and optional checksum after owner commit/push/tag steps.
- Verified shared AVD android-dev / emulator-5554; rebuilt debug APK updated in place and MainActivity launched with status ok. Emulator remains debug-signed to preserve its installed app/data, running with Calendar open. No uninstall, data clear, signing rotation, or AVD wipe.
- Release and debug signatures differ. A release cannot directly replace an existing debug installation with the same app ID; do not erase data as an automatic workaround.

Next: owner backs up credentials, commits/pushes public configuration changes, publishes the APK as a GitHub Release asset, and tests it via Obtainium on GrapheneOS.
