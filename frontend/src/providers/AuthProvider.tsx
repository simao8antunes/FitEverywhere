import { ReactNode } from "react";
import { useFetchUser } from "../hooks/useFetchUser.ts";
import { AuthContext } from "../hooks/useAuth.ts";
import type { UseFetchUserResult } from "../types.ts";

interface AuthProviderProps {
  children: ReactNode;
}

export default function AuthProvider({ children }: AuthProviderProps) {
  const authContext: UseFetchUserResult = useFetchUser();
  return (
    <AuthContext.Provider value={authContext}>{children}</AuthContext.Provider>
  );
}
