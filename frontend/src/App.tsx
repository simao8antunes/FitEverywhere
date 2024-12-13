import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import { useAuth } from "./hooks/useAuth.ts";
import "./index.css";
import Dashboard from "./pages/Dashboard";
import GymProfile from "./pages/GymProfile.tsx";
import Login from "./pages/Login";
import PersonalTrainerProfile from "./pages/PersonalTrainerProfile.tsx";
import Profile from "./pages/Profile.tsx";
import SelectRole from "./pages/SelectRole";
import Workouts from "./pages/Workouts.tsx";

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
                  <Route path="workouts" element={<Workouts />} />
                </Route>
              )}

              {/* Personal Trainer Routes */}
              {user.role === "personal_trainer" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<PersonalTrainerProfile />} />
                </Route>
              )}
            </>
          ) : (
            <>
              <Route
                path="*"
                element={<Navigate to="/select-role" replace />}
              />
              <Route path="/" element={<Dashboard />} />
            </>
          )}
        </>
      ) : (
        <>
          <Route path="login" element={<Login />} />
          <Route path="/" element={<Dashboard />} />
        </>
      )}
      <Route path="*" element={<Navigate to="/select-role" replace />} />
    </Routes>
  );
};

export default App;
