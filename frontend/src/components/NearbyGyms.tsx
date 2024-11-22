import React from "react";
import GoogleMap from "../components/GoogleMap";
import type { NearbyGymsProps } from "../types";

const NearbyGyms: React.FC<NearbyGymsProps> = ({ gyms, loading, error }) => {
  // Render loading state
  if (loading) {
    return <div>Loading nearby gyms...</div>;
  }

  // Render error state
  if (error) {
    return <div>Error: {error}</div>;
  }

  // Render no gyms message
  if (gyms.length === 0) {
    return <p>No nearby gyms found.</p>;
  }

  // Render the list of gyms and the Google Map
  return (
    <div className="bg-intense shadow-md rounded-lg p-6 ">
      {/* Render GoogleMap component to display the gyms on a map */}
      <div className="mb-10">
        <GoogleMap gyms={gyms} />
      </div>
      {/* Render list of gyms */}
      <ul className="gyms-list space-y-4">
        {gyms.map((gym, index) => (
          <li
            key={index}
            className="bg-background rounded-lg p-4 border border-secbackground hover:bg-secbackground transition-colors"
          >
            <h4 className="text-lg font-semibold">{gym.name}</h4>
            <p className="text-sm">{gym.vicinity}</p>
            <p className="text-sm text-gray-500">Distance: {gym.distance} km</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default NearbyGyms;
