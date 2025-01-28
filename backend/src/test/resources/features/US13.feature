Feature: Booking day passes and temporary memberships

  #AC1.4
  Scenario: Client books a day pass at a gym
    Given the client is on the gym’s page
    When the client selects the option to book a day pass
    Then the client should be able to complete the booking and receive a confirmation

  #AC1.5
  Scenario: Client books a temporary membership
    Given the client is on the gym’s page
    When the client selects the option for a temporary membership
    Then the client should be able to complete the booking and receive a confirmationccc