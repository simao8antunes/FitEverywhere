import React, { useState } from "react";
import { Gym, Purchase } from "../types.ts";
import { useAuth } from "../hooks/useAuth.ts";

interface GymFormProps {
  selectedGym: Gym | null;
}

const BuyGymService: React.FC<GymFormProps> = ({ selectedGym }) => {
  const [serviceType, setServiceType] = useState<string | null>(null);
  const [times, setTimes] = useState<number>(1);
  const [price, setPrice] = useState<number>(0);
  const { user, purchaseGymMembership } = useAuth();

  if (!selectedGym) return null;

  // Update price dynamically
  const calculatePrice = (type: string | null, times: number) => {
    if (type === "dailyFee") return times * selectedGym.dailyFee; // Example price
    if (type === "weeklyMembership")
      return times * selectedGym.weeklyMembership; // Example price
    return 0;
  };

  // Handle service type change
  const handleServiceTypeChange = (type: string) => {
    setServiceType(type);
    setPrice(calculatePrice(type, times));
  };

  // Handle times change
  const handleTimesChange = (value: string) => {
    const parsedValue = parseInt(value, 10);
    setTimes(parsedValue || 1);
    setPrice(calculatePrice(serviceType, parsedValue || 1));
  };

  // Handle form submission
  const handlePurchase = async () => {
    if (!serviceType || times < 1) {
      alert("Please select a service type and enter a valid number of times.");
      return;
    }

    const newPurchase: Purchase = {
      id: 0,
      gym: selectedGym,
      type: serviceType,
      purchaseDate: new Date().toISOString(),
      price: price,
      client: user!.email,
    };

    await purchaseGymMembership(newPurchase);
  };

  return (
    <div className="card bg-base-300 w-96 shadow-2xl">
      <div className="card-body">
        <h1 className="card-title">Buy Gym Service from {selectedGym?.name}</h1>
        <p className="text-lg text-neutral">
          Here you can buy services from the gym you selected.
        </p>
        <div className="form-control">
          <label>
            <span className="label-text">Service Name</span>
          </label>
          <select
            className="select select-bordered w-full max-w-xs"
            value={serviceType || ""}
            onChange={(e) => handleServiceTypeChange(e.target.value)}
          >
            <option disabled value="">
              Type of service
            </option>
            <option value="dailyFee">Daily Fee</option>
            <option value="weeklyMembership">Weekly Membership</option>
          </select>
          <label className="label-text mt-4">Quantity</label>
          <input
            type="number"
            className="input input-bordered"
            value={times}
            min="1"
            onChange={(e) => handleTimesChange(e.target.value)}
          />
          <br />
          <p className="mt-2">
            <strong>Price:</strong> € {price.toFixed(2)}
          </p>
        </div>
        <div className="card-actions justify-center">
          <button className="btn btn-primary" onClick={handlePurchase}>
            Buy Service
          </button>
        </div>
      </div>
    </div>
  );
};

export default BuyGymService;
