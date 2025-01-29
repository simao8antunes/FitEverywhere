import React, { useState } from "react";
import { CiHeart } from "react-icons/ci";
import { FaHeart } from "react-icons/fa"; // Import a filled heart icon
import { useFetchGyms } from "../../hooks/useFetchGyms.ts";
import type { GymsProps } from "../../types.ts";

const GymList: React.FC<GymsProps> = ({ gyms, loading, error }) => {
  const { addGymAndAddToFavourites } = useFetchGyms();

  // State to track favorited gyms
  const [favoritedGyms, setFavoritedGyms] = useState<number[]>([]);

  const toggleFavorite = (gymId: number) => {
    setFavoritedGyms((prev) =>
      prev.includes(gymId)
        ? prev.filter((id) => id !== gymId)
        : [...prev, gymId],
    );
    const gym = gyms?.find((gym) => gym.id === gymId);
    if (gym) {
      addGymAndAddToFavourites(gym);
    }
  };

  if (loading) {
    return <div>Loading nearby gyms...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (gyms?.length === 0) {
    return <p>No gyms found.</p>;
  }

  return (
    <div className="gyms-list space-y-4">
      {gyms &&
        gyms.map((gym, index) => (
          <div
            key={gym.id || `${gym.name}-${gym.distance}-${index}`}
            className="card bg-base-200 border-secbackground hover:bg-secbackground transition-colors gym"
          >
            <div className="card-body">
              <div className="flex justify-between">
                <h4 className="card-title">{gym.name}</h4>
                <button
                  onClick={() => toggleFavorite(gym.id)}
                  aria-label="Favorite"
                >
                  {favoritedGyms.includes(gym.id) ? (
                    <FaHeart className="text-white" />
                  ) : (
                    <CiHeart />
                  )}
                </button>
              </div>
              <p>Distance: {gym.distance} km</p>
              <p>Price: {gym.dailyFee} €</p>
            </div>
          </div>
        ))}
    </div>
  );
};

export default GymList;
