Feature: EVENTS-001-F03 Repeat events by calendar days or weeks
  Recurrence repeats events, not merely their notifications.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario Outline: EVENTS-001-S10 Count the first occurrence in a day or week series
    When the owner creates Course at 09:00 starting <start_date> every <interval> <unit> ending after 3 occurrences
    Then Course starts at 09:00 on exactly <start_date>, <second_date> and <third_date>
    And Course has no occurrence on <next_date>
    And each timed occurrence lasts 60 elapsed minutes

    Examples:
      | unit | interval | start_date | second_date | third_date | next_date |
      | days | 3 | 2027-01-30 | 2027-02-02 | 2027-02-05 | 2027-02-08 |
      | weeks | 2 | 2027-12-27 | 2028-01-10 | 2028-01-24 | 2028-02-07 |
      | days | 1 | 2028-02-28 | 2028-02-29 | 2028-03-01 | 2028-03-02 |

  Scenario Outline: EVENTS-001-S11 Include the recurrence end date
    When the owner creates Course at 09:00 starting <start_date> every <interval> <unit> ending on <end_date>
    Then exactly 3 Course occurrences start from <start_date> through <end_date>
    And Course starts at 09:00 on <end_date>
    And Course has no occurrence on <next_date>

    Examples:
      | unit | interval | start_date | end_date | next_date |
      | days | 3 | 2027-01-30 | 2027-02-05 | 2027-02-08 |
      | weeks | 2 | 2027-12-27 | 2028-01-24 | 2028-02-07 |

  Scenario: EVENTS-001-S12 A never-ending series remains available in a later window
    When the owner creates Course at 09:00 starting 2027-01-01 every 1 days ending never
    And the owner navigates to 2030-01-01 through 2030-01-03
    Then Course starts at 09:00 on each of those 3 dates
    And its recurrence details still show no end

  Scenario Outline: EVENTS-001-S13 Reject a nonpositive or noninteger recurrence interval
    Given the owner opened event creation with + for Course on 2027-02-06
    When the owner attempts to configure daily recurrence with interval input <interval_input>
    Then recurrence interval input <interval_input> is not accepted
    When the owner abandons the editor
    Then no event named Course exists

    Examples:
      | interval_input |
      | 0 |
      | -1 |
      | empty |
      | 1.5 |

  Scenario Outline: EVENTS-001-S14 Reject a nonpositive or noninteger occurrence count
    Given the owner opened event creation with + for Course on 2027-02-06
    When the owner attempts to end daily recurrence after count input <count_input>
    Then occurrence count input <count_input> is not accepted
    When the owner abandons the editor
    Then no event named Course exists

    Examples:
      | count_input |
      | 0 |
      | -1 |
      | empty |
      | 1.5 |

  Scenario: EVENTS-001-S15 Reject an end date before the first occurrence
    Given the owner opened event creation with + for Course on 2027-02-10
    When the owner configures daily recurrence ending on 2027-02-09 and attempts to save
    Then the end date is identified as invalid and the editor remains open
    When the owner abandons the editor
    Then no event named Course exists
