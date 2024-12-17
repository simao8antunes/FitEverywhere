import { useFetchGyms } from "../../hooks/useFetchGyms.ts";
import { useFetchUser } from "../../hooks/useFetchUser.ts";
import { useEffect, useState } from "react";
import { Gym, PersonalTrainer } from "../../types.ts";

interface GymFormProps {
  selectedGym: Gym | null;
}

const GymForm = ({ selectedGym }: GymFormProps) => {
  const [message, setMessage] = useState<string | null>(null);
  const { fetchGymDetails, updateGym } = useFetchGyms();
  const { fetchAvailablePTs } = useFetchUser();
  const [availablePTs, setAvailablePTs] = useState<PersonalTrainer[]>([]);
  const [isAddingPT, setIsAddingPT] = useState<boolean>(false);
  const [selectedGymForm, setSelectedGymForm] = useState<Gym | null>(
    selectedGym
  );

  const handleAddPT = async (index: number) => {
    try {
      // Add personal trainer to gym
      const url = new URL(
        import.meta.env.VITE_API_BASE_URL +
          "/gym/" +
          selectedGymForm?.id +
          "/link-pt",
        window.location.origin
      );
      url.searchParams.append("ptEmail", availablePTs[index].email);

      const response = await fetch(url.toString(), {
        method: "POST",
        credentials: "include",
      });

      if (response.ok) {
        setMessage("Personal trainer linked successfully!");

        // Update the state to include the newly linked personal trainer
        setSelectedGymForm((prev) => {
          if (!prev) return prev;
          return {
            ...prev,
            personalTrainers: [...prev.personalTrainers, availablePTs[index]],
          };
        });
      } else {
        const errorData = await response.json();
        setMessage(`Error: ${errorData.message}`);
      }
    } catch (error) {
      setMessage("Error linking PT. Please try again later." + error);
    }
  };

  useEffect(() => {
    if (selectedGym) {
      fetchGymDetails(selectedGym.id, setSelectedGymForm);
    }
  }, [selectedGym]);

  return (
    <>
      <h1 className="text-3xl font-bold">Gym Form</h1>
      <p className="text-text opacity-30">
        Here you can see all the details of the gym you manage.
      </p>
      <div className="p-base grid grid-cols-3">
        {selectedGymForm && (
          <div className="card bg-base-100 shadow-xl">
            <div className="card-body gap-10">
              <label className="form-control">
                <div className="label">
                  <span className="card-title">Name</span>
                </div>
                <input
                  disabled
                  value={selectedGymForm?.name}
                  className="input input-bordered card-title"
                />
              </label>
              <label className="form-control">
                <div className="label">
                  <span className="label-text">Description</span>
                </div>
                <textarea
                  value={selectedGymForm?.description ?? ""}
                  onChange={(e) => {
                    setSelectedGymForm({
                      ...selectedGymForm,
                      description: e.target.value,
                    });
                  }}
                  className="textarea textarea-bordered"
                />
              </label>
              <label className="form-control">
                <div className="label">
                  <span className="label-text">Daily Fee</span>
                </div>
                <input
                  type="number"
                  value={selectedGymForm?.dailyFee ?? 0}
                  onChange={(e) => {
                    setSelectedGymForm({
                      ...selectedGymForm,
                      dailyFee: Number(e.target.value),
                    });
                  }}
                  className="input input-bordered"
                />
              </label>
              <label className="form-control">
                <div className="label">
                  <span className="label-text">Weekly Membership</span>
                </div>
                <input
                  type="number"
                  value={selectedGymForm?.weeklyMembership ?? 0}
                  onChange={(e) => {
                    setSelectedGymForm({
                      ...selectedGymForm,
                      weeklyMembership: Number(e.target.value),
                    });
                  }}
                  className="input input-bordered"
                />
              </label>
              {selectedGymForm?.overpassData && (
                <div className="grid grid-cols-2 gap-base">
                  <label className="form-control">
                    <div className="label">
                      <span className="label-text">Latitude</span>
                    </div>
                    <input
                      type="number"
                      disabled
                      value={selectedGymForm?.overpassData.lat}
                      className="input input-bordered"
                    />
                  </label>
                  <label className="form-control">
                    <div className="label">
                      <span className="label-text">Longitude</span>
                    </div>
                    <input
                      type="number"
                      disabled
                      value={selectedGymForm?.overpassData.lon}
                      className="input input-bordered"
                    />
                  </label>
                </div>
              )}

              <div className="card-actions justify-center">
                <button
                  className="btn btn-primary"
                  onClick={() => updateGym(selectedGymForm)}
                >
                  Save Gym Details
                </button>
              </div>
            </div>
          </div>
        )}
        {selectedGymForm && (
          <div className="card bg-base-100 shadow-xl ml-10">
            <div className="card-body gap-10">
              <label className="form-control">
                <div className="label">
                  <span className="card-title">Linked Personal Trainers</span>
                </div>
              </label>
              <ul>
                {selectedGymForm.personalTrainers.map((trainer) => (
                  <li>
                    <h5>{trainer.username}</h5>
                  </li>
                ))}
              </ul>
              <div className="card-actions justify-center">
                <button
                  className="btn btn-primary"
                  onClick={() => {
                    setIsAddingPT(!isAddingPT);
                    fetchAvailablePTs().then((data) => {
                      if (data) {
                        setAvailablePTs(data);
                      }
                    });
                  }}
                >
                  Add Personal Trainer
                </button>
              </div>
            </div>
          </div>
        )}
        {isAddingPT && (
          <div className="card bg-base-100 shadow-xl ml-10">
            <div className="card-body gap-10">
              <label className="form-control">
                <div className="label">
                  <span className="card-title">
                    Available Personal Trainers
                  </span>
                </div>
              </label>

              {availablePTs.map((trainer, index) => (
                <div
                  key={index}
                  className="card bg-base-200 border-secbackground hover:bg-secbackground transition-colors"
                >
                  <div className="card-body">
                    <h4 className="card-title">{trainer.username}</h4>
                    <p>{trainer.email}</p>
                    <div className="card-actions justify-end">
                      <button
                        onClick={() => {
                          handleAddPT(index), setIsAddingPT(false);
                        }}
                        className="btn btn-primary"
                      >
                        Add to Gym
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default GymForm;
