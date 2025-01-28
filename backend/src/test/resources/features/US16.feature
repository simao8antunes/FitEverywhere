Feature: Choose a personal trainer for a one-on-one coaching

  #AC1.8
  Scenario: Client selects a personal trainer for coaching
    Given the client is viewing personal trainer profiles
    When the client selects a personal trainer for one-on-one coaching
    Then the client should be able to book a coaching session with the trainer