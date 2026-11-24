Feature: CAL-001-F02 Empty week and day time grids
  The owner can inspect time in the same calendar layout family as the pinned Proton reference.

  Background:
    Given the calendar contains no events
    And the device date is 2028-02-15 in time zone UTC
    And the device locale is English United States

  Scenario Outline: CAL-001-S06 Switch from Month to an empty timed view
    Given Month displays February 2028 with today selected
    When the owner opens the navigation drawer and chooses <view>
    Then <view> is the active view
    And the drawer closes
    And the time grid has <column_count> date columns from <first_date> through <last_date>
    And hour labels appear to the left of horizontal hour separators
    And the default distance between successive hour separators is 60 density-independent pixels
    And no event blocks appear

    Examples:
      | view | column_count | first_date | last_date |
      | Week | 7 | 2028-02-14 | 2028-02-20 |
      | Day | 1 | 2028-02-15 | 2028-02-15 |

  Scenario Outline: CAL-001-S07 Scroll the time grid without changing dates
    Given <view> displays the date range <first_date> through <last_date>
    And the device uses 24-hour time
    When the owner scrolls vertically to the start of the day
    Then the hour label 01:00 is visible
    When the owner scrolls vertically to the end of the day
    Then the hour label 23:00 is visible
    And <view> displays the date range <first_date> through <last_date>

    Examples:
      | view | first_date | last_date |
      | Week | 2028-02-14 | 2028-02-20 |
      | Day | 2028-02-15 | 2028-02-15 |

  Scenario: CAL-001-S08 Open a day from a week header
    Given Week displays 2028-02-14 through 2028-02-20
    When the owner taps the date header for 2028-02-18
    Then Day is the active view
    And its single date column is 2028-02-18

  Scenario Outline: CAL-001-S17 Timed views have no month expansion control
    Given Month displays February 2028 with today selected
    When the owner opens the navigation drawer and chooses <view>
    Then <view> is the active view
    And no month expansion control or dropdown picker is displayed
    And the time grid remains visible

    Examples:
      | view |
      | Week |
      | Day |

  Scenario Outline: CAL-001-S10 Follow the device time format
    Given the device uses <time_format> time
    And Day displays 2028-02-15
    When the owner scrolls to the hour thirteen hours after midnight
    Then that hour label is <hour_label>

    Examples:
      | time_format | hour_label |
      | 24-hour | 13:00 |
      | 12-hour | 1 PM |

  Scenario: CAL-001-S19 Day labels occupy only the left time column
    Given Month displays February 2028 with today selected
    When the owner opens the navigation drawer and chooses Day
    Then the Day weekday and date are centered over the left time column
    And the Day weekday uses 12sp text and its date uses 16sp text
    And no weekday strip is displayed above the Day header
    And the toolbar title is February

  Scenario Outline: CAL-001-S20 Day hour labels follow the reference metrics
    Given the device uses <time_format> time
    And Day displays 2028-02-15
    When the owner scrolls to the hour thirteen hours after midnight
    Then that hour label is <hour_label>
    And Day hour labels use 12sp text centered vertically on hour lines
    And the Day time column fits the widest hour label with 10dp padding on each side

    Examples:
      | time_format | hour_label |
      | 24-hour | 13:00 |
      | 12-hour | 1 PM |

  Scenario: CAL-001-S21 Day initially shows one hour before the current time
    Given Month displays February 2028 with today selected
    When the owner opens the navigation drawer and chooses Day
    Then the Day time grid starts one hour before the current local time within its scroll limits

  Scenario Outline: CAL-001-S22 Header dividers continue the time grid boundaries
    Given Month displays February 2028 with today selected
    When the owner opens the navigation drawer and chooses <view>
    Then the time-column separator continues through the full date header
    And the date-header bottom divider spans the full calendar width
    When the owner scrolls vertically to the start of the day
    Then the time-column separator continues through the full date header
    And the date-header bottom divider spans the full calendar width
    When the owner scrolls vertically to the end of the day
    Then the time-column separator continues through the full date header
    And the date-header bottom divider spans the full calendar width

    Examples:
      | view |
      | Day |
      | Week |
