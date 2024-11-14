import { ReactNode } from "react";
import { useFetchUser } from "../hooks/useFetchUser.ts";
import { AuthContext } from "../hooks/useAuth.ts";

interface AuthProviderProps {
  children: ReactNode;
}

export default function AuthProvider({ children }: AuthProviderProps) {
  const authContext = useFetchUser();

  return (
    <AuthContext.Provider value={authContext}>{children}</AuthContext.Provider>
  );
}
