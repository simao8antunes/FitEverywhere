import { createContext, useContext } from "react";
import type { UseFetchUserResult } from "../types.ts";

export const AuthContext = createContext<UseFetchUserResult | null>(null);

// Custom hook to use the authentication context
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
