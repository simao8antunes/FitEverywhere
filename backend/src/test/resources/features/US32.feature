Feature: Manage Pricing

  #AC3.2
  Scenario: Gym manager updates gym pricing
    Given the gym manager is logged in and on the pricing page
    When the manager updates pricing details for day passes and memberships
    Then the updated pricing should be visible to clients on the gym profile