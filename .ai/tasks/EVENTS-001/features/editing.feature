Feature: EVENTS-001-F05 Edit events with explicit series scope
  Text edits preserve local overrides; temporal changes reset affected exceptions only with consent.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario: EVENTS-001-S20 Edit a one-time event without creating a duplicate
    Given Dentist starts at 09:30 on 2027-02-06 with description Checkup
    When the owner edits Dentist to Consultation at 11:00 on 2027-02-07 with description Followup and saves
    Then Consultation is represented at 11:00 on 2027-02-07
    And its details show description Followup
    And the computed interval is 2027-02-07 11:00 to 2027-02-07 12:00 in UTC
    And no event named Dentist exists
    And exactly 1 event exists from 2027-02-06 through 2027-02-07

  Scenario Outline: EVENTS-001-S21 Change between all-day and timed modes
    Given Switch is <original_mode> on 2027-02-06
    When the owner changes Switch to <target_mode> and saves, choosing 09:00 if timed
    Then Switch details show <target_mode> on 2027-02-06
    And exactly 1 event named Switch exists
    And no duration, event end-time or event end-date adjustment is offered

    Examples:
      | original_mode | target_mode |
      | all-day | timed |
      | timed | all-day |

  Scenario: EVENTS-001-S22 Move and customize only one recurring occurrence
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    When the owner edits only the occurrence originally on 2027-06-03 to Custom at 11:00 on 2027-06-04 with description Local and saves
    Then no Course occurrence starts on 2027-06-03
    And Custom starts at 11:00 on 2027-06-04 with description Local
    And Course starts at 09:00 on 2027-06-01, 2027-06-02, 2027-06-04 and 2027-06-05
    And no occurrence of the original series starts on 2027-06-06

  Scenario Outline: EVENTS-001-S23 Series text edits preserve individual fields and cancellations
    Given the Course exception fixture is prepared
    When the owner changes the series title to Renamed and description to Shared with scope <scope> from 2027-06-03 and saves
    Then the 2027-06-01 occurrence has title <first_title> and description <first_description> at 09:00
    And the 2027-06-02 occurrence keeps title Custom and description Local at 09:00
    And the 2027-06-03 occurrence inherits title Renamed and description Shared but stays at 11:00
    And the 2027-06-04 occurrence remains deleted
    And the 2027-06-05 occurrence has title Renamed and description Shared at 09:00
    And no occurrence of the original series starts on 2027-06-06

    Examples:
      | scope | first_title | first_description |
      | whole series | Renamed | Shared |
      | this and future | Course | Base |

  Scenario Outline: EVENTS-001-S24 Confirm before resetting temporal exceptions
    Given the Course exception fixture is prepared
    When the owner changes the series time to 10:00 with scope <scope> from 2027-06-03 and attempts to save
    Then a warning identifies that affected individual modifications will reset and deleted occurrences may return
    When the owner confirms the reset
    Then the 2027-06-01 occurrence starts at <first_time>
    And the 2027-06-02 occurrence has title <second_title> at <second_time>
    And Course starts at 10:00 on 2027-06-03, 2027-06-04 and 2027-06-05
    And no affected occurrence remains at 11:00
    And no occurrence of the original series starts on 2027-06-06

    Examples:
      | scope | first_time | second_title | second_time |
      | whole series | 10:00 | Course | 10:00 |
      | this and future | 09:00 | Custom | 09:00 |

  Scenario Outline: EVENTS-001-S25 Cancelling a reset warning changes no saved event
    Given the Course exception fixture is prepared
    When the owner changes the series time to 10:00 with scope <scope> from 2027-06-03 and attempts to save
    And the owner cancels the reset warning and abandons the editor
    Then the Course exception fixture remains unchanged

    Examples:
      | scope |
      | whole series |
      | this and future |

  Scenario Outline: EVENTS-001-S26 Recurrence edits keep the remaining occurrence budget unless changed
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    When the owner changes the recurrence interval to 2 days with scope <scope> from 2027-06-03 without changing the ending and saves
    Then Course starts at 09:00 on exactly <expected_dates>
    And no Course occurrence starts on <absent_date>

    Examples:
      | scope | expected_dates | absent_date |
      | whole series | 2027-06-01, 2027-06-03, 2027-06-05, 2027-06-07, 2027-06-09 | 2027-06-02 |
      | this and future | 2027-06-01, 2027-06-02, 2027-06-03, 2027-06-05, 2027-06-07 | 2027-06-04 |

  Scenario: EVENTS-001-S27 A new future count starts with the first affected occurrence
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    When the owner sets the ending to after 2 occurrences with scope this and future from 2027-06-03 and saves
    Then Course starts at 09:00 on exactly 2027-06-01, 2027-06-02, 2027-06-03 and 2027-06-04
    And no Course occurrence starts on 2027-06-05

  Scenario Outline: EVENTS-001-S40 Other structural fields also require reset consent
    Given the Course exception fixture is prepared
    When the owner changes series field <field> to <value> with scope whole series from 2027-06-03 and attempts to save
    Then a warning identifies that affected individual modifications will reset and deleted occurrences may return
    When the owner cancels the reset warning and abandons the editor
    Then the Course exception fixture remains unchanged

    Examples:
      | field | value |
      | starting date | 2027-06-02 |
      | mode | all-day |
      | recurrence interval | 2 days |
