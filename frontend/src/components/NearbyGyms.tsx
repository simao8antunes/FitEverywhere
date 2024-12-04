import React, { useState } from "react";
import GoogleMap from "../components/GoogleMap";
import type { Gym, GymsProps } from "../types";
import GymList from "./GymList.tsx";
import ShowGym from "./ShowGym.tsx";

const NearbyGyms: React.FC<GymsProps> = ({
  gyms,
  loading,
  error,
  onSelectGym,
}) => {
  // Render loading state
  const [selectedGym] = useState<Gym | undefined>(undefined);
  if (loading) {
    return <div>Loading nearby gyms...</div>;
  }

  // Render error state
  if (error) {
    return <div>Error: {error}</div>;
  }

  // Render no gyms message
  if (gyms && gyms.length === 0) {
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
      <GymList
        gyms={gyms}
        loading={loading}
        error={error}
        onSelectGym={onSelectGym}
      />
      <ShowGym gym={selectedGym} />
    </div>
  );
};

export default NearbyGyms;
