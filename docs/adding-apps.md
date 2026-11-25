# Adding an independent Android app

This is a developer guide for a future product, not a request to create a second
app now. Calendar builds independently as `:calendar` in `apps/calendar-app/`.
Each app owns its source, tests, tools, README, app instructions and public
signing pin under `apps/`; shared presentation lives under `shared/`. Register
logical module names explicitly rather than inferring them from folder names.

1. **Register the module.** Create `apps/<name>/build.gradle.kts` and sources,
   then add `include(":<name>")` and
   `project(":<name>").projectDir = file("apps/<name>")` in root
   [`settings.gradle.kts`](../settings.gradle.kts). Use the application, Kotlin
   Android and Compose plugins already pinned in the root build. Reuse the
   approved JDK/SDK and dependency versions; ask before adding external dependencies.
   Do not copy Calendar's task-specific acceptance adapter into a new app.
2. **Assign identity.** Choose an independently owned `namespace` and a unique
   `applicationId`, different from `app.local.calendar`. Add that app's own
   manifest/activity/resources and launcher label/icon. Never change Calendar's
   identity to accommodate it. Start with minSdk 26 and compile/target 35 only
   if appropriate for the new product; these are Calendar's build settings,
   not confirmed support requirements for every future app.
3. **Consume design.** Add `implementation(project(":design"))` and import
   `app.suite.design.SuiteColors` as described in [the design guide](design.md).
   Keep app-specific navigation, dates and domain logic in the app; don't copy
   the palette or make the shared module depend on an app.
4. **Keep data and permissions separate.** Use app-private storage under the
   new application identity. Do not use shared user IDs, export data providers
   or automatically share Calendar data. Declare only permissions approved for
   the new app in its manifest, not the shared design. Calendar retains no
   network/calendar permissions, Play services or event storage in this task.
   Decide backup/export and exported Android components per app.
5. **Version independently.** Give the app its own `versionCode` and
   `versionName`. Increment its code above every installed release it must
   replace; a Calendar release does not require changing the other app's version.
6. **Sign independently.** Obtain owner approval and provide that app's own
   external signing directory, filenames/alias and public certificate pin.
   Never reuse Calendar's signing directory, pin or release configuration by
   copying it. Keep credentials outside Git and the Nix store, verify secure
   backups, fail release builds if credentials are missing, and verify the
   signed APK identity/certificate and separation from debug. Do not regenerate
   Calendar's key or uninstall an app to bypass a signature failure.
7. **Build and test by module.** Once `<name>` is registered/configured, run
   `./gradlew :<name>:testDebugUnitTest :<name>:lintDebug :<name>:assembleDebug`.
   Keep its own tooling in `apps/<name>/tools/`, not at root. Its debug APK will
   normally be `apps/<name>/build/outputs/apk/debug/<name>-debug.apk` when the
   logical Gradle name is `<name>`; confirm the actual AGP output and document it
   in that app's guide. Calendar illustrates explicit mapping: folder
   `calendar-app`, module `:calendar`, APK basename `calendar`. A configured signed release uses
   `./gradlew :<name>:assembleRelease` and normally
   `apps/<name>/build/outputs/apk/release/<name>-release.apk`. These are templates,
   not executable commands for an existing second product. Add app-specific
   tests/acceptance only as required by its approved task.
8. **Deliver independently.** Document that app's exact commands, APK identity,
   output paths, certificate pin and release version. Review the artifact before
   the owner commits/pushes and publishes. Future multi-app release asset/tag and
   Obtainium organization is not decided here; never assume all APKs belong to
   one update stream. Calendar publication instructions are in
   [the publication guide](publishing-calendar.md).

## Checks after a shared-design change

For Calendar, from the repository root in the Android environment:

```sh
./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:generateAcceptance
ANDROID_SERIAL=<dedicated-test-serial> ./gradlew :calendar:acceptanceTest
./gradlew :design:lintDebug :calendar:lintDebug :calendar:assembleDebug :calendar:assembleRelease :calendar:testReleaseArtifact
```

Use the offline test-device setup in the [Calendar guide](../apps/calendar-app/README.md#development-gherkin-acceptance),
not the owner's emulator for destructive tests or clock/density/network fixtures.
Run the equivalent documented checks for every other real consumer once it exists.
Then install Calendar debug in place on the owner's verified persistent
`android-dev`, preserving data/signature and leaving it open. Shared library AARs
are build inputs, not standalone installable apps and do not carry app signing
credentials.
