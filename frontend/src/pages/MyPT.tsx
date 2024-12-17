import React from "react";
import { useAuth } from "../hooks/useAuth.ts";
import { type Client, type UserOptions } from "../types";

const MyPT: React.FC = () => {
  const { user } = useAuth();

  // Type guard to check if the user is a client
  const isClient = (user: UserOptions | null): user is Client => {
    return user?.role === "client";
  };

  if (!isClient(user)) {
    return (
      <div className="alert alert-warning shadow-lg mt-5">
        <div>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="stroke-current flex-shrink-0 h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M13 16h-1v-4h-1m1-4h.01M12 20.5c4.418 0 8-3.582 8-8s-3.582-8-8-8-8 3.582-8 8 3.582 8 8 8z"
            />
          </svg>
          <span>Only clients can view their personal trainers.</span>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto mt-8">
      <h1 className="text-4xl font-bold text-center mb-6">My PTs</h1>
      <p className="text-lg text-center text-gray-600 mb-8">
        Here you can view your personal trainer's profile and your purchased
        services.
      </p>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {user.ptServices?.map((service) => (
          <div
            key={service.personalTrainer?.email}
            className="card w-full bg-base-200 shadow-xl"
          >
            <div className="card-body">
              <h2 className="card-title text-primary">
                {service.personalTrainer?.username}
              </h2>
              <p className="text-sm text-gray-500">
                {service.personalTrainer?.description ||
                  "No description available."}
              </p>

              <div className="mt-4">
                <p className="font-semibold">Service Details:</p>
                <ul className="list-disc ml-5">
                  <li>
                    <strong>Service Name:</strong> {service.name}
                  </li>
                  <li>
                    <strong>Duration:</strong> {service.duration} minutes
                  </li>
                  <li>
                    <strong>Price:</strong> €{service.price.toFixed(2)}
                  </li>
                </ul>
              </div>

              <div className="card-actions mt-4">
                <button className="btn btn-secondary btn-sm">
                  View Profile
                </button>
                <button className="btn btn-primary btn-sm">
                  Schedule Appointment
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {user.ptServices?.length === 0 && (
        <div className="alert alert-info mt-6">
          <div>
            <span>
              You have not purchased any personal trainer services yet.
            </span>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyPT;
