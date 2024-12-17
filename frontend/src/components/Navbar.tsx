import { NavLink } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.ts";

const Navbar = () => {
  const { logout, user } = useAuth();

  return (
    <div className="navbar bg-base-100">
      <div className="flex-1">
        <a className="btn btn-ghost text-xl">FitEverywhere</a>
      </div>
      <div className="flex-none gap-2 ">
        <div className="form-control">
          <label>{user?.username}</label>
        </div>
        <div className="dropdown dropdown-end">
          <div
            tabIndex={0}
            role="button"
            className="btn btn-ghost btn-circle avatar"
          >
            <div className="w-10 rounded-full">
              <img
                alt="Tailwind CSS Navbar component"
                src={user?.userSpecs?.picture}
              />
            </div>
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"
          >
            <li>
              <NavLink to="/profile" className="justify-between">
                Profile
                <span className="badge">New</span>
              </NavLink>
            </li>
            <li>
              <a>Settings</a>
            </li>
            <li>
              <button onClick={logout}>Logout</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
