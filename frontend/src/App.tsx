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
  const hasRole = Boolean(user?.role);
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="login" element={<Login />} />
      <Route
        path="select-role"
        element={hasRole ? <Navigate to="/" replace /> : <SelectRole />}
      />

      {/* Protected Routes */}
      {isAuthenticated && (
        <>
          {/* Gym Manager Routes */}
          {user?.role === "gym_manager" && (
            <Route path="/" element={<Layout />}>
              <Route index element={<Dashboard />} />
              <Route path="profile" element={<GymProfile />} />
            </Route>
          )}

          {/* Client Routes */}
          {user?.role === "client" && (
            <Route path="/" element={<Layout />}>
              <Route index element={<Dashboard />} />
              <Route path="profile" element={<Profile />} />
            </Route>
          )}

          {/* Personal Trainer Routes */}
          {user?.role === "personal_trainer" && (
            <Route path="/" element={<Layout />}>
              <Route index element={<Dashboard />} />
              <Route path="profile" element={<Profile />} />
            </Route>
          )}

          {/* Fallback to Select Role if the role is undefined */}
          {!user?.role ? (
            <Route path="*" element={<Navigate to="/select-role" replace />} />
          ) : (
            <Route path="*" element={<Navigate to="/" replace />} />
          )}
        </>
      )}
    </Routes>
  );
};

export default App;
