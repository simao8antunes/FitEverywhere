import React, { useEffect, useState } from "react";
import { FiUser } from "react-icons/fi";
import { useAuth } from "../hooks/useAuth";
import { useFetchGyms } from "../hooks/useFetchGyms";
import type { Gym, GymManager, UserOptions } from "../types.ts";
import GymList from "../components/GymList.tsx";
import NearbyGyms from "../components/NearbyGyms.tsx";
import ShowGym from "../components/ShowGym.tsx"; // Assuming this hook fetches nearby gyms

const GymProfile: React.FC = () => {
  const { user } = useAuth();
  const { username, email, role } = user || {};
  const [myGyms, setMyGyms] = useState<Gym[]>([]);
  const [isAddingGym, setIsAddingGym] = useState<boolean>(false);

  const [gymAddress, setGymAddress] = useState<string>("");

  const { gyms, fetchOwnGyms, fetchNearbyGyms } = useFetchGyms(); // Call the hook at the top level

  const [selectedGym, setSelectedGym] = useState<Gym | undefined>(undefined);
  const isGymManager = (user: UserOptions | null): user is GymManager => {
    return user?.role === "gym_manager";
  };

  useEffect(() => {
    if (isGymManager(user) && myGyms.length === 0) {
      console.log(user.linkedGyms);
      setMyGyms(user.linkedGyms);
    }
  }, [user]);

  useEffect(() => {
    fetchOwnGyms();
  }, []);

  return (
    <div className="flex justify-center items-center">
      <div className="bg-background rounded-lg shadow-lg p-8  w-full mx-4">
        <hr className="my-4" />
        <div className="flex justify-center mb-6">
          <FiUser className="w-20 h-20" />
        </div>
        <h2 className="text-2xl font-semibold text-primary mb-4">
          {username ? `Welcome, ${username}!` : "Welcome!"}
        </h2>
        <div className="text-left space-y-3">
          <div>
            <span className="font-bold">Username:</span> {username || "N/A"}
          </div>
          <div>
            <span className="font-bold">Email:</span> {email || "N/A"}
          </div>
          <div>
            <span className="font-bold">Role:</span> {role || "N/A"}
          </div>
        </div>
        <hr className="my-4" />
        <div className="justify-self-center">
          <button
            className="px-4 py-2 bg-primary rounded"
            onClick={() => setIsAddingGym(!isAddingGym)}
          >
            + Link Gym
          </button>
        </div>
        {isAddingGym && (
          <div className="flex flex-col space-y-4 mt-4">
            <input
              type="text"
              value={gymAddress}
              onChange={(e) => setGymAddress(e.target.value)}
              placeholder="Gym Address"
              className="border p-2 rounded"
            />
            <button
              className="px-4 py-2 bg-primary rounded"
              onClick={() => fetchNearbyGyms(gymAddress)}
            >
              {" "}
              Search Gym
            </button>
          </div>
        )}
      </div>
      <div className="bg-background rounded-lg shadow-lg p-8  w-full mx-4">
        <GymList gyms={myGyms} loading={false} error={null} />
        <NearbyGyms
          gyms={gyms}
          loading={false}
          error={null}
          onSelectGym={setSelectedGym}
        />
      </div>
      <div className="bg-background rounded-lg shadow-lg p-8  w-full mx-4">
        <ShowGym gym={selectedGym} />
      </div>
    </div>
  );
};

export default GymProfile;
