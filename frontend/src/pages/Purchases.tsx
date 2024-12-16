import React, { useEffect, useState } from "react";
import { useFetchGyms } from "../hooks/useFetchGyms.ts";
import { type Client, Gym, type UserOptions } from "../types.ts";
import BuyGymService from "../components/BuyGymService.tsx";
import { useAuth } from "../hooks/useAuth.ts";

const Purchases: React.FC = () => {
  const { gyms, getAllGyms } = useFetchGyms();
  const { user } = useAuth();
  const [selectedGym, setSelectedGym] = useState<Gym | null>(null);

  useEffect(() => {
    getAllGyms();
  }, []);

  const isClient = (user: UserOptions | null): user is Client => {
    return user?.role === "client";
  };

  return (
    <div className="p-base grid grid-cols-1 lg:grid-cols-2">
      <div>
        <h1 className="text-3xl font-bold">Purchase Gym Offers</h1>
        <p className="text-text opacity-30">
          Here you can see all the gyms services you can purchase.
        </p>
        <div className="p-3 flex gap-base w-full overflow-x-auto">
          {gyms.map((gym) => (
            <div
              key={gym.id}
              className={`card bg-base-100 w-64 shadow-xl cursor-pointer ${gym.id === selectedGym?.id ? "border-2 border-primary" : ""}`}
              onClick={() => setSelectedGym(gym)}
            >
              <div className="card-body">
                <h2 className="card-title">{gym.name}</h2>
                <p>{gym.id}</p>
              </div>
            </div>
          ))}
        </div>
        <BuyGymService selectedGym={selectedGym} />
      </div>
      <div className="grid gap-base">
        <h1 className="text-3xl font-bold mb-base">My Purchases</h1>
        {isClient(user) &&
          user.purchases?.map((purchase) => (
            <div key={purchase.id} className="card bg-base-100 w-96 shadow-xl">
              <div className="card-body">
                <h2 className="card-title">Type: {purchase.type}</h2>
                <p>Gym: {purchase.gym.name}</p>
                <p>Price: {purchase.price}€</p>
              </div>
            </div>
          ))}
      </div>
    </div>
  );
};

export default Purchases;
