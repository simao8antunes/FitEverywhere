import React, { useEffect, useState } from "react";
import { useFetchGyms } from "../hooks/useFetchGyms"; // Assuming the hooks are in this path

interface WorkoutSuggestion {
  time: string;
  gym: string;
}

const Workouts: React.FC = () => {
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

  // Fetch suggestions when the component loads
  useEffect(() => {
    handleFetchSuggestions();
  }, []);

  return (
    <div className="flex justify-center items-center">
      <div className="bg-background rounded-lg shadow-lg p-8 w-full mx-4">
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
      </div>
    </div>
  );
};

export default Workouts;
