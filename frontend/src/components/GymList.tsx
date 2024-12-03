import React from "react";
import type { GymsProps } from "../types.ts";

const GymList: React.FC<GymsProps> = ({
  gyms,
  loading,
  error,
  onSelectGym,
}) => {
  if (loading) {
    return <div>Loading nearby gyms...</div>;
  }

  // Render error state
  if (error) {
    return <div>Error: {error}</div>;
  }

  // Render no gyms message
  if (gyms?.length === 0) {
    return <p>No gyms found.</p>;
  }

  const handleSelectGym = (gymId: number) => {
    if (onSelectGym) {
      onSelectGym(gyms[gymId]); // Pass the selected gym's ID back to the parent
    }
  };

  return (
    <ul className="gyms-list space-y-4">
      {gyms &&
        gyms.map((gym, index) => (
          <li
            key={index}
            className="bg-background rounded-lg p-4 border border-secbackground hover:bg-secbackground transition-colors"
          >
            <h4 className="text-lg font-semibold">{gym.name}</h4>
            <p className="text-sm text-gray-500">Distance: {gym.distance} km</p>
            <p className="text-sm text-gray-500">Price: {gym.dailyFee} $</p>
      
            <button
              onClick={() => handleSelectGym(index)} // When clicked, trigger the selection
              className="mt-2 px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark"
            >
              Select Gym
            </button>
          </li>
        ))}
    </ul>
  );
};

export default GymList;
