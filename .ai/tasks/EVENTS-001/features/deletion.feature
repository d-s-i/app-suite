Feature: EVENTS-001-F06 Delete exactly the selected event scope
  Cancellation never silently extends recurrence or removes unrelated events.

  Background:
    Given the calendar contains no events
    And the phone time zone is UTC
    And the phone language is English United States
    And the phone uses 24-hour time

  Scenario Outline: EVENTS-001-S28 Delete a one-time event without deleting its neighbour
    Given Remove is <mode> on 2027-02-06
    And Keep is all-day on 2027-02-06 with description Unrelated
    When the owner deletes Remove and confirms
    Then Remove is absent from Month, Week and Day for 2027-02-06
    And Keep is still available with description Unrelated

    Examples:
      | mode |
      | all-day |
      | timed |

  Scenario Outline: EVENTS-001-S29 Delete the selected recurring scope
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    When the owner deletes the occurrence on 2027-06-03 with scope <scope> and confirms
    Then exactly <remaining_count> Course starts remain from 2027-06-01 through 2027-06-10
    And the 2027-06-03 occurrence is absent
    And the 2027-06-01 occurrence is <first_presence>
    And the 2027-06-05 occurrence is <last_presence>
    And no Course occurrence starts on 2027-06-06

    Examples:
      | scope | remaining_count | first_presence | last_presence |
      | this occurrence | 4 | present | present |
      | this and future | 2 | present | absent |
      | whole series | 0 | absent | absent |

  Scenario Outline: EVENTS-001-S30 Cancel a deletion without changing the series
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    When the owner requests deletion from 2027-06-03 with scope <scope> but cancels before confirmation
    Then Course still starts at 09:00 on each date from 2027-06-01 through 2027-06-05
    And no occurrence starts on 2027-06-06

    Examples:
      | scope |
      | this occurrence |
      | this and future |
      | whole series |

  Scenario Outline: EVENTS-001-S31 Scope follows original series order after a move
    Given Course repeats daily at 09:00 from 2027-06-01 for 5 occurrences with description Base
    And only the occurrence originally on 2027-06-05 was moved to 11:00 on 2027-05-31
    When the owner deletes the occurrence on 2027-06-03 with scope <scope> and confirms
    Then exactly <remaining_count> Course starts remain from 2027-05-31 through 2027-06-10
    And no Course occurrence remains on 2027-05-31, 2027-06-03, 2027-06-04 or 2027-06-05
    And the 2027-06-01 and 2027-06-02 occurrences are <past_presence>

    Examples:
      | scope | remaining_count | past_presence |
      | this and future | 2 | present |
      | whole series | 0 | absent |
