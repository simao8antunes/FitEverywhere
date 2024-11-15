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
    <div className="bg-white shadow-md rounded-lg p-6 flex flex-col">
      {/* Render GoogleMap component to display the gyms on a map */}
      <div className="h-64 w-full mb-6">
        <GoogleMap gyms={gyms} />
      </div>
      {/* Render list of gyms */}
      <ul className="space-y-4">
        {gyms.map((gym, index) => (
          <li
            key={index}
            className="bg-gray-100 rounded-lg p-4 border border-gray-200 hover:bg-gray-200 transition-colors"
          >
            <h4 className="text-lg font-semibold">{gym.name}</h4>
            <p className="text-sm text-gray-600">{gym.vicinity}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default NearbyGyms;
