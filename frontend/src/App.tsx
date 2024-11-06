
import './App.css';
//import Sidebar from './components/Sidebar/Sidebar';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';

const App: React.FC = () => {
  return (
    <div className="app-container">
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/login" element={<Login />} />
          </Routes>
      </BrowserRouter>

    
    </div>
  );
};

export default App;
