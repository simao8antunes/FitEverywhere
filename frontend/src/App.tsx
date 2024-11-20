import React from "react";
import { Route, Routes } from "react-router-dom";
import "./index.css";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import Layout from "./components/Layout";
import SelectRole from "./pages/SelectRole";
import Profile from "./pages/Profile.tsx";
import GymProfile from "./pages/GymProfile.tsx";
import { useAuth } from "./hooks/useAuth.ts";

const App: React.FC = () => {
  const { user } = useAuth();
  return (
    <Routes>
      <Route path="login" element={<Login />} />
      <Route path="select-role" element={<SelectRole />} />
      {user?.role === "gym" && (
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="profile" element={<GymProfile />} />
        </Route>
      )}
      {user?.role === "client" && (
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="profile" element={<Profile />} />
        </Route>
      )}
      {user?.role === "pt" && (
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="profile" element={<Profile />} />
        </Route>
      )}
    </Routes>
  );
};

export default App;
