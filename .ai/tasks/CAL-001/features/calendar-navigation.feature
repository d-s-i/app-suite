Feature: CAL-001-F03 Navigate empty calendar periods locally
  The owner can move between dates and return to today without an online service.

  Background:
    Given the calendar contains no events
    And the device date is 2028-02-15 in time zone UTC
    And the device locale is English United States
    And the device has no network connection and no Google Play services

  Scenario Outline: CAL-001-S11 Navigate timed periods across date boundaries
    Given <view> displays the date range <initial_first> through <initial_last>
    When the owner swipes the time grid <direction> by one complete period
    Then <view> displays the date range <expected_first> through <expected_last>
    And no event blocks appear

    Examples:
      | view | initial_first | initial_last | direction | expected_first | expected_last |
      | Week | 2028-02-21 | 2028-02-27 | left | 2028-02-28 | 2028-03-05 |
      | Week | 2028-01-03 | 2028-01-09 | right | 2027-12-27 | 2028-01-02 |
      | Day | 2028-02-29 | 2028-02-29 | left | 2028-03-01 | 2028-03-01 |
      | Day | 2028-01-01 | 2028-01-01 | right | 2027-12-31 | 2027-12-31 |

  Scenario Outline: CAL-001-S12 Return to today without changing the active view
    Given <view> displays a period in December 2027
    When the owner activates the Today control
    Then <view> remains the active view
    And its displayed date range is <first_date> through <last_date>
    And the Today control displays the number 15

    Examples:
      | view | first_date | last_date |
      | Month | 2028-01-31 | 2028-03-12 |
      | Week | 2028-02-14 | 2028-02-20 |
      | Day | 2028-02-15 | 2028-02-15 |

  Scenario: CAL-001-S14 Return from Day to Month using the view selector
    Given Day displays 2028-03-01
    When the owner opens the navigation drawer and chooses Month
    Then Month is the active view
    And the drawer closes
    And the toolbar title is March
    And its 42 date cells run consecutively from 2028-02-28 through 2028-04-09

  Scenario Outline: CAL-001-S15 Show the current time on today's timed grid
    Given <view> displays a period containing 2028-02-15
    When the owner scrolls the time grid to the current device time
    Then a current-time line and dot appear in the February 15 column at the device's current local time

    Examples:
      | view |
      | Week |
      | Day |

  Scenario Outline: CAL-001-S16 System Back from Day opens the containing month
    Given Day displays <selected_date>
    When the owner presses system Back
    Then Month is the active view
    And the toolbar title is <month_title>
    And the 42 date cells run consecutively from <first_date> through <last_date>

    Examples:
      | selected_date | month_title | first_date | last_date |
      | 2028-02-20 | February | 2028-01-31 | 2028-03-12 |
      | 2028-03-01 | March | 2028-02-28 | 2028-04-09 |

  Scenario: CAL-001-S18 System Back closes the drawer before leaving Day
    Given Day displays 2028-02-20
    And the navigation drawer is open
    When the owner presses system Back
    Then the drawer closes
    And Day remains the active view
    And its single date column is 2028-02-20
    When the owner presses system Back
    Then Month is the active view
    And the toolbar title is February
