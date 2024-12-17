import { useFetchGyms } from "../../hooks/useFetchGyms.ts";
import { useEffect, useState } from "react";
import { Gym } from "../../types.ts";

interface GymFormProps {
  selectedGym: Gym | null;
}

const GymForm = ({ selectedGym }: GymFormProps) => {
  const { fetchGymDetails, updateGym } = useFetchGyms();
  const [selectedGymForm, setSelectedGymForm] = useState<Gym | null>(
    selectedGym,
  );

  useEffect(() => {
    if (selectedGym) {
      fetchGymDetails(selectedGym.id, setSelectedGymForm);
    }
  }, [selectedGym]);

  return (
    <>
    </>
  );
};

export default GymForm;
