import React, { useState, useEffect } from "react";

interface DayOfWeek {
  day_of_week: string;
}

interface ExerciseCategory {
  id: string;
  name: string;
}

interface Exercise {
  id: number;
  name: string;
  description: string;
  reps: number;
  sets: number;
}

const API_BASE = "https://wger.de/api/v2";

const fetchData = async (endpoint: string) => {
  const response = await fetch(`${API_BASE}/${endpoint}/`);
  if (!response.ok) {
    throw new Error(`Failed to fetch ${endpoint}`);
  }
  return response.json();
};

const WorkoutPlanner: React.FC = () => {
  const [daysOfWeek, setDaysOfWeek] = useState<DayOfWeek[]>([]);
  const [categories, setCategories] = useState<ExerciseCategory[]>([]);
  const [selectedDays, setSelectedDays] = useState<
    { day: string; category: number | null }[]
  >([]);
  const [workoutPlan, setWorkoutPlan] = useState<
    { day: string; exercises: Exercise[] }[]
  >([]);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [days, cats] = await Promise.all([
          fetchData("daysofweek"),
          fetchData("exercisecategory"),
        ]);
        setDaysOfWeek(days.results);
        setCategories(cats.results);
      } catch (error) {
        console.error("Error fetching initial data:", error);
      }
    };

    fetchInitialData();
  }, []);

  const handleDayCategoryChange = (day: string, category: number | null) => {
    console.log("Day:", day, "Category:", category);
    setSelectedDays((prev) => {
      const existing = prev.find((d) => d.day === day);
      if (existing) {
        return prev.map((d) => (d.day === day ? { ...d, category } : d));
      }
      return [...prev, { day, category }];
    });
  };

  const generateWorkoutPlan = async () => {
    try {
      const plans = await Promise.all(
        selectedDays.map(async ({ day, category }) => {
          if (!category) return { day, exercises: [] };
          console.log("Generating workout plan for", day, category);
          const params = new URLSearchParams({
            language: "2",
            category: category.toString(),
          });

          const response = await fetch(
            `${API_BASE}/exercise/?${params.toString()}`,
          );
          if (!response.ok) {
            throw new Error("Failed to fetch exercises");
          }

          const data = await response.json();
          const exercises = data.results.slice(0, 5).map((exercise: any) => ({
            id: exercise.id,
            name: exercise.name,
            description: exercise.description,
            reps: Math.floor(Math.random() * 6) + 8,
            sets: Math.floor(Math.random() * 3) + 3,
          }));

          return { day, exercises };
        }),
      );

      setWorkoutPlan(plans);
    } catch (error) {
      console.error("Error generating workout plan:", error);
    }
  };

  return (
    <div className="p-6 max-w-4xl mx-auto bg-base-200 rounded-lg shadow-lg">
      <h1 className="text-3xl font-bold text-center mb-6">Workout Planner</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {daysOfWeek.map((day) => (
          <div
            key={day.day_of_week}
            className="flex flex-col bg-white p-4 rounded-lg shadow-md"
          >
            <label className="flex items-center space-x-2 mb-2">
              <input
                type="checkbox"
                className="checkbox checkbox-primary"
                value={day.day_of_week}
                onChange={(e) => {
                  if (e.target.checked) {
                    setSelectedDays([
                      ...selectedDays,
                      { day: day.day_of_week, category: null },
                    ]);
                  } else {
                    setSelectedDays(
                      selectedDays.filter((d) => d.day !== day.day_of_week),
                    );
                  }
                }}
              />
              <span className="font-medium">{day.day_of_week}</span>
            </label>
            {selectedDays.find((d) => d.day === day.day_of_week) && (
              <select
                className="select select-bordered"
                onChange={(e) =>
                  handleDayCategoryChange(
                    day.day_of_week,
                    Number(e.target.value),
                  )
                }
              >
                <option value="">Select a category</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
            )}
          </div>
        ))}
      </div>

      <button
        className="btn btn-primary mt-6 w-full"
        onClick={generateWorkoutPlan}
      >
        Generate Workout Plan
      </button>

      {workoutPlan.length > 0 && (
        <div className="mt-6">
          <h2 className="text-2xl font-semibold mb-4">Your Workout Plan</h2>
          {workoutPlan.map(({ day, exercises }) => (
            <div key={day} className="mb-6">
              <h3 className="text-xl font-medium mb-2">{day}</h3>
              {exercises.length > 0 ? (
                <ul className="space-y-2">
                  {exercises.map((exercise) => (
                    <li
                      key={exercise.id}
                      className="p-4 bg-white rounded-lg shadow-sm"
                    >
                      <h4 className="font-semibold">{exercise.name}</h4>
                      <p>
                        <strong>Reps:</strong> {exercise.reps} |{" "}
                        <strong>Sets:</strong> {exercise.sets}
                      </p>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-gray-500">
                  No exercises available for this day.
                </p>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default WorkoutPlanner;
