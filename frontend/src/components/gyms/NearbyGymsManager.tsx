import React, { useEffect, useState } from "react";
import type { Gym, GymsProps } from "../../types.ts";
import GoogleMap from "../GoogleMap.tsx";
import GymList2 from "./GymList2.tsx";

const NearbyGyms: React.FC<GymsProps> = ({
  gyms,
  loading,
  error,
  onSelectGym,
}) => {
  const [sortedGyms, setSortedGyms] = useState<Gym[]>([]);
  const [sortOption, setSortOption] = useState<string>("distance");

  // Sort gyms whenever the sort option or gyms data changes
  useEffect(() => {
    const sorted = [...gyms].sort((a, b) => {
      if (sortOption === "distance") {
        // Ensure distance is a string or fallback to Infinity for sorting
        const distanceA = parseFloat(a.distance?.toString() || "Infinity");
        const distanceB = parseFloat(b.distance?.toString() || "Infinity");
        return distanceA - distanceB;
      } else if (sortOption === "dailyFee") {
        // Ensure dailyFee is a string or fallback to Infinity for sorting
        const feeA = parseFloat(a.dailyFee?.toString() || "Infinity");
        const feeB = parseFloat(b.dailyFee?.toString() || "Infinity");
        return feeA - feeB;
      }
      return 0;
    });
    setSortedGyms(sorted);
  }, [sortOption, gyms]);

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

  return (
    <>
      {/* Sorting options */}
      <div className="mb-4 flex justify-end">
        <label htmlFor="sort" className="mr-2 font-semibold">
          Sort by:
        </label>
        <select
          id="sort"
          value={sortOption}
          onChange={(e) => setSortOption(e.target.value)}
          className="p-2 border rounded"
        >
          <option value="distance">Distance</option>
          <option value="dailyFee">Daily Fee</option>
        </select>
      </div>

      {/* Render GoogleMap component to display the gyms on a map */}
      <div className="mb-10">
        <GoogleMap gyms={sortedGyms} />
      </div>

      {/* Render list of gyms */}
      <GymList2
        gyms={sortedGyms}
        loading={loading}
        error={error}
        onSelectGym={onSelectGym}
      />
    </>
  );
};

export default NearbyGyms;
