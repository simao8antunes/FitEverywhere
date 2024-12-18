import React from "react";
import type { GymsProps } from "../../types.ts";

const GymList: React.FC<GymsProps> = ({
  gyms,
  loading,
  error,
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

  return (
    <div className="gyms-list space-y-4">
      {gyms &&
        gyms.map((gym, index) => (
          <div
            key={index}
            className="card bg-base-200 border-secbackground hover:bg-secbackground transition-colors"
          >
            <div className="card-body">
              <h4 className="card-title">{gym.name}</h4>
              <p>Distance: {gym.distance} km</p>
              <p>Price: {gym.dailyFee} $</p>
            </div>
          </div>
        ))}
    </div>
  );
};

export default GymList;
