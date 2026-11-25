Feature: EVENTS-001-F02 Find events in all calendar views
  Rendering must not lose events or confuse all-day dates with timed appointments.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario Outline: EVENTS-001-S07 Find all-day and timed events in each calendar view
    Given Picnic is all-day on 2027-02-06 with description Outside
    And Dentist starts at 09:30 on 2027-02-06 with description Checkup
    When the owner opens <view> for 2027-02-06
    Then Picnic and Dentist are represented on 2027-02-06
    And neither event is represented on 2027-02-05 or 2027-02-07
    When the owner opens Picnic details
    Then the details show one all-day date 2027-02-06 without a requested hour
    When the owner opens Dentist details
    Then the computed interval is 2027-02-06 09:30 to 2027-02-06 10:30 in UTC

    Examples:
      | view |
      | Month |
      | Week |
      | Day |

  Scenario Outline: EVENTS-001-S08 Keep simultaneous events independently accessible
    Given Alpha starts at 09:30 on 2027-02-06 with description First
    And Beta starts at 09:30 on 2027-02-06 with description Second
    When the owner opens <view> for 2027-02-06
    Then both events can be opened from the representation at 09:30
    And Alpha details show description First
    And Beta details show description Second

    Examples:
      | view |
      | Day |
      | Week |

  Scenario: EVENTS-001-S09 Account for every event in a populated month cell
    Given the owner created the 18 all-day events E01 through E18 on 2027-02-06
    When the owner opens Month for 2027-02-06
    Then the date cell shows at least one of those event titles
    And its visible titles and any +N overflow account for exactly 18 distinct events
    And an overflow control is shown whenever any of those titles are hidden
    When the owner opens the full day using overflow if present or the date cell otherwise
    Then Day displays 2027-02-06
    And every event E01 through E18 can be opened exactly once from that day
