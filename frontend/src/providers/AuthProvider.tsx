import {
  useState,
  useEffect,
  useContext,
  createContext,
  ReactNode,
} from "react";
import { useNavigate } from "react-router-dom";

// Define the user data structure returned from the backend
interface User {
  username: string;
  role?: string;
}

// Define the shape of the authentication context
interface AuthContextType {
  isAuthenticated: boolean;
  user: User | null;
  login: () => Promise<void>;
  logout: () => void;
}

// Initialize the context with a type, null by default
const AuthContext = createContext<AuthContextType | null>(null);

// Custom hook to use the authentication context
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Props type for the AuthProvider component
interface AuthProviderProps {
  children: ReactNode;
}

// Example AuthProvider component
export default function AuthProvider({ children }: AuthProviderProps) {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<User | null>(null);
  const navigate = useNavigate();

  // Function to check if the user is logged in
  const fetchUserData = async () => {
    try {
      const response = await fetch("/api/auth/login/success", {
        credentials: "include",
      });

      if (response.ok) {
        const data: { user: User } = await response.json();
        console.log("Fetched user data:", data);

        setIsAuthenticated(true);
        setUser(data.user);

        // Store user info in sessionStorage
        sessionStorage.setItem("user", JSON.stringify(data.user));

        if (!data.user.role) {
          navigate("/select-role");
        } else {
          navigate("/dashboard", { state: { userName: data.user.username } });
        }
      } else {
        console.log("Failed to authenticate");
        setIsAuthenticated(false);
        setUser(null);
      }
    } catch (error) {
      console.error("Error fetching user data:", error);
      setIsAuthenticated(false);
      setUser(null);
    }
  };

  // Function to log out the user
  const logout = () => {
    setIsAuthenticated(false);
    setUser(null);
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  // Run fetchUserData on mount to check if the user is logged in
  useEffect(() => {
    fetchUserData();
  }, []);

  // Context value to be provided to other components
  const authContextValue: AuthContextType = {
    isAuthenticated,
    user,
    login: fetchUserData,
    logout,
  };

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  );
}
