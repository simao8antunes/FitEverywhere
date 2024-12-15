import React, { useState } from "react";

import NearbyGyms from "../components/gyms/NearbyGyms.tsx";
import ShowGym from "../components/gyms/ShowGym.tsx"; // Assuming this hook fetches nearby gyms
import { useAuth } from "../hooks/useAuth";
import { useFetchGyms } from "../hooks/useFetchGyms";
import type { Gym } from "../types.ts";

const GymProfile: React.FC = () => {
  const { user } = useAuth();
  const { username, email, role } = user || {};
  const [isAddingGym, setIsAddingGym] = useState<boolean>(false);

  const [gymAddress, setGymAddress] = useState<string>("");

  const { gyms, fetchGyms } = useFetchGyms(); // Call the hook at the top level

  const [selectedGym, setSelectedGym] = useState<Gym | undefined>(undefined);

  return (
    <div data-testid="gym-profile" className="grid grid-cols-4 gap-base">
      <div className="card bg-base-100 shadow-xl">
        <div className="card-body">
          <hr className="my-4" />
          <div className="flex justify-center mb-6">
            <img
              src={user?.userSpecs.picture}
              alt={"profile"}
              className="rounded-circle size-32"
            />
          </div>

          <h2 className="card-title text-primary">
            {username ? `Welcome,\n ${username}!` : "Welcome!"}
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
          <div className="card-actions">
            <div className="justify-self-center">
              <button
                className="btn btn-primary"
                onClick={() => setIsAddingGym(!isAddingGym)}
              >
                + Link Gym
              </button>
            </div>
            {isAddingGym && (
              <div className="flex flex-col space-y-4">
                <input
                  type="text"
                  value={gymAddress}
                  onChange={(e) => setGymAddress(e.target.value)}
                  placeholder="Gym Address"
                  className="input input-bordered"
                />
                <button
                  className="btn btn-primary"
                  onClick={() => fetchGyms(gymAddress)}
                >
                  🔎 Search Gym
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
      <div className="card bg-base-100 shadow-xl col-span-2">
        <div className="card-body">
          <NearbyGyms
            gyms={gyms}
            loading={false}
            error={null}
            onSelectGym={setSelectedGym}
          />
        </div>
      </div>
      <div className="card bg-base-100 shadow-xl">
        <div className="card-body">
          <ShowGym gym={selectedGym} />
        </div>
      </div>
    </div>
  );
};

export default GymProfile;
