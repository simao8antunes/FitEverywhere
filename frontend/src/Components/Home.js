import React from 'react';

const Home = () => {
    const handleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google'; // Adjust as needed
    };

    return (
        <div>
            <h1>Welcome to FitEverywhere</h1>
            <button onClick={handleLogin}>Login with Google</button>
        </div>
    );
};

export default Home;
