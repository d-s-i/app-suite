Feature: MONOREPO-001-F01 Preserve Calendar when moving to app-suite
  The owner can build and use Calendar from app-suite without replacing its Android identity.

  Scenario: MONOREPO-001-S01 Build and verify Calendar from the migrated repository
    Given the app-suite checkout has the documented Calendar build prerequisites
    And the owner's existing Calendar release credentials are available outside the checkout
    When the owner runs the documented Calendar unit tests, acceptance generator tests, UI acceptance, lint, debug build, release build and release artifact verification
    Then each command exits successfully
    And the documented output paths contain a debug APK and a signed release APK for app.local.calendar
    And release artifact verification confirms the existing release certificate and distinct debug certificate

  Scenario: MONOREPO-001-S02 Continue using the installed Calendar after migration
    Given the dedicated offline Android test device has the pre-migration debug Calendar installed without Google Play services
    And the device date is 2028-02-15 in time zone UTC with English United States locale and 24-hour time
    When the operator updates Calendar with the migrated debug APK without uninstalling it or clearing data
    And the operator launches Calendar, opens February 20 from Month, returns to Month using system Back, and selects Week
    Then the update succeeds for app.local.calendar without a second Calendar installation
    And February contains 7 columns and 6 rows with 42 consecutive dates from 2028-01-31 through 2028-03-12
    And selecting February 20 opens Day for February 20
    And system Back from Day returns to February in Month
    And Week displays February 14 through February 20 without an account or network requirement

  Scenario: MONOREPO-001-S03 Refuse a release build without the existing signing credentials
    Given the app-suite checkout has the documented Calendar build prerequisites
    And the release signing location selected for this invocation contains no credentials
    When the owner runs the documented Calendar release build
    Then the command exits unsuccessfully with a missing signing credentials explanation
    And no newly built Calendar release APK is offered as ready for publication
    And no replacement signing key is created and no debug signing fallback is used
