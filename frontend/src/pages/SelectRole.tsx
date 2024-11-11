import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const SelectRole: React.FC = () => {
  const [role, setRole] = useState<string>("");
  const navigate = useNavigate();
  const location = useLocation();
  const userNameFromState = location.state?.userName;

  let sessionUser = null;
  const sessionUserString = sessionStorage.getItem("user");
  if (sessionUserString) {
    try {
      sessionUser = JSON.parse(sessionUserString);
    } catch (error) {
      console.error("Failed to parse session user:", error);
    }
  }

  const _userName = userNameFromState || sessionUser?.username || "Guest";

  const handleRoleSelection = async () => {
    try {
      const response = await fetch(`/api/auth/role?role=${role}`, {
        method: "PUT",
        credentials: "include",
      });

      if (response.ok) {
        navigate("/dashboard", { state: { userName: _userName, role } });
      } else {
        const errorData = await response.json();
        console.error(
          "Failed to update role:",
          errorData.message || response.statusText
        );
      }
    } catch (error) {
      console.error("Error updating role:", error);
    }
  };

  return (
    <div className="role-selection">
      <h2>Hello, {_userName}! Please select your role:</h2>
      <button onClick={() => setRole("gym")}>Gym</button>
      <button onClick={() => setRole("client")}>Client</button>
      <button onClick={handleRoleSelection} disabled={!role}>
        Confirm
      </button>
    </div>
  );
};

export default SelectRole;
