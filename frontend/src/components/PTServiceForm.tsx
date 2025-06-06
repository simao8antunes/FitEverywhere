import React, { useEffect, useState } from "react";
import type { PTService } from "../types.ts";
import { useAuth } from "../hooks/useAuth.ts";

interface PTServiceFormProps {
  selectedService: PTService | null;
  setSelectedService: (service: PTService | null) => void;
}

const newPTService: PTService = {
  id: 0,
  name: "",
  description: "",
  price: 0,
  duration: 0,
  type: "",
};

const PTServiceForm: React.FC<PTServiceFormProps> = ({
  selectedService,
  setSelectedService,
}) => {
  const [formData, setFormData] = useState<PTService>(newPTService);
  const { addServiceToPersonalTrainer } = useAuth();

  useEffect(() => {
    if (selectedService) {
      setFormData(selectedService);
    } else {
      setFormData(newPTService);
    }
  }, [selectedService]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "price" || name === "duration" ? parseFloat(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Updated Service Data:", formData);
    addServiceToPersonalTrainer(formData);
    setFormData(newPTService);
    setSelectedService(null);
  };

  return (
    <div className="p-base">
      <h2 className="text-2xl font-semibold">
        {selectedService ? "Edit Service" : "Add New Service"}
      </h2>
      <form onSubmit={handleSubmit} className="mt-4">
        <div className="mb-4">
          <label htmlFor="name" className="block font-bold mb-1">
            Service Name:
          </label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            className="input input-bordered w-full"
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="description" className="block font-bold mb-1">
            Description:
          </label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            className="textarea textarea-bordered w-full"
            rows={4}
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="price" className="block font-bold mb-1">
            Price (€):
          </label>
          <input
            type="number"
            id="price"
            name="price"
            value={formData.price}
            onChange={handleInputChange}
            className="input input-bordered w-full"
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="duration" className="block font-bold mb-1">
            Duration (minutes):
          </label>
          <input
            type="number"
            id="duration"
            name="duration"
            value={formData.duration}
            onChange={handleInputChange}
            className="input input-bordered w-full"
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="type" className="block font-bold mb-1">
            Type:
          </label>
          <input
            type="text"
            id="type"
            name="type"
            value={formData.type}
            onChange={handleInputChange}
            className="input input-bordered w-full"
            required
          />
        </div>
        <div className="card-actions justify-center">
          <button type="submit" className="btn btn-primary">
            Save Service
          </button>
        </div>
      </form>
    </div>
  );
};

export default PTServiceForm;
