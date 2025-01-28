Feature: Customising a Profile with Specialised Services

  #AC2.2
  Scenario: Personal trainer adds specialised services to their profile
    Given the personal trainer has an existing profile
    When the personal trainer adds specialised services like yoga or strength training
    Then the services should be displayed on the trainer’s profile for clients to view