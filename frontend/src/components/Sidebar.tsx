import React from "react";
import logo from "/logo.svg";
import { useAuth } from "../hooks/useAuth.ts";
import { FiHome, FiGrid, FiUser } from "react-icons/fi";
import { NavLink } from "react-router-dom";

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

  const menuItems = [
    { name: "Home", icon: <FiHome />, link: "/" },
    { name: "Workout Itineraries", icon: <FiGrid />, link: "/itineraries" },
  ];

  const navItemStyles: SidebarStyles = {
    container:
      "w-64 flex flex-col h-screen bg-background text-text p-base fixed",
    logoContainer: "flex justify-center mb-base",
    navItem:
      "flex items-center rounded gap-base decoration-0 text-text py-s transition-all hover:bg-[#34495E] hover:pl-s active:bg-primary active:text-text active:bg-background",
    userInfo:
      "mt-auto text-s text-white border-t border-solid border-primary pt-s",
    logout:
      " text-red-600 border-none w-full py-2 rounded-soft cursor-pointer transition-colors hover:text-secondary duration-300 ease-in-out",
  };

  return (
    <nav className={navItemStyles.container}>
      <div className={navItemStyles.logoContainer}>
        <img src={logo} alt="Logo" id="logo" className="p-base" />
      </div>
      <ul className="space-y-m mt-base">
        {menuItems.map((item, index) => (
          <li key={index}>
            <NavLink
              to={item.link}
              end
              className={({ isActive }) =>
                `w-full flex items-center py-s px-base rounded-soft transition-colors duration-200 ease-in-out ${
                  isActive ? "bg-primary text-white" : "hover:bg-secondary"
                }`
              }
            >
              <div
                className={`text-xl transition-transform duration-200 ease-in-out`}
              >
                {item.icon}
              </div>

              <span
                className={`ml-base whitespace-nowrap transform transition-transform duration-300`}
              >
                {item.name}
              </span>
            </NavLink>
          </li>
        ))}
      </ul>

      {/* User Information */}
      <div className={navItemStyles.userInfo}>
        <NavLink to="/profile" className={navItemStyles.navItem}>
          <div
            className={`text-xl transition-transform duration-200 ease-in-out`}
          >
            <FiUser />
          </div>

          <span
            className={`ml-base whitespace-nowrap transform transition-transform duration-300`}
          >
            {userName}
          </span>
        </NavLink>
        <button onClick={logout} className={navItemStyles.logout}>
          Log Out
        </button>
      </div>
    </nav>
  );
};

export default Sidebar;
