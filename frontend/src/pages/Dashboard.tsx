import React from 'react';
import { useLocation } from 'react-router-dom';
import Sidebar from '../components/Sidebar/Sidebar';

const Dashboard: React.FC = () => {
    const location = useLocation();
    const userNameFromState = location.state?.userName;
    const userName = userNameFromState || sessionStorage.getItem('user') || 'Guest';  // Fallback to sessionStorage

    console.log("Received userName in Dashboard:", userName);

    return (
        <div className="app-container">
            <Sidebar userName={userName} />
            <div className="main-content">
                <h1>Hello {userName}, Welcome to FitEverywhere</h1>
            </div>
        </div>
    );
};


export default Dashboard;
