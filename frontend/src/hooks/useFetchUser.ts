import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import type { UseFetchUserResult, User } from "../types";

export function useFetchUser(): UseFetchUserResult {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<User | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  if (location.pathname === "/login" && user) navigate("/");

  const fetchUsers = async () => {
    return await fetch("/api/auth/login/success", {
      credentials: "include",
    });
  };

  useEffect(() => {
    fetchUsers()
      .then((response) => response.json())
      .then((data: { user: User }) => {
        console.log("Fetched user data:", data);

        setIsAuthenticated(true);
        setUser(data.user);

        sessionStorage.setItem("user", JSON.stringify(data.user));
        console.log(user);
        if (!data.user.role) {
          navigate("/select-role");
        }
      })
      .catch((err) => {
        setIsAuthenticated(false);
        setUser(null);
        setError("Failed to authenticate");
        navigate("/login");
        throw new Error(`Error fetching user data: ${err}`);
      });
  }, [navigate]);

  const logout = async () => {
    return await fetch("/api/auth/logout", {
      method: "GET",
      credentials: "include",
    })
      .then((response) => {
        if (response.status === 200) {
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
