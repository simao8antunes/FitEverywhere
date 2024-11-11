import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import google_icon from "../assets/google_icon.png";
import logo from "../assets/logo.svg";
import styles from "./Login.module.css";

const Login: React.FC = () => {
  const [_isAuthenticated, setIsAuthenticated] = useState(false);
  const [_userName, setUserName] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch("/api/auth/login/success", {
          credentials: "include",
        });
        if (response.ok) {
          const data = await response.json();
          console.log("Fetched user data:", data); // Log response data
          setIsAuthenticated(true);
          setUserName(data.user.username);
          sessionStorage.setItem("user", JSON.stringify(data.user)); // Store user info in sessionStorage
          if (!data.user.role) {
            navigate("/select-role");
          } else {
            navigate("/dashboard", { state: { userName: data.user.username } });
          }
        } else {
          console.log("Failed to authenticate");
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
        setIsAuthenticated(false);
      }
    };

    fetchUserData();
  }, [navigate]);

  const handleLogin = () => {
    window.location.href = "/api/oauth2/authorization/google";
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
        <p className={styles.infoText}>
          With FitEverywhere, maintaining a fitness routine becomes a rewarding
          part of the travel experience, making it easy to stay active and
          healthy—anytime, anywhere.
        </p>
        <p>{_isAuthenticated ? "Autenticado" : "Nao Autenticado"}</p>
        <p>{_userName}</p>
      </div>
      <div className={styles.loginbox}>
        <h1>Login</h1>
        <button onClick={handleLogin} className={styles.buttonContainer}>
          Login with Google
          <img src={google_icon} alt="google" className={styles.googleIcon} />
        </button>
      </div>
    </div>
  );
};

export default Login;
