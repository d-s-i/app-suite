Feature: EVENTS-001-F01 Create and inspect local events
  Events are created only through +, with a required title and optional description.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone date is 2027-01-20
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario: EVENTS-001-S39 Timed events require an explicitly chosen hour
    Given the owner opened event creation with + for Dentist on 2027-02-06
    When the owner switches to timed mode without confirming an hour
    Then no implicit timed hour is adopted
    And a timed event cannot be saved until an hour is chosen
    When the owner abandons the editor
    Then no event was created on 2027-02-06

  Scenario Outline: EVENTS-001-S01 Open the form with contextual defaults
    Given <view> displays the period containing <view_date>
    When the owner opens event creation with +
    Then the form date is <default_date>
    And the title and description are empty
    And all-day and no repetition are selected
    And no duration, event end-time or event end-date adjustment is offered

    Examples:
      | view | view_date | default_date |
      | Day | 2027-02-06 | 2027-02-06 |
      | Month | 2027-01-01 | 2027-01-20 |
      | Month | 2027-02-01 | 2027-02-01 |
      | Week | 2027-01-18 | 2027-01-20 |
      | Week | 2027-02-01 | 2027-02-01 |

  Scenario Outline: EVENTS-001-S02 Save an all-day event and return to the originating view
    Given <view> displays the period containing 2027-01-20
    When the owner creates Picnic with + as all-day on 2027-02-06 with description Bring papers
    Then <view> displays the period containing 2027-02-06
    And Picnic is represented on 2027-02-06
    When the owner opens Picnic details
    Then the details show title Picnic and description Bring papers
    And the details show one all-day date 2027-02-06 without a requested hour

    Examples:
      | view |
      | Month |
      | Week |
      | Day |

  Scenario: EVENTS-001-S03 Create a timed event offline with an automatic hour
    Given the phone has no network connection and no Google Play services
    And Day displays the period containing 2027-02-06
    When the owner creates Dentist with + at 09:30 on 2027-02-06 without a description
    Then Day displays the period containing 2027-02-06
    And Dentist is represented at 09:30 on 2027-02-06
    When the owner opens Dentist details
    Then the details show an empty description
    And the computed interval is 2027-02-06 09:30 to 2027-02-06 10:30 in UTC
    And the computed duration is 60 elapsed minutes
    And no sign-in, network or shared Android calendar is required

  Scenario Outline: EVENTS-001-S04 Reject missing mandatory event input
    Given the owner opened event creation with + for 2027-02-06
    When the owner attempts to save a timed event at 09:30 with title input <title_input>
    Then the editor remains open and identifies the title as required
    When the owner abandons the editor
    Then no event was created on 2027-02-06

    Examples:
      | title_input |
      | empty |
      | three spaces |

  Scenario Outline: EVENTS-001-S05 Abandoning a draft restores the original calendar period
    Given Month displays the period containing 2027-01-20
    And the owner opened event creation with +
    And the draft is Draft at 10:00 on 2027-02-06 with description Unsaved
    When the owner abandons the editor using <action>
    Then Month displays the period containing 2027-01-20
    When the owner navigates to 2027-02-06
    Then no event named Draft exists

    Examples:
      | action |
      | Cancel |
      | system Back |

  Scenario Outline: EVENTS-001-S06 Empty time slots do not open event creation
    Given <view> displays the period containing 2027-02-06
    When the owner taps an empty time slot at 09:30 on 2027-02-06
    Then <view> remains active with no event editor open
    And no event was created on 2027-02-06

    Examples:
      | view |
      | Day |
      | Week |
