import { useAuth } from "../hooks/useAuth.ts";
import { PersonalTrainer, UserOptions } from "../types.ts";
import type { PTService } from "../types.ts";
import { useState } from "react";
import PTServiceForm from "../components/PTServiceForm.tsx";

const MyServices = () => {
  const { user } = useAuth();
  const [selectedService, setSelectedService] = useState<PTService | null>(
    null,
  );

  // Type guard to check if the user is a personal trainer
  const isPersonalTrainer = (
    user: UserOptions | null,
  ): user is PersonalTrainer => {
    return user?.role === "personal_trainer";
  };

  return (
    <div className="p-base">
      <h1 className="text-3xl font-bold">My Services</h1>
      <p className="text-text opacity-30">
        Here you can see all the services you offer as a personal trainer.
      </p>
      <div className="p-3 flex gap-base flex-wrap">
        {isPersonalTrainer(user) &&
          user.services?.map((service) => (
            <div
              key={service.id}
              className={`card bg-base-100 w-96 shadow-xl cursor-pointer ${service.id === selectedService?.id ? "border-2 border-primary" : ""}`}
              onClick={() => setSelectedService(service)}
            >
              <div className="card-body">
                <h2 className="card-title text-2xl">{service.name}</h2>
                <p>
                  <b>{service.description}</b>
                </p>
                <p>
                  <b>Price:</b> €{service.price.toFixed(2)}
                </p>
                <p>
                  <b>Duration:</b> {service.duration} minutes
                </p>
                <p>
                  <b>Type:</b> {service.type}
                </p>
              </div>
            </div>
          ))}
        <div>
          {selectedService && (
            <button
              className="btn btn-primary"
              onClick={() => setSelectedService(null)}
            >
              + Add Service
            </button>
          )}
        </div>
      </div>
      <PTServiceForm
        selectedService={selectedService}
        setSelectedService={setSelectedService}
      />
    </div>
  );
};

export default MyServices;
