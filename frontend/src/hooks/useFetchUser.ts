import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import type { UseFetchUserResult, UserOptions } from "../types";

export function useFetchUser(): UseFetchUserResult {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<UserOptions | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  const fetchUsers = async () => {
    try {
      const response = await fetch("/api/auth/login/success", {
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
        } else {
          setError("An unexpected error occurred.");
        }
        setIsAuthenticated(false);
        setUser(null);
        return navigate("/login");
      }

      const data = await response.json();
      console.log("Fetched user data:", data, location.pathname);
      setIsAuthenticated(true);
      setUser(data.user);
      sessionStorage.setItem("user", data.user.email);
    } catch (error) {
      console.error("Error fetching user data:", error);
      setIsAuthenticated(false);
      setUser(null);
      setError("Failed to authenticate");
      navigate("/login");
    }
  };

  useEffect(() => {
    if (location.pathname !== "/login") {
      fetchUsers();
    }
  }, [location.pathname]);

  const logout = async () => {
    return await fetch("/api/logout", {
      method: "GET",
      credentials: "include",
    })
      .then((response) => {
        if (response.status === 200) {
          console.log("Logged out successfully");
          setIsAuthenticated(false);
          setUser(null);
          sessionStorage.removeItem("user");
          navigate("/login");
        } else {
          console.error("Failed to logout");
        }
      })
      .catch((error) => console.error("Error during logout:", error));
  };

  return { isAuthenticated, user, error, logout };
}
