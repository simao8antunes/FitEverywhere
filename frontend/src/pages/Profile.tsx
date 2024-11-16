import React from "react";
import { useAuth } from "../hooks/useAuth.ts";
import { FiUser } from "react-icons/fi";

const Profile: React.FC = () => {
  // Assume user object is provided from your authentication context
  const { user } = useAuth();

  // Destructure user information for convenience
  const { username, email, id, role } = user || {};

  return (
    <div className="flex justify-center items-center w-max ">
      <div className="bg-background rounded-lg shadow-lg p-8 max-w-md w-full mx-4">
        {/* Logo */}
        <div className="flex justify-center mb-6">
          <FiUser className="w-20 h-20 " />
        </div>

        {/* User Details */}
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
            <span className="font-bold">User ID:</span> {id || "N/A"}
          </div>
          <div>
            <span className="font-bold">Role:</span> {role || "N/A"}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
