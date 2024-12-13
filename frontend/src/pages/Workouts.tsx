import React, { useEffect, useState } from "react";

interface WorkoutSuggestion {
  time: string;
  gym: string;
}

const Workouts: React.FC = () => {
  const [suggestedWorkouts, setSuggestedWorkouts] = useState<WorkoutSuggestion[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleFetchSuggestions = async () => {
    setIsLoading(true);
    setError(null); // Reset error before fetching

    try {
      const response = await fetch(
          import.meta.env.VITE_API_BASE_URL +
          "/client/workout-suggestions", {
        method: "GET",
        credentials: "include",
      });

      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message || "Failed to fetch workout suggestions");
        return;
      }

      const data = await response.json();
      console.log(data);
      setSuggestedWorkouts(data.suggestions || []);
    } catch (error) {
      console.error("Error fetching suggestions:", error);
      setError("Error fetching workout suggestions. Please try again later.");
    } finally {
      setIsLoading(false);
    }
  };

  // Fetch suggestions when the component loads
  useEffect(() => {
    handleFetchSuggestions();
  }, []);

  return (
    <div className="workouts-page">
      <h1>Workout Suggestions</h1>
      {isLoading && <p>Loading suggestions...</p>}
      {error && <p className="error">{error}</p>}

      {suggestedWorkouts.length > 0 ? (
        <ul className="workout-list">
          {suggestedWorkouts.map((workout, index) => (
            <li key={index} className="workout-item">
              <p>
                <strong>Time:</strong> {workout.time}
              </p>
              <p>
                <strong>Gym:</strong> {workout.gym}
              </p>
            </li>
          ))}
        </ul>
      ) : (
        !isLoading && <p>No workout suggestions available.</p>
      )}
    </div>
  );
};

export default Workouts;
