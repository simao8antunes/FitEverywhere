Feature: Creating a profile

  #AC2.1
  Scenario: Personal trainer creates a profile
    Given the personal trainer is on the registration page
    When the personal trainer fills in the necessary details and submits the form
    Then the personal trainer’s profile should be created and visible to clients