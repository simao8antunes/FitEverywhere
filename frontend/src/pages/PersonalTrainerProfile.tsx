import React, { useEffect, useState } from "react";
import { FiUser } from "react-icons/fi";
import { useAuth } from "../hooks/useAuth";

const PersonalTrainerProfile: React.FC = () => {

    const { user } = useAuth();
    const { username, email, role } = user || {};

    return (
        <div className="flex justify-center items-center">
            <div className="bg-background rounded-lg shadow-lg p-8  w-full mx-4">
            <hr className="my-4" />
            <div className="flex justify-center mb-6">
                <FiUser className="w-20 h-20" />
            </div>
            <h2 className="text-2xl font-semibold text-primary mb-4">
                {username ? `Welcome, ${username}!` : "Welcome!"}
            </h2>
            <div className="text-left space-y-3">
                <div>
                <span className="font-bold">Username:</span> {username || "N/A"}
                </div>
                <div>
                <span className="font-bold">Email:</span> {email || "N/A"}
                </div>
                <div>
                <span className="font-bold">Role:</span> {role || "N/A"}
                </div>
            </div>
            <hr className="my-4" />
            </div>  
        </div>
    );
};

export default PersonalTrainerProfile;
