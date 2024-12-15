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
      </div>
    </>
  );
};

export default GymForm;
