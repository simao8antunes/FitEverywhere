import React from "react";
import logo from "/logo.svg";
import { CgGym } from "react-icons/cg";
import { FiHome, FiGrid } from "react-icons/fi";
import { NavLink } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.ts";

const Sidebar: React.FC = () => {
  const { user } = useAuth();
  const menuItems = [
    { name: "Home", icon: <FiHome />, link: "/" },
    { name: "Workouts", icon: <FiGrid />, link: "/workouts", role: "client" },
    { name: "My Gyms", icon: <CgGym />, link: "/my-gyms", role: "gym_manager" },
  ];

  return (
    <nav className="w-64 flex flex-col h-screen bg-base-100 text-text p-base fixed">
      <div className="flex justify-center mb-base">
        <img src={logo} alt="Logo" id="logo" className="p-base" />
      </div>
      <ul className="space-y-m mt-base">
        {menuItems.map(
          (item, index) =>
            ((item.role && item.role === user?.role) || !item.role) && (
              <li key={index}>
                <NavLink
                  to={item.link}
                  end
                  className={({ isActive }) =>
                    ` btn w-full justify-start transition-colors duration-200 ease-in-out ${
                      isActive ? "btn-primary" : "hover:btn-neutral"
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
            ),
        )}
      </ul>
    </nav>
  );
};

export default Sidebar;
