Feature: MONOREPO-001-F02 Document reuse of the Calendar design for future apps
  The developer can find the shared design and the steps for adding an independent application.
  No second application is required for this delivery.

  Scenario: MONOREPO-001-S04 Find the reusable design through the developer guide
    Given the developer has the app-suite checkout
    When the developer follows the design reuse guide from the repository README
    Then each reference to the shared design resolves to a versioned definition or documented public usage example
    And the guide identifies the existing Calendar visual elements that are shared and how a future application uses them without copying their definitions
    And Calendar is documented as a consumer of that shared design
    And the guide distinguishes shared presentation from Calendar-specific date calculations and navigation

  Scenario: MONOREPO-001-S05 Find the steps for adding and delivering an independent application
    Given the developer has the app-suite checkout
    When the developer follows the application addition guide from the repository README
    Then the guide describes how to assign a separate Android application identity and reuse the shared design
    And the guide describes how to build and test one application and locate its APK
    And the guide describes how to keep application versions, private data, permissions and signing credentials independent
    And the guide identifies the Calendar checks to rerun when shared design changes
    And the guide does not require creating a second product to build Calendar
