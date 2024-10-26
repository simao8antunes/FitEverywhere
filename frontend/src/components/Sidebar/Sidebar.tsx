import React from "react";
import styles from "./Sidebar.module.css";
import logo from "../../assets/logo.jpg";

interface SidebarProps {
  userName: string;
}

const Sidebar: React.FC<SidebarProps> = ({ userName }) => {
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
        <button className={styles.logout}>Sair</button>
      </div>
    </div>
  );
};

export default Sidebar;
