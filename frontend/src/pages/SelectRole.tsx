import React, { useState } from "react";
import { FaDumbbell, FaUser } from "react-icons/fa";
import { GiMuscleUp } from "react-icons/gi";
import { useLocation, useNavigate } from "react-router-dom";
const API_URL = import.meta.env.VITE_API_BASE_URL as string;
const SelectRole: React.FC = () => {
  const [role, setRole] = useState<string>("");
  const navigate = useNavigate();
  const location = useLocation();
  const userNameFromState = location.state?.userName || "Guest";

  const handleRoleSelection = async () => {
    try {
      const response = await fetch(API_URL + `/auth/signup?role=${role}`, {
        method: "POST",
        credentials: "include",
      });

      if (response.ok) {
        const data = await response.json();
        console.log("User created successfully:", data.message);
        navigate("/", { state: { userName: userNameFromState, role } });
      } else {
        const errorData = await response.json();

        if (response.status === 400) {
          // Bad request, likely an invalid role
          console.error(
            "Invalid role:",
            errorData.error || "Invalid role provided.",
          );
          alert("Please select a valid role.");
        } else if (response.status === 401) {
          // Unauthorized, user not authenticated
          console.error(
            "User not authenticated:",
            errorData.error || "Authentication required.",
          );
          alert("You need to be logged in to select a role.");
          navigate("/login");
        } else {
          // Other unexpected errors
          console.error(
            "Failed to update role:",
            errorData.error || response.statusText,
          );
          alert("An error occurred while updating the role. Please try again.");
        }
      }
    } catch (error) {
      console.error("Error updating role:", error);
      alert(
        "An unexpected error occurred. Please check your connection and try again.",
      );
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <h2 className="text-h2 mb-10 font-semibold">
        Hello, {userNameFromState}! Please select your role:
      </h2>

      <div className="flex space-x-6 mb-12">
        {/* Gym Card */}
        <div
          onClick={() => setRole("gym")}
          className={`cursor-pointer card w-56 bg-white text-center transition-transform transform hover:scale-105 ${
            role === "gym"
              ? "border-2 border-primary"
              : "border border-gray-300"
          }`}
        >
          <div className="card-body">
            <FaDumbbell
              className="mx-auto mb-4 text-primary"
              style={{ fontSize: "36px" }}
            />
            <h3 className="card-title self-center  text-gray-800">Gym</h3>
            <button
              data-testid={"gym-button"}
              className={`px-4 py-2 rounded-full font-medium transition-all ${
                role === "gym"
                  ? "bg-primary text-gray-800"
                  : "bg-gray-200 text-gray-600"
              } hover:bg-primary hover:text-gray-800`}
            >
              Select
            </button>
          </div>
        </div>

        {/* Personal Trainer Card */}
        <div
          onClick={() => setRole("pt")}
          className={`cursor-pointer card w-56 bg-white text-center transition-transform transform hover:scale-105 ${
            role === "pt" ? "border-2 border-primary" : "border border-gray-300"
          }`}
        >
          <div className="card-body">
            <GiMuscleUp
              className="mx-auto mb-4 text-primary"
              style={{ fontSize: "36px" }}
            />
            <h3 className="text-lg font-semibold text-gray-800 mb-3">
              Personal Trainer
            </h3>
            <button
              className={`px-4 py-2 rounded-full font-medium transition-all ${
                role === "pt"
                  ? "bg-primary text-gray-800"
                  : "bg-gray-200 text-gray-600"
              } hover:bg-primary hover:text-gray-800`}
            >
              Select
            </button>
          </div>
        </div>

        {/* Client Card */}
        <div
          onClick={() => setRole("client")}
          className={`cursor-pointer card w-56 bg-white text-center transition-transform transform hover:scale-105 ${
            role === "client"
              ? "border-2 border-primary"
              : "border border-gray-300"
          }`}
        >
          <div className="card-body">
            <FaUser
              className="mx-auto mb-4 text-primary"
              style={{ fontSize: "36px" }}
            />
            <h3 className="text-lg font-semibold text-gray-800 mb-3">Client</h3>
            <button
              data-testid={"client-button"}
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
