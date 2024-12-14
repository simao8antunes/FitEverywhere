import { useAuth } from "../hooks/useAuth.ts";
import { GymManager, UserOptions } from "../types.ts";
import type { Gym } from "../types.ts";
import { useState } from "react";
import GymForm from "../components/gyms/GymForm.tsx";

const MyGyms = () => {
  const { user } = useAuth();
  const [selectedGym, setSelectedGym] = useState<Gym | null>(null);
  const isGymManager = (user: UserOptions | null): user is GymManager => {
    return user?.role === "gym_manager";
  };

  return (
    <div className="p-base">
      <h1 className="text-3xl font-bold">My Gyms</h1>
      <p className="text-text opacity-30">
        Here you can see all the gyms you manage.
      </p>
      <div className="p-3 flex gap-base">
        {isGymManager(user) &&
          user.linkedGyms.map((gym) => (
            <div
              key={gym.id}
              className="card bg-base-100 w-96 shadow-xl cursor-pointer"
              onClick={() => setSelectedGym(gym)}
            >
              <div className="card-body">
                <h2 className="card-title">{gym.name}</h2>
                <p>{gym.id}</p>
              </div>
            </div>
          ))}
      </div>
      <GymForm selectedGym={selectedGym} />
    </div>
  );
};

export default MyGyms;
