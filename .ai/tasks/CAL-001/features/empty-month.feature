Feature: CAL-001-F01 Empty month calendar
  The owner can inspect a complete month without events or an online account.

  Background:
    Given the calendar contains no events
    And the device date is 2028-02-15 in time zone UTC
    And the device locale is English United States

  Scenario: CAL-001-S01 First launch opens the current month offline
    Given the app has not been launched before
    And the device has no network connection and no Google Play services
    When the owner launches the app
    Then Month is the active view
    And the toolbar title is February
    And the calendar has 7 equal-width columns and 6 equal-height rows
    And weekday headers run from Monday through Sunday
    And the 42 date cells run consecutively from 2028-01-31 through 2028-03-12
    And no event blocks appear
    And no sign-in or network requirement prevents calendar use

  Scenario: CAL-001-S02 Month styling follows the pinned empty-calendar reference
    When the owner opens Month for February 2028
    Then weekday headers occupy 24 density-independent pixels in height
    And month grid dividing lines are 1 density-independent pixel wide
    And day numbers use 12 scale-independent pixel text centered horizontally in each cell
    And day-number baselines are 18 density-independent pixels below the top of each row
    And adjacent-month date numbers are muted relative to February date numbers
    And February 15 has an accent-colored rounded outline 21 density-independent pixels wide and 20 high
    And the outline has a radius of 5 density-independent pixels and a stroke of 1
    And no other date cell has the today outline

  Scenario Outline: CAL-001-S03 Navigate month boundaries without missing or inventing dates
    Given Month displays <starting_month>
    When the owner swipes the month grid <direction> once
    Then Month displays <expected_month>
    And the 42 date cells run consecutively from <first_date> through <last_date>
    And <boundary_date> appears exactly once

    Examples:
      | starting_month | direction | expected_month | first_date | last_date | boundary_date |
      | January 2028 | left | February 2028 | 2028-01-31 | 2028-03-12 | 2028-02-29 |
      | March 2027 | right | February 2027 | 2027-02-01 | 2027-03-14 | 2027-02-28 |
      | December 2027 | left | January 2028 | 2027-12-27 | 2028-02-06 | 2028-01-01 |
      | January 2028 | right | December 2027 | 2027-11-29 | 2028-01-09 | 2027-12-31 |

  Scenario Outline: CAL-001-S04 Select an empty month date
    Given Month displays February 2028
    When the owner taps the cell for <selected_date>
    Then Day is the active view
    And its single date column is <selected_date>
    And no event blocks appear

    Examples:
      | selected_date |
      | 2028-02-20 |
      | 2028-01-31 |
      | 2028-03-01 |

  Scenario Outline: CAL-001-S05 Format the month title using the device locale
    Given the device locale is <locale>
    When the owner opens Month for <month_date>
    Then the toolbar title is <title>
    And weekday headers run from Monday through Sunday

    Examples:
      | locale | month_date | title |
      | English United States | 2028-02-01 | February |
      | French France | 2028-02-01 | février |
      | English United States | 2027-12-01 | December 2027 |
      | French France | 2027-12-01 | décembre 2027 |
