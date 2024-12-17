import React, { useState } from "react";
import { PersonalTrainer, PTService } from "../types.ts";
import { useAuth } from "../hooks/useAuth.ts";

interface PTFormProps {
  selectedPT: PersonalTrainer | null;
}

const BuyGymService: React.FC<PTFormProps> = ({ selectedPT }) => {
  const [selectedService, setSelectedService] = useState<PTService | null>(
    null,
  ); // `number` for service ID
  const [price, setPrice] = useState<number>(0);
  const { purchasePTService } = useAuth();

  // Function to handle service selection
  const handleServiceChange = (serviceId: string) => {
    const service = selectedPT?.services?.find(
      (service) => service.id === Number(serviceId),
    );
    setSelectedService(service ? service : null);
    setPrice(service ? service.price : 0);
  };

  // Function to handle the purchase
  const handlePurchase = async () => {
    if (!selectedService) {
      alert("Please select a service to purchase!");
      return;
    }

    try {
      await purchasePTService(selectedService.id);
    } catch (error) {
      console.error("Error purchasing service:", error);
      alert("Failed to purchase service. Please try again.");
    }
  };

  if (!selectedPT) return null;

  return (
    <div className="card bg-base-300 w-96 shadow-2xl">
      <div className="card-body">
        <h1 className="card-title">
          Buy PT Service from {selectedPT?.username}
        </h1>
        <p className="text-lg text-neutral">
          Here you can buy services from the gym you selected.
        </p>
        <div className="form-control">
          <label>
            <span className="label-text">Service Name</span>
          </label>
          <select
            className="select select-bordered w-full max-w-xs"
            value={selectedService?.id || ""}
            onChange={(e) => handleServiceChange(e.target.value)}
          >
            <option disabled value="">
              Select a service
            </option>
            {selectedPT?.services?.map((service) => (
              <option key={service.id} value={service.id}>
                {service.name}
              </option>
            ))}
          </select>

          {selectedService && (
            <>
              <p className="mt-4">Description: {selectedService.description}</p>
              <p>
                <strong>Price:</strong> € {price.toFixed(2)}
              </p>
            </>
          )}
        </div>
        <div className="card-actions justify-center mt-4">
          <button className="btn btn-primary" onClick={handlePurchase}>
            Buy Service
          </button>
        </div>
      </div>
    </div>
  );
};

export default BuyGymService;
