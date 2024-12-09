import React, { useEffect, useState } from "react";
import { useAuth } from "../hooks/useAuth";
import type { PersonalTrainer, UserOptions } from "../types.ts";

const PersonalTrainerProfile: React.FC = () => {
  const { user } = useAuth();
  const { username, email, role } = user || {};
  const [myGym, setMyGym] = useState<string | undefined>();

  const isPersonalTrainer = (
    user: UserOptions | null,
  ): user is PersonalTrainer => {
    return user?.role === "personal_trainer";
  };

  useEffect(() => {
    if (isPersonalTrainer(user) && myGym === undefined) {
      console.log(user.linkedGym);
      setMyGym(user.linkedGym);
    }
  }, [user]);

  return (
    <div className="flex justify-center items-center">
      <div className="bg-background rounded-lg shadow-lg p-8  w-full mx-4">
        <hr className="my-4" />
        <div className="flex justify-center mb-6">
          <img
            src={user?.userSpecs.picture}
            alt={"profile"}
            className="rounded-circle size-32"
          />
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
        <div className="bg-background rounded-lg shadow-lg p-8 w-full mx-4">
          <h4 className="text-lg font-semibold">
            {myGym ?? "You have no gym associated."}
          </h4>
        </div>
        <hr className="my-4" />
      </div>
    </div>
  );
};

export default PersonalTrainerProfile;
