Feature: Create a profile

  #AC3.1
  Scenario: Gym manager creates a gym profile
    Given the gym manager is logged in
    When the gym manager enters gym details and submits the form
    Then the gym profile should be created and visible to clients searching for gyms