import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar.tsx";
import { useAuth } from "../hooks/useAuth.ts";

const Layout = () => {
  const auth = useAuth();
  if (!auth || !auth.user) return <div>Not authorized</div>;

  return (
    <>
      <Sidebar userName={auth.user!.username} />
      <main className="p-base ml-64">
        <Outlet />
      </main>
    </>
  );
};
export default Layout;
