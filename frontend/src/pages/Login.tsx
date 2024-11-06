import React, { useEffect, useState } from 'react';
import google_icon from "../assets/google_icon.png";
import logo from "../assets/logo.jpg";
import styles from "./Login.module.css";

const Login: React.FC = () => {

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
            setUserName(data.user.username);  // Assuming 'name' is the attribute containing the user’s name
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
            <div className={styles.sideInfo}>
                <div className={styles.headerDiv}>
                    <h1>FitEverywhere</h1>
                    <div className="logoContainer">
                        <img src={logo} alt="Logo" className={styles.logo} />
                    </div>
                </div>
                <p className={styles.infoText}>With FitEverywhere, maintaining a fitness routine becomes a rewarding part of the travel experience, making it easy to stay active and healthy—anytime, anywhere.</p>
            </div>
            <div className={styles.loginbox}>
                <h1>Login</h1>
                    <button>
                        <div onClick={handleLogin} className={styles.buttonContainer}>
                            Login with Google
                            <div className="googleIconContainer">
                                <img src={google_icon} alt="google" className={styles.googleIcon} />
                            </div>
                        </div>
                    </button>
            </div>
        </div>
    );
};

export default Login;
