Feature: EVENTS-001-F04 Select free monthly recurrence patterns
  Missing dates clamp within their target month without changing the chosen rule.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario Outline: EVENTS-001-S16 Repeat a freely chosen numbered day without date drift
    When the owner creates all-day Course from <lower_bound> every <interval> months on day <month_day> ending after 3 occurrences
    Then the calendar returns to the originating view on the first occurrence date <first_date>
    And Course is all-day on exactly <first_date>, <second_date> and <third_date>
    And its monthly rule still requests day <month_day>

    Examples:
      | interval | month_day | lower_bound | first_date | second_date | third_date |
      | 1 | 31 | 2027-01-30 | 2027-01-31 | 2027-02-28 | 2027-03-31 |
      | 1 | 31 | 2028-01-01 | 2028-01-31 | 2028-02-29 | 2028-03-31 |
      | 3 | 15 | 2027-01-20 | 2027-02-15 | 2027-05-15 | 2027-08-15 |
      | 2 | 30 | 2027-01-31 | 2027-02-28 | 2027-04-30 | 2027-06-30 |
      | 1 | 5 | 2027-01-20 | 2027-02-05 | 2027-03-05 | 2027-04-05 |

  Scenario Outline: EVENTS-001-S17 Repeat a freely chosen ordinal weekday
    When the owner creates all-day Course from <lower_bound> every <interval> months on the <ordinal> <weekday> ending after 3 occurrences
    Then Course is all-day on exactly <first_date>, <second_date> and <third_date>
    And its monthly rule still requests the <ordinal> <weekday>

    Examples:
      | interval | ordinal | weekday | lower_bound | first_date | second_date | third_date |
      | 1 | first | Monday | 2027-01-20 | 2027-02-01 | 2027-03-01 | 2027-04-05 |
      | 2 | first | Monday | 2027-01-20 | 2027-02-01 | 2027-04-05 | 2027-06-07 |
      | 1 | second | Tuesday | 2027-01-01 | 2027-01-12 | 2027-02-09 | 2027-03-09 |
      | 1 | third | Thursday | 2027-01-01 | 2027-01-21 | 2027-02-18 | 2027-03-18 |
      | 1 | fourth | Monday | 2027-02-01 | 2027-02-22 | 2027-03-22 | 2027-04-26 |
      | 1 | fifth | Monday | 2027-02-01 | 2027-02-22 | 2027-03-29 | 2027-04-26 |
      | 1 | last | Friday | 2027-01-01 | 2027-01-29 | 2027-02-26 | 2027-03-26 |

  Scenario: EVENTS-001-S18 Reject a monthly window with no matching occurrence
    Given the owner opened event creation with + for Course from 2027-01-20
    When the owner selects every 2 months on the first Monday ending on 2027-01-31 and attempts to save
    Then the form identifies that no occurrence fits before the end date
    When the owner abandons the editor
    Then no event named Course exists

  Scenario: EVENTS-001-S19 Include a clamped monthly occurrence on the end date
    When the owner creates all-day Course from 2027-01-30 every 1 months on day 31 ending on 2027-04-30
    Then Course is all-day on exactly 2027-01-31, 2027-02-28, 2027-03-31 and 2027-04-30
    And Course has no occurrence on 2027-05-31
