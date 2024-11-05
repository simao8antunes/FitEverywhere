import React from 'react';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';

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
