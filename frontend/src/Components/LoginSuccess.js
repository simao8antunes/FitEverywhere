import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginSuccess = () => {
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/auth/login/success', {
            credentials: 'include',
        })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            console.log(data);
            // Handle user data (e.g., store it in context or state)
            navigate('/'); // Redirect to home or another route
        })
        .catch(error => console.error('Error fetching user data:', error));
    }, [navigate]);

    return <div>Logging you in...</div>;
};

export default LoginSuccess;
