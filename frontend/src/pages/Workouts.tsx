import React, { useEffect, useState } from "react";
import { useFetchGyms } from "../hooks/useFetchGyms"; // Assuming the hooks are in this path

interface WorkoutSuggestion {
  id: number;
  time: string;
  gym: string;
}

const Workouts: React.FC = () => {
  const [savedWorkouts, setSavedWorkouts] = useState<WorkoutSuggestion[]>([]);
  const [suggestedWorkouts, setSuggestedWorkouts] = useState<
    WorkoutSuggestion[]
  >([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showSearchOptions, setShowSearchOptions] = useState<number | null>(
    null,
  );
  const [streetInput, setStreetInput] = useState<string>("");
  const [selectedWorkoutIndex, setSelectedWorkoutIndex] = useState<
    number | null
  >(null);

  const {
    gyms,
    loading: gymLoading,
    error: gymError,
    fetchGyms,
  } = useFetchGyms();

  const handleFetchSuggestions = async () => {
    setIsLoading(true);
    setError(null); // Reset error before fetching

    try {
      const response = await fetch(
        import.meta.env.VITE_API_BASE_URL + "/client/workout-suggestions",
        {
          method: "GET",
          credentials: "include",
        },
      );

      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message || "Failed to fetch workout suggestions");
        return;
      }

      const data = await response.json();
      console.log(data);

      // Transform the data
      if (Array.isArray(data)) {
        const suggestions = data.map((item: string) => {
          const [startTime, endTime] = item.split("/"); // Split by "/"
          return {
            id: Date.now(), // You can replace this with the actual ID if available
            time: `${startTime} to ${endTime}`, // Combine start and end times
            gym: "-", // Replace with real gym name if available
          };
        });
        setSuggestedWorkouts(suggestions);
      } else {
        setSuggestedWorkouts([]);
      }
    } catch (error) {
      console.error("Error fetching suggestions:", error);
      setError("Error fetching workout suggestions. Please try again later.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearchGym = async (type: "event" | "street", index: number) => {
    if (type === "street") {
      if (!streetInput.trim()) {
        setError("Please enter a street name to search for gyms.");
        return;
      }
      await fetchGyms(streetInput);
      setSelectedWorkoutIndex(index); // Set the selected workout index
    } else {
      alert(
        `Searching for gyms near the event location for workout ${index + 1}`,
      );
    }
    setShowSearchOptions(null); // Close options after selection
  };

  const handleSelectGym = (gymName: string) => {
    if (selectedWorkoutIndex === null) return;

    setSuggestedWorkouts((prev) =>
      prev.map((workout, index) =>
        index === selectedWorkoutIndex ? { ...workout, gym: gymName } : workout,
      ),
    );

    setSelectedWorkoutIndex(null); // Reset selected workout index
  };

  // Function to save the workout suggestions to the backend
  const handleSaveSuggestions = async () => {
    setIsLoading(true);
    setError(null); // Reset error before saving

    try {
      const response = await fetch(
        import.meta.env.VITE_API_BASE_URL + "/client/save-workout-suggestions",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(
            suggestedWorkouts.map((workout) => ({
              time: workout.time,
              gym: workout.gym,
            })),
          ),
          credentials: "include",
        },
      );

      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message || "Failed to save workout suggestions");
        return;
      }

      // Success: Optionally show a success message or redirect
      console.log("Workout suggestions saved successfully!");
      // After saving, update the savedWorkouts state to include the new workouts
      const savedData = await response.json(); // Assuming the backend returns the saved workouts
      fetchSavedWorkouts();
    } catch (error) {
      console.error("Error saving workout suggestions:", error);
      setError("Error saving workout suggestions. Please try again later.");
    } finally {
      setIsLoading(false);
    }
  };

  // Make API call to fetch saved workouts
  const fetchSavedWorkouts = async () => {
    try {
      const response = await fetch(
        import.meta.env.VITE_API_BASE_URL + "/client/saved-workout-suggestions",
        {
          method: "GET",
          credentials: "include",
        },
      );

      const workouts = await response.json();
      console.log(workouts); // Ensure the updated list is returned
      setSavedWorkouts(workouts); // Update the state with the new list
    } catch (error) {
      console.error("Error fetching saved workouts:", error);
    }
  };

  const handleDeleteWorkout = async (id: number) => {
    try {
      const response = await fetch(
        import.meta.env.VITE_API_BASE_URL +
          `/client/delete-workout-suggestion/${id}`,
        {
          method: "DELETE",
          credentials: "include",
        },
      );

      if (response.ok) {
        const message = await response.text();
        console.log(message); // Should log "Workout suggestion deleted successfully!"
        // Re-fetch the list of saved workouts to update the UI
        fetchSavedWorkouts();
      } else {
        const errorMessage = await response.text();
        console.error("Failed to delete workout suggestion:", errorMessage);
      }
    } catch (error) {
      console.error("Error deleting workout suggestion:", error);
    }
  };

  // Fetch suggestions when the component loads
  useEffect(() => {
    fetchSavedWorkouts();
    handleFetchSuggestions();
  }, []);

  return (
    <div className="flex justify-center items-start">
      <div className="bg-background rounded-lg shadow-lg p-8 w-full mx-4 flex space-x-8">
        {/* Workout Suggestions */}
        <div className="flex-1">
          <h1 className="text-2xl font-semibold text-primary mb-6">
            Workout Suggestions
          </h1>
          {isLoading && <p>Loading suggestions...</p>}
          {error && <p className="error text-red-500">{error}</p>}

          {suggestedWorkouts.length > 0 ? (
            <ul className="workout-list space-y-4">
              {suggestedWorkouts.map((workout, index) => (
                <li
                  key={index}
                  className="workout-item p-4 border border-gray-200 rounded-lg space-y-2"
                >
                  <p className="font-semibold text-lg">
                    <strong>Time:</strong> {workout.time}
                  </p>
                  <p>
                    <strong>Gym:</strong> {workout.gym}
                  </p>
                  <button
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                    onClick={() => setShowSearchOptions(index)}
                  >
                    Find a Gym
                  </button>

                  {showSearchOptions === index && (
                    <div className="mt-4 p-4 bg-100 border rounded">
                      <h3 className="font-semibold mb-2">
                        Choose the Gym Search Location
                      </h3>
                      <div className="flex flex-col mb-4">
                        <input
                          type="text"
                          value={streetInput}
                          onChange={(e) => setStreetInput(e.target.value)}
                          placeholder="Enter street name"
                          className="border p-2 rounded mb-2"
                        />
                        <button
                          className="px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600"
                          onClick={() => handleSearchGym("street", index)}
                        >
                          Search by Street
                        </button>
                      </div>
                    </div>
                  )}
                </li>
              ))}
            </ul>
          ) : (
            !isLoading && <p>No workout suggestions available.</p>
          )}

          {/* Display nearby gyms if searched */}
          {gymLoading && <p>Searching for gyms...</p>}
          {gymError && <p className="error text-red-500">{gymError}</p>}
          {gyms.length > 0 && selectedWorkoutIndex !== null && (
            <div className="mt-6">
              <h2 className="text-lg font-semibold">Nearby Gyms</h2>
              <ul className="space-y-2">
                {gyms.map((gym, idx) => (
                  <li
                    key={idx}
                    className="border p-2 rounded cursor-pointer hover:bg-black-100"
                    onClick={() => handleSelectGym(gym.name)}
                  >
                    <p>
                      <strong>Name:</strong> {gym.name}
                    </p>
                    <p>
                      <strong>Distance:</strong> {gym.distance} km
                    </p>
                  </li>
                ))}
              </ul>
            </div>
          )}
          {/* Button to save workout suggestions */}
          <button
            className="mt-6 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
            onClick={handleSaveSuggestions}
          >
            Save Workout Suggestions
          </button>
        </div>

        {/* Saved Workouts */}
        <div className="w-80">
          <h2 className="text-2xl font-semibold text-primary mb-6">
            Saved Workouts
          </h2>
          <div>
            {savedWorkouts.length === 0 ? (
              <p>No saved workouts</p>
            ) : (
              <ul>
                {savedWorkouts.map((workout) => (
                  <li
                    key={workout.id}
                    className="flex justify-between items-center"
                  >
                    <span>
                      {workout.time} - {workout.gym}
                    </span>
                    <button
                      onClick={() => handleDeleteWorkout(workout.id)}
                      className="px-3 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    >
                      Delete
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Workouts;
