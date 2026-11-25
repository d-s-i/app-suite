Feature: MONOREPO-001-F03 Publish app-suite and its Calendar release on GitHub
  The owner publishes the migrated project in a new repository and personally verifies the release APK.

  Scenario: MONOREPO-001-S06 Publish the migrated code without renaming the original repository
    Given the owner can publish to a new GitHub repository d-s-i/app-suite
    And the existing repository d-s-i/calendar is available
    When the owner publishes the reviewed app-suite code to d-s-i/app-suite
    Then d-s-i/app-suite contains the migrated Calendar source, shared design and developer documentation
    And the pre-migration Git history and existing task identifiers remain available in the published project
    And d-s-i/calendar remains available at its existing address without deletion or renaming
    And no private signing credentials are included in the published files or history

  Scenario: MONOREPO-001-S07 Owner verifies the Calendar APK in the new repository release
    Given the reviewed app-suite code is published to d-s-i/app-suite
    And a Calendar release APK has passed verification against the existing release certificate
    And its versionCode is greater than the Calendar release versions it is intended to replace
    When the owner publishes a Calendar release in d-s-i/app-suite and checks its attached APK
    Then the release attachment is the verified Calendar release APK for app.local.calendar
    And the attachment can be downloaded by the owner from that release
    And the delivery record contains the release URL and the owner's verification result
