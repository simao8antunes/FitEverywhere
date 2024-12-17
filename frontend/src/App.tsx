import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import "./index.css";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import Layout from "./components/Layout";
import SelectRole from "./pages/SelectRole";
import Profile from "./pages/Profile.tsx";
import GymProfile from "./pages/GymProfile.tsx";
import MyGyms from "./pages/MyGyms.tsx";
import { useAuth } from "./hooks/useAuth.ts";
import PersonalTrainerProfile from "./pages/PersonalTrainerProfile.tsx";
import PTServices from "./pages/PTServices.tsx";
import Purchases from "./pages/Purchases.tsx";
import MyPT from "./pages/MyPT.tsx";

const App: React.FC = () => {
  const { user, isAuthenticated } = useAuth();
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
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
                  <Route path="my-gyms" element={<MyGyms />} />
                </Route>
              )}

              {/* Client Routes */}
              {user.role === "client" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<Profile />} />
                  <Route path="purchases" element={<Purchases />} />
                  <Route path="my-pt" element={<MyPT />} />
                </Route>
              )}

              {/* Personal Trainer Routes */}
              {user.role === "personal_trainer" && (
                <Route path="/" element={<Layout />}>
                  <Route index element={<Dashboard />} />
                  <Route path="profile" element={<PersonalTrainerProfile />} />
                  <Route path="my-services" element={<PTServices />} />
                </Route>
              )}
            </>
          ) : (
            <>
              <Route path="select-role" element={<SelectRole />} />
              <Route
                path="*"
                element={<Navigate to="/select-role" replace />}
              />
            </>
          )}
        </>
      ) : (
        <>
          <Route path="/" element={<Dashboard />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </>
      )}
    </Routes>
  );
};

export default App;
