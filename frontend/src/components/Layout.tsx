import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar.tsx";
import { useAuth } from "../hooks/useAuth.ts";
import Navbar from "./Navbar.tsx";

const Layout = () => {
  const auth = useAuth();
  if (!auth || !auth.user) return <div>Not authorized</div>;

  return (
    <>
      <Sidebar />
      <main className="ml-64 h-screen bg-base-200">
        <Navbar />
        <main className="p-base bg-base-200">
          <Outlet />
        </main>
      </main>
    </>
  );
};
export default Layout;
