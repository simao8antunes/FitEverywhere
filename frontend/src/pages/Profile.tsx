import React, { useEffect, useState } from "react";
import { useAuth } from "../hooks/useAuth.ts";
import type { Client, UserOptions } from "../types.ts";

const Profile: React.FC = () => {
  const { user } = useAuth();

  const isClient = (user: UserOptions | null): user is Client => {
    return user?.role === "client";
  };

  const { username, email, role } = user || {};
  const [workoutsPerWeek, setWorkoutsPerWeek] = useState<number | null>(null);
  const [preferredTime, setPreferredTime] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);

  // If the user is a client, set initial state values
  useEffect(() => {
    if (isClient(user) && workoutsPerWeek === null && preferredTime === null) {
      setWorkoutsPerWeek(user.workoutsPerWeek);
      setPreferredTime(user.preferredTime);
    }
  }, [user, workoutsPerWeek, preferredTime]);

  const handleSavePreferences = async () => {
    try {
      const response = await fetch(
        `/api/client/workout-preferences?number=${workoutsPerWeek}&time=${preferredTime}`,
        {
          method: "PUT",
          credentials: "include",
        },
      );

      if (!response.ok) {
        const errorData = await response.json();
        console.error(
          "Failed to save preferences:",
          errorData.message || response.statusText,
        );
        alert("Failed to save preferences");
        return;
      }

      const data = await response.json();
      alert(data.message);
      setIsEditing(false);
    } catch (error) {
      console.error("Error saving preferences:", error);
      alert("Error saving preferences");
    }
  };

  if (!isClient(user)) {
    return <div>User is not a client.</div>;
  }

  return (
    <div
      data-testid="client-profile"
      className="flex justify-center items-center w-max"
    >
      <div className="bg-background rounded-lg shadow-lg p-8 max-w-md w-full mx-4">
        <div className="flex justify-center mb-6">
          <img
            src={user?.userSpecs.picture}
            alt={"profile"}
            className="rounded-circle size-32"
          />
        </div>
        <h2 className="text-2xl font-semibold text-primary mb-4">
          {username ? `Welcome, ${username}!` : "Welcome!"}
        </h2>
        <div className="text-left space-y-3">
          <div>
            <span className="font-bold">Username:</span> {username || "N/A"}
          </div>
          <div>
            <span className="font-bold">Email:</span> {email || "N/A"}
          </div>
          <div>
            <span className="font-bold">Role:</span> {role || "N/A"}
          </div>
        </div>
        <hr className="my-4" />
        {!isEditing ? (
          <div>
            <div>
              <span className="font-bold">Workouts Per Week:</span>{" "}
              {workoutsPerWeek !== null ? workoutsPerWeek : "Not set"}
            </div>
            <div>
              <span className="font-bold">Preferred Time:</span>{" "}
              {preferredTime || "Not set"}
            </div>
            <button
              className="bg-primary text-white py-2 px-4 rounded mt-4"
              onClick={() => setIsEditing(true)}
            >
              Edit Preferences
            </button>
          </div>
        ) : (
          <div>
            <div>
              <label className="font-bold">Workouts Per Week:</label>
              <input
                type="number"
                className="border p-2 rounded w-full"
                value={workoutsPerWeek || ""}
                onChange={(e) => setWorkoutsPerWeek(Number(e.target.value))}
              />
            </div>
            <div>
              <label className="font-bold">Preferred Time:</label>
              <select
                className="border p-2 rounded w-full"
                value={preferredTime || ""}
                onChange={(e) => setPreferredTime(e.target.value)}
              >
                <option value="">Select</option>
                <option value="morning">Morning</option>
                <option value="afternoon">Afternoon</option>
                <option value="evening">Evening</option>
              </select>
            </div>
            <button
              className="bg-primary text-white py-2 px-4 rounded mt-4"
              onClick={handleSavePreferences}
            >
              Save Preferences
            </button>
            <button
              className="bg-secondary text-white py-2 px-4 rounded mt-4 ml-2"
              onClick={() => setIsEditing(false)}
            >
              Cancel
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
