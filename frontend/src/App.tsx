import React from 'react';
import Sidebar from './components/Sidebar/Sidebar';
import './App.css';

const App: React.FC = () => {
  return (
    <div className="app-container">
      <Sidebar
        userName="Salif Faustino"
      />
      <div className="main-content">
        <h1>Welcome to FitEverywhere</h1>
      </div>
    </div>
  );
};

export default App;
