import React from "react";
import { Route, Routes, Navigate } from "react-router-dom";
import "./index.css";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import Layout from "./components/Layout";
import SelectRole from "./pages/SelectRole";
import Profile from "./pages/Profile.tsx";
import GymProfile from "./pages/GymProfile.tsx";
import { useAuth } from "./hooks/useAuth.ts";

const App: React.FC = () => {
  const { user, isAuthenticated } = useAuth();
  return (
    <Routes>
      <Route
        path="select-role"
        element={user ? <Navigate to="/" replace /> : <SelectRole />}
      />

      {/* Protected Routes */}
      {isAuthenticated ? (
        <>
          {/* Check if user exists */}
          {user ? (
            <>
              {/* Gym Manager Routes */}
              {user.role === "gym_manager" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<GymProfile />} />
                </Route>
              )}

              {/* Client Routes */}
              {user.role === "client" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<Profile />} />
                </Route>
              )}

              {/* Personal Trainer Routes */}
              {user.role === "personal_trainer" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<Profile />} />
                </Route>
              )}
            </>
          ) : (
            <Route path="*" element={<Navigate to="/select-role" replace />} />
          )}
        </>
      ) : (
        <Route path="login" element={<Login />} />
      )}
    </Routes>
  );
};

export default App;
