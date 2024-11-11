import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/logo.svg";
import styles from "./Sidebar.module.css";

interface SidebarProps {
  userName: string;
}

const Sidebar: React.FC<SidebarProps> = ({ userName }) => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      const response = await fetch("/api/auth/logout", {
        method: "GET",
        credentials: "include", // Ensures cookies are sent
      });
      if (response.ok) {
        sessionStorage.removeItem("user"); // Clear user info from session storage
        navigate("/"); // Redirect to login page
      } else {
        console.log("Failed to logout");
      }
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  return (
    <div className={styles.sidebar}>
      <div className={styles.logoContainer}>
        <img src={logo} alt="Logo" className={styles.logo} />
      </div>

      {/* Navigation Links */}
      <nav className={styles.navLinks}>
        <a href="#home" className={styles.navItem}>
          🏠 Home
        </a>
        <a href="#gyms" className={styles.navItem}>
          🏋️ Gyms
        </a>
        <a href="#personal-trainers" className={styles.navItem}>
          🤸 Personal Trainers
        </a>
        <a href="#itinerary" className={styles.navItem}>
          🧳 Travel Itineraries
        </a>
        <a href="#messages" className={styles.navItem}>
          💬 Messages
        </a>
      </nav>

      {/* User Information */}
      <div className={styles.userInfo}>
        <p className={styles.userName}>{userName}</p>
        <button onClick={handleLogout} className={styles.logout}>
          Log Out
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
