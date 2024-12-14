const GymForm = ({ selectedGym }) => {
  return (
    <>
      <h1 className="text-3xl font-bold">Gym Form</h1>
      <p className="text-text opacity-30">
        Here you can see all the details of the gym you manage.
      </p>
      <div className="p-3 flex gap-base">
        {selectedGym && (
          <div className="card bg-base-100 w-96 shadow-xl">
            <div className="card-body">
              <label className="form-control w-full max-w-xs">
                <div className="label">
                  <span className="card-title">Name</span>
                </div>
                <input
                  value={selectedGym?.name}
                  className="input input-bordered"
                />
              </label>
              <label className="form-control w-full max-w-xs">
                <div className="label">
                  <span className="label-text">Description</span>
                </div>
                <textarea
                  value={selectedGym?.description}
                  className="textarea textarea-bordered"
                />
              </label>
              <label className="form-control w-full max-w-xs">
                <div className="label">
                  <span className="label-text">Daily Fee</span>
                </div>
                <input
                  value={selectedGym?.dailyFee}
                  className="input input-bordered"
                />
              </label>
              <label className="form-control w-full max-w-xs">
                <div className="label">
                  <span className="label-text">Latitude</span>
                </div>
                <input
                  value={selectedGym?.latitude}
                  className="input input-bordered"
                />
              </label>
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default GymForm;
