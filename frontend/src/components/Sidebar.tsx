import React from "react";
import logo from "../../public/logo.svg";
import { useAuth } from "../hooks/useAuth.ts";

interface SidebarProps {
  userName: string;
}

type SidebarStyles = {
  container: string;
  logoContainer: string;
  navItem: string;
  userInfo: string;
  logout: string;
};

const Sidebar: React.FC<SidebarProps> = ({ userName }) => {
  const { logout } = useAuth();

  const navItemStyles: SidebarStyles = {
    container:
      "w-64 flex flex-col h-[100vh] bg-background text-text p-base fixed",
    logoContainer: "flex justify-center mb-base",
    navItem:
      "flex items-center gap-base decoration-0 text-text py-s transition-all hover:bg-[#34495E] hover:pl-s active:bg-primary active:text-text",
    userInfo:
      "mt-auto text-s text-white border-t border-solid border-primary pt-s",
    logout:
      "bg-red-600 text-white border-none py-3 px-4 rounded-soft cursor-pointer transition-colors hover:bg-red-800 duration-300 ease-in-out",
  };

  return (
    <nav className={navItemStyles.container}>
      <div className={navItemStyles.logoContainer}>
        <img src={logo} alt="Logo" className="p-base" />
      </div>
      <div className="flex flex-col my-xxl">
        <a href="#home" className={navItemStyles.navItem}>
          🏠 Home
        </a>
        <a href="#gyms" className={navItemStyles.navItem}>
          🏋️ Gyms
        </a>
        <a href="#personal-trainers" className={navItemStyles.navItem}>
          🤸 Personal Trainers
        </a>
        <a href="#itinerary" className={navItemStyles.navItem}>
          🧳 Travel Itineraries
        </a>
        <a href="#messages" className={navItemStyles.navItem}>
          💬 Messages
        </a>
      </div>

      {/* User Information */}
      <div className={navItemStyles.userInfo}>
        <p>{userName}</p>
        <button onClick={logout} className={navItemStyles.logout}>
          Log Out
        </button>
      </div>
    </nav>
  );
};

export default Sidebar;
