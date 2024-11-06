import React from 'react';
import Sidebar from '.././components/Sidebar/Sidebar';

const Dashboard: React.FC = () => {
    return (
        <div className="app-container">
            <Sidebar userName="Guest" />
            <div className="main-content">
                <h1>Welcome to FitEverywhere</h1>
            </div>
        </div>
    );
};

export default Dashboard;
