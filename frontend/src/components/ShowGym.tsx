import type { Gym } from "../types.ts";
import React, { useState } from "react";

interface ShowGymProps {
  gym?: Gym;
}

const ShowGym: React.FC<ShowGymProps> = ({ gym }) => {
  const [message, setMessage] = useState<string | null>(null);
  const handleCreateGym = async () => {
    try {
      const url = new URL(
        import.meta.env.VITE_API_BASE_URL + "/gym/",
        window.location.origin,
      );
      url.searchParams.append("name", gym?.name || "");
      url.searchParams.append("id", gym?.id?.toString() || "");
      console.log(gym?.id);

      const response = await fetch(url.toString(), {
        method: "POST",
        credentials: "include",
      });

      if (response.ok) {
        setMessage("Gym created successfully!");
      } else {
        const errorData = await response.json();
        setMessage(`Error: ${errorData.message}`);
      }
    } catch (error) {
      setMessage("Error creating gym. Please try again later." + error);
    }
  };
  return (
    <div>
      <h1> {gym?.name} </h1>
      <h1> {gym?.dailyFee} </h1>
      <button
        onClick={handleCreateGym}
        className="mt-2 px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark"
      >
        Create Gym
      </button>
      {message && <p>{message}</p>}
    </div>
  );
};

export default ShowGym;
