Feature: Search for gyms near my travel destination

  # AC1.1
  Scenario: Client Searches for gyms near a travel destination
    Given the client is on the search page
    When the client clicks an event
    Then a list of gyms near the travel destination should be displayed

  # AC1.2
  Scenario: Client sees gyms displayed on a map
    Given the client has searched for gyms near a travel destination
    When the gyms are displayed
    Then the gyms should be marked on a map with location markers