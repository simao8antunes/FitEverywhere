import React from 'react';
import styles from "./Login.module.css";
import logo from "../assets/logo.jpg";
import google_icon from "../assets/google_icon.png";

const Login: React.FC = () => {
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
                        <div className={styles.buttonContainer}>
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
