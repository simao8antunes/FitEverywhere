import type { Gym } from "../types.ts";
import React from "react";

interface ShowGymProps {
  gym?: Gym;
}

const ShowGym: React.FC<ShowGymProps> = ({ gym }) => {
  return (
    <div>
      <h1> {gym?.name} </h1>
    </div>
  );
};

export default ShowGym;
