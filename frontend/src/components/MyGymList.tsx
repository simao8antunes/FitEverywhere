import React, { useState } from "react";
import type { GymsProps } from "../types";

const MyGymList: React.FC<GymsProps> = ({
  gyms,
  loading,
  error,
  onSelectGym,
}) => {
  const [isChangingPrice, setIsChangingPrice] = useState<number | null>(null);
  const [newPrice, setNewPrice] = useState<string>(""); // Store the new price
  const [updateError, setUpdateError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  if (loading) {
    return <div>Loading nearby gyms...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (gyms?.length === 0) {
    return <p>You have {gyms?.length}</p>;
  }

  const handleSelectGym = (gymId: number) => {
    if (onSelectGym) {
      onSelectGym(gyms[gymId]);
    }
  };

  const updateGymPrice = async (gymId: number) => {
    try {
      setUpdateError(null);
      setSuccessMessage(null);

      // Fetch the current gym details
      const currentGym = gyms.find((gym) => gym.id === gymId);
      if (!currentGym) {
        throw new Error("Gym not found");
      }

      const response = await fetch(`/api/gym/`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          ...currentGym, // Include all current gym fields
          dailyFee: parseFloat(newPrice), // Update only the dailyFee
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to update the price");
      }

      setSuccessMessage("Price updated successfully!");
      setIsChangingPrice(null);
    } catch (err: any) {
      setUpdateError(err.message);
    }
  };

  return (
    <ul className="gyms-list space-y-4">
      {gyms.map((gym, index) => (
        <li
          key={index}
          className="bg-background rounded-lg p-4 border border-secbackground hover:bg-secbackground transition-colors"
        >
          <h4 className="text-lg font-semibold">{gym.name}</h4>
          <p className="text-sm text-gray-500">Daily Fee: ${gym.dailyFee}</p>

          <button
            onClick={() =>
              setIsChangingPrice(isChangingPrice === gym.id ? null : gym.id)
            }
            className="mt-2 px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark"
          >
            {isChangingPrice === gym.id ? "Cancel" : "Change Price"}
          </button>

          {isChangingPrice === gym.id && (
            <div className="flex flex-col space-y-4 mt-4">
              <input
                type="number"
                placeholder="Enter new price"
                value={newPrice}
                onChange={(e) => setNewPrice(e.target.value)}
                className="p-2 border rounded"
              />
              <button
                className="px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark"
                onClick={() => updateGymPrice(gym.id)}
              >
                Set Price
              </button>
            </div>
          )}

          {updateError && (
            <p className="text-sm text-red-500 mt-2">{updateError}</p>
          )}
          {successMessage && (
            <p className="text-sm text-green-500 mt-2">{successMessage}</p>
          )}
        </li>
      ))}
    </ul>
  );
};

export default MyGymList;
