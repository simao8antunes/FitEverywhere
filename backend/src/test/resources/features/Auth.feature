Feature: User login

  Scenario: Successful login with valid credentials
    Given the user navigates to the login page
    When the user submits valid credentials
    Then the user should see the dashboard