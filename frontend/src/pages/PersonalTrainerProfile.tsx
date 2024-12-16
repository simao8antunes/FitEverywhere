import React, { useState } from "react";
import { useAuth } from "../hooks/useAuth";
import type { PersonalTrainer, UserOptions } from "../types.ts";
import { useFetchUser } from "../hooks/useFetchUser.ts";

const PersonalTrainerProfile: React.FC = () => {
  const isPersonalTrainer = (
    user: UserOptions | null,
  ): user is PersonalTrainer => {
    return user?.role === "personal_trainer";
  };

  const { user } = useAuth();
  const { updateUserData } = useFetchUser();
  const { username, email } = user || {};

  const [formData, setFormData] = useState<PersonalTrainer>({
    username: username || "",
    email: email || "",
    role: "personal_trainer",
    description:
      isPersonalTrainer(user) && user.description ? user.description : "",
    linkedGym: isPersonalTrainer(user) ? user.linkedGym : undefined,
    services: isPersonalTrainer(user) && user.services ? user.services : [],
  });

  const [isEditing, setIsEditing] = useState(false); // Track edit mode

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Updated profile data:", formData);
    updateUserData(formData);
    setIsEditing(false); // Exit edit mode after saving
  };

  const toggleEditMode = () => {
    setIsEditing((prev) => !prev);
  };

  return (
    <div className="flex justify-center items-center">
      <div className="card w-full bg-base-100 shadow-xl">
        <div className="card-body">
          {!isEditing ? ( // View mode
            <>
              <h2 className="text-2xl font-semibold text-primary mb-4">
                Personal Trainer Profile
              </h2>
              <div className="flex justify-center mb-6">
                <img
                  src={user?.userSpecs?.picture}
                  alt={"profile"}
                  className="rounded-circle size-32"
                />
              </div>
              <p className="mb-2 text-center">
                <h1 className="text-3xl font-semibold text-primary mb-4">
                  {formData.username}
                </h1>
              </p>
              <p className="mb-2">
                <strong>Email:</strong> {formData.email}
              </p>
              <p className="mb-2">
                <strong>Description:</strong>
                <p style={{ whiteSpace: "pre-line" }}>
                  {formData.description || "N/A"}
                </p>
              </p>
              <p className="mb-2">
                <strong>Linked Gym:</strong>{" "}
                {formData.linkedGym || "Wait for your gym owner to add you."}
              </p>
              <div className="card-actions justify-end">
                <button className="btn btn-secondary" onClick={toggleEditMode}>
                  Edit Profile
                </button>
              </div>
            </>
          ) : (
            // Edit mode
            <form onSubmit={handleSubmit}>
              <h2 className="text-2xl font-semibold text-primary mb-4">
                Edit Your Profile
              </h2>
              <div className="mb-4">
                <label htmlFor="username" className="block font-bold mb-1">
                  Username:
                </label>
                <input
                  type="text"
                  id="username"
                  name="username"
                  value={formData.username}
                  onChange={handleInputChange}
                  className="input input-bordered w-full"
                  required
                  disabled
                />
              </div>
              <div className="mb-4">
                <label htmlFor="email" className="block font-bold mb-1">
                  Email:
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className="input input-bordered w-full"
                  required
                  disabled
                />
              </div>
              <div className="mb-4">
                <label htmlFor="description" className="block font-bold mb-1">
                  Description:
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  className="textarea textarea-bordered w-full"
                  rows={4}
                />
              </div>
              <div className="mb-4">
                <label htmlFor="linkedGym" className="block font-bold mb-1">
                  Linked Gym:
                </label>
                <input
                  type="text"
                  id="linkedGym"
                  placeholder="Wait for your gym owner to add you."
                  disabled
                  name="linkedGym"
                  value={formData.linkedGym || ""}
                  onChange={handleInputChange}
                  className="input input-bordered w-full"
                />
              </div>
              <div className="card-actions justify-center">
                <button type="submit" className="btn btn-primary">
                  Save Changes
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={toggleEditMode}
                >
                  Cancel
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default PersonalTrainerProfile;
