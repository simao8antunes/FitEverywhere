import React from "react";
import logo from "../../public/logo.svg";
import { SiGoogle } from "react-icons/si";

const Login: React.FC = () => {
  const handleLogin = () => {
    window.location.href = "/api/oauth2/authorization/google";
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-gray-800 text-white rounded-lg shadow-lg p-8 max-w-sm w-full mx-4 text-center">
        {/* Logo */}
        <div className="flex justify-center mb-6">
          <img src={logo} alt="FitEverywhere Logo" className="w-20 h-20" />
        </div>

        {/* Title */}
        <h1 className="text-2xl font-semibold mb-4">FitEverywhereA</h1>

        {/* Description */}
        <p className="text-gray-400 mb-8 px-4 leading-relaxed">
          With FitEverywhere, maintaining a fitness routine becomes a rewarding
          part of the travel experience, making it easy to stay active and
          healthy—anytime, anywhere.
        </p>

        {/* Login Button with Google Icon */}
        <button
          onClick={handleLogin}
          className="flex items-center justify-center w-full bg-white text-gray-700 border border-gray-300 py-3 rounded-lg font-medium shadow hover:bg-gray-100 transition"
        >
          <SiGoogle className="text-red-500 w-5 h-5 mr-2" />{" "}
          {/* Google Icon in Red */}
          Login with Google
        </button>
      </div>
    </div>
  );
};

export default Login;
