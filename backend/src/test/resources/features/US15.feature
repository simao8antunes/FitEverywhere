Feature: Synchronise workout plans with travel itineraries

  #AC1.7
  Scenario: Client syncs their workout plan with their travel itinerary
    Given the client has a workout plan
    When the client synchronises their workout plan with their travel itinerary
    Then the workout plan should adjust to accommodate changes in the travel itinerary