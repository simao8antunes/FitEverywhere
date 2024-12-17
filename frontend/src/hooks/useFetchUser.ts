import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import type {
  PersonalTrainer,
  PTService,
  Purchase,
  UseFetchUserResult,
  UserOptions,
} from "../types";
const API_URL = import.meta.env.VITE_API_BASE_URL;
export function useFetchUser(): UseFetchUserResult {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<UserOptions | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  const fetchUsers = async () => {
    try {
      const response = await fetch(API_URL + "/auth/login/success", {
        credentials: "include",
      });
      console.log("Fetched user data:", response, location.pathname);
      if (!response.ok) {
        if (response.status === 404) {
          // User not found, redirect to role selection
          setError("User not found. Please ensure your account is registered.");
          setIsAuthenticated(true);
          return navigate("/select-role");
        } else if (response.status === 401) {
          // User not authenticated, redirect to login
          setError("User not authenticated. Please log in.");
          return navigate("/login");
        } else {
          setError("An unexpected error occurred.");
        }
        setIsAuthenticated(false);
        setUser(null);
      }

      const data = await response.json();
      console.log("Fetched user data:", data, location.pathname);
      setIsAuthenticated(true);
      data.user.userSpecs = data.userSpecs;
      setUser(data.user);
      sessionStorage.setItem("user", data.user.email);
      if (location.pathname === "/login") {
        navigate("/");
      }
    } catch (error) {
      console.error("Error fetching user data:", error);
      setIsAuthenticated(false);
      setUser(null);
      setError("Failed to authenticate");
      navigate("/login");
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [location.pathname]);

  const logout = async () => {
    return await fetch(API_URL + "/logout", {
      mode: "no-cors",
      method: "GET",
      credentials: "include",
    })
      .then(() => {
        console.log("Logged out successfully");
        setIsAuthenticated(false);
        setUser(null);
        sessionStorage.removeItem("user");
        navigate("/login");
      })
      .catch((error) => console.error("Error during logout:", error));
  };

  const updateUserData = async (data: UserOptions) => {
    try {
      const response = await fetch(API_URL + "/auth", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(data),
      });
      if (!response.ok) {
        throw new Error("Failed to update user data");
      }
      const updatedData = await response.json();
      console.log("User data updated:", updatedData);
      setUser(updatedData);
      return updatedData;
    } catch (error) {
      console.error("Error updating user data:", error);
    }
  };

  const addServiceToPersonalTrainer = async (service: PTService) => {
    console.log("Adding service to personal trainer:", service);
    try {
      const response = await fetch(API_URL + `/personal-trainer/add-service`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(service),
      });
      if (!response.ok) {
        throw new Error("Failed to add service to personal trainer");
      }
      alert("Service added or updated to personal trainer successfully");
      await fetchUsers();
    } catch (error) {
      console.error("Error adding service to personal trainer:", error);
    }
  };

  const purchaseGymMembership = async (purchase: Purchase) => {
    try {
      const response = await fetch(
        API_URL + `/client/${purchase.gym.id}/purchase?type=${purchase.type}`,
        {
          method: "POST",
          credentials: "include",
        },
      );
      if (!response.ok) {
        throw new Error("Failed to purchase gym membership");
      }
      const data = await response.json();
      console.log("Gym membership purchased:", data);
      alert("Gym membership purchased successfully");
      await fetchUsers();
    } catch (error) {
      console.error("Error purchasing gym membership:", error);
    }
  };

  const fetchPersonalTrainers = async () => {
    try {
      const response = await fetch(API_URL + "/personal-trainer", {
        credentials: "include",
      });
      if (!response.ok) {
        throw new Error("Failed to fetch personal trainers");
      }
      const data: PersonalTrainer[] = await response.json();
      console.log("Personal Trainers:", data);

      return data;
    } catch (error) {
      console.error("Error fetching personal trainers:", error);
    }
  };

  const purchasePTService = async (serviceId: number) => {
    try {
      const response = await fetch(
        API_URL + `/client/${serviceId}/purchase-service`,
        {
          method: "POST",
          credentials: "include",
        },
      );
      if (!response.ok) {
        console.log(response);
        throw new Error("Failed to purchase personal training service");
      }
      const data = await response.json();
      console.log("Personal Training Service purchased:", data);
      alert("Personal Training Service purchased successfully");
      await fetchUsers();
    } catch (error) {
      console.error("Error purchasing personal training service:", error);
    }
  };

  return {
    isAuthenticated,
    user,
    error,
    logout,
    fetchUsers,
    updateUserData,
    addServiceToPersonalTrainer,
    purchaseGymMembership,
    fetchPersonalTrainers,
    purchasePTService,
  };
}
