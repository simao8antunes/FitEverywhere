Feature: Filter Gyms

  #AC1.3
  Scenario: Client applies filters to search results
    Given the client is on the search results page
    When the client selects filters for amenities such as pool, classes, or free weights
    Then only gyms that match the selected filters should be shown in the results