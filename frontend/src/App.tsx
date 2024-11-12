import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import "./index.css";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import SelectRole from "./pages/SelectRole";

const App: React.FC = () => {
  return (
    <div className="app-container">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/select-role" element={<SelectRole />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default App;
