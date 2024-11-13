import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { FaDumbbell, FaUser } from "react-icons/fa";

const SelectRole: React.FC = () => {
  const [role, setRole] = useState<string>("");
  const navigate = useNavigate();
  const location = useLocation();
  const userNameFromState = location.state?.userName || "Guest";

  const handleRoleSelection = async () => {
    if (role) {
      navigate("/dashboard", { state: { userName: userNameFromState, role } });
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-b from-gray-100 via-gray-200 to-gray-300 p-6">
      <h2 className="text-h2 text-gray-700 mb-10 font-semibold">
        Hello, {userNameFromState}! Please select your role:
      </h2>

      <div className="flex space-x-6 mb-12">
        {/* Gym Card */}
        <div
          onClick={() => setRole("gym")}
          className={`cursor-pointer bg-white shadow-lg rounded-lg p-8 w-48 text-center transition-transform transform hover:scale-105 ${
            role === "gym"
              ? "border-2 border-primary"
              : "border border-gray-300"
          }`}
        >
          <FaDumbbell
            className="mx-auto mb-4 text-primary"
            style={{ fontSize: "36px" }}
          />
          <h3 className="text-lg font-semibold text-gray-800 mb-3">Gym</h3>
          <button
            className={`px-4 py-2 rounded-full font-medium transition-all ${
              role === "gym"
                ? "bg-primary text-gray-800"
                : "bg-gray-200 text-gray-600"
            } hover:bg-primary hover:text-gray-800`}
          >
            Select
          </button>
        </div>

        {/* Client Card */}
        <div
          onClick={() => setRole("client")}
          className={`cursor-pointer bg-white shadow-lg rounded-lg p-8 w-48 text-center transition-transform transform hover:scale-105 ${
            role === "client"
              ? "border-2 border-primary"
              : "border border-gray-300"
          }`}
        >
          <FaUser
            className="mx-auto mb-4 text-primary"
            style={{ fontSize: "36px" }}
          />
          <h3 className="text-lg font-semibold text-gray-800 mb-3">Client</h3>
          <button
            className={`px-4 py-2 rounded-full font-medium transition-all ${
              role === "client"
                ? "bg-primary text-gray-800"
                : "bg-gray-200 text-gray-600"
            } hover:bg-primary hover:text-gray-800`}
          >
            Select
          </button>
        </div>
      </div>

      {/* Confirm Button */}
      <button
        onClick={handleRoleSelection}
        disabled={!role}
        className={`px-6 py-3 rounded-full font-semibold transition-transform transform hover:scale-105 ${
          role
            ? "bg-primary text-gray-900 hover:bg-green-400"
            : "bg-gray-300 text-gray-500 cursor-not-allowed"
        }`}
      >
        Continue
      </button>
    </div>
  );
};

export default SelectRole;
