import React, { useEffect, useState } from 'react';
import './App.css';
import Sidebar from './components/Sidebar/Sidebar';

const App: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userName, setUserName] = useState<string | null>(null);

  useEffect(() => {
    // Fetch user data to check if the user is authenticated
    const fetchUserData = async () => {
      try {
        const response = await fetch('http://localhost:8080/auth/login/success', {
          credentials: 'include',
        });
        if (response.ok) {
          const data = await response.json();
          setIsAuthenticated(true);
          setUserName(data.user.name);  // Assuming 'name' is the attribute containing the user’s name
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
        setIsAuthenticated(false);
      }
    };

    fetchUserData();
  }, []);

  const handleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="app-container">
      <Sidebar userName={userName || "Guest"} />
      <div className="main-content">
        <h1>Welcome to FitEverywhere</h1>
        {!isAuthenticated ? (
          <button onClick={handleLogin}>Login with Google</button>
        ) : (
          <p>Hello, {userName}!</p>
        )}
      </div>
    </div>
  );
};

export default App;
