Feature: EVENTS-001-F07 Keep event time meaning across zones and clock changes
  Timed events retain their origin zone; all-day events retain dates.

  Background:
    Given the calendar contains no events
    And the phone time zone is Europe/Paris
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario Outline: EVENTS-001-S32 Convert display without changing the editing zone
    Given Dentist starts at 09:00 on 2027-01-15 with description Checkup
    When the owner changes the phone time zone to <display_zone>
    Then Dentist is represented at <display_time> on <display_date>
    When the owner opens Dentist for editing
    Then the editing date and hour are 2027-01-15 and 09:00
    And the editing zone is identified as Europe/Paris
    When the owner abandons the editor
    Then Dentist is represented at <display_time> on <display_date>

    Examples:
      | display_zone | display_date | display_time |
      | America/New_York | 2027-01-15 | 03:00 |
      | Asia/Tokyo | 2027-01-15 | 17:00 |
      | Pacific/Honolulu | 2027-01-14 | 22:00 |

  Scenario: EVENTS-001-S33 Keep the origin clock time when DST offsets change
    Given Course starts at 09:00 on 2027-03-21 every 1 weeks ending after 3 occurrences
    When the owner changes the phone time zone to America/New_York
    Then Course is represented at 04:00 on 2027-03-21
    And Course is represented at 03:00 on 2027-03-28 and 2027-04-04
    When the owner inspects each occurrence for editing without saving
    Then each occurrence still requests 09:00 in Europe/Paris
    And each computed duration is 60 elapsed minutes

  Scenario Outline: EVENTS-001-S34 All-day dates do not shift with the display zone
    Given Picnic is all-day on 2027-01-15 with description Outside
    When the owner changes the phone time zone to <display_zone>
    Then Picnic is all-day on 2027-01-15
    And Picnic is not represented on 2027-01-14 or 2027-01-16

    Examples:
      | display_zone |
      | Pacific/Honolulu |
      | Pacific/Kiritimati |

  Scenario: EVENTS-001-S35 Adding an hour to an all-day event captures the current phone zone
    Given Picnic is all-day on 2027-01-15 with description Outside
    And the owner changes the phone time zone to Asia/Tokyo
    When the owner edits Picnic to timed at 09:00 on 2027-01-15 and saves
    When the owner opens Picnic for editing
    Then the editing zone is identified as Asia/Tokyo
    When the owner abandons the editor
    And the owner changes the phone time zone to UTC
    Then Picnic is represented at 00:00 on 2027-01-15
    And exactly 1 event named Picnic exists

  Scenario: EVENTS-001-S36 Shift a nonexistent hour without drifting the recurrence clock
    Given the owner opened event creation with + for Course from 2027-03-28
    When the owner selects daily recurrence at 02:30 ending after 2 occurrences
    Then the editor shows that the first start is adjusted to 03:30 in Europe/Paris
    And the requested recurrence clock remains 02:30
    When the owner saves Course
    Then the computed interval is 2027-03-28 03:30 to 2027-03-28 04:30 in Europe/Paris
    And the next occurrence is 2027-03-29 02:30 to 2027-03-29 03:30 in Europe/Paris
    When the owner changes the phone time zone to UTC
    Then Course starts at 01:30 on 2027-03-28 and at 00:30 on 2027-03-29
    And exactly 2 Course starts exist from 2027-03-28 through 2027-03-30

  Scenario: EVENTS-001-S37 Choose the first repeated local hour only once
    When the owner creates Night with + at 02:30 on 2026-10-25 without a description
    Then exactly 1 Night start exists on 2026-10-25
    When the owner changes the phone time zone to UTC
    Then the computed interval is 2026-10-25 00:30 to 2026-10-25 01:30 in UTC
    And the computed duration is 60 elapsed minutes
    And no second Night start exists at 01:30 on 2026-10-25 in UTC

  Scenario Outline: EVENTS-001-S38 Automatic midnight overlap is one occurrence
    Given <view> displays the period containing 2027-01-15
    And the phone time zone is UTC
    When the owner creates Night with + at <start_time> on 2027-01-15 without a description
    Then the computed interval is 2027-01-15 <start_time> to <end_date> <end_time> in UTC
    And Night is represented on 2027-01-15
    And its representation on 2027-01-16 is <next_day_presence>
    And opening any visible fragment identifies the same start 2027-01-15 <start_time>
    When the owner deletes Night once from its starting date and confirms
    Then Night is absent from both 2027-01-15 and 2027-01-16

    Examples:
      | view | start_time | end_date | end_time | next_day_presence |
      | Month | 22:59 | 2027-01-15 | 23:59 | absent |
      | Month | 23:00 | 2027-01-16 | 00:00 | absent |
      | Month | 23:30 | 2027-01-16 | 00:30 | present |
      | Week | 22:59 | 2027-01-15 | 23:59 | absent |
      | Week | 23:00 | 2027-01-16 | 00:00 | absent |
      | Week | 23:30 | 2027-01-16 | 00:30 | present |
      | Day | 22:59 | 2027-01-15 | 23:59 | absent |
      | Day | 23:00 | 2027-01-16 | 00:00 | absent |
      | Day | 23:30 | 2027-01-16 | 00:30 | present |
