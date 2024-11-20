import React, { useEffect, useState } from "react";
import { FiMapPin } from "react-icons/fi";

const GymProfile: React.FC = () => {
  const [gymName, setGymName] = useState<string | null>(null);
  const [location, setLocation] = useState<string | null>(null);
  const [contactInfo, setContactInfo] = useState<string | null>(null);
  const [openingHours, setOpeningHours] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const fetchGymDetails = async () => {
      try {
        const response = await fetch(`/api/gym/details`, {
          method: "GET",
          credentials: "include",
        });

        if (!response.ok) {
          throw new Error("Failed to fetch gym details");
        }

        const data = await response.json();
        setGymName(data.gymName);
        setLocation(data.location);
        setContactInfo(data.contactInfo);
        setOpeningHours(data.openingHours);
      } catch (error) {
        console.error("Error fetching gym details:", error);
      }
    };

    fetchGymDetails();
  }, []);

  const handleSaveGymDetails = async () => {
    try {
      const response = await fetch(`/api/gym/details`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          gymName,
          location,
          contactInfo,
          openingHours,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Failed to save gym details:", errorData.message || response.statusText);
        alert("Failed to save gym details");
        return;
      }

      const data = await response.json();
      alert(data.message);
      setIsEditing(false);
    } catch (error) {
      console.error("Error saving gym details:", error);
      alert("Error saving gym details");
    }
  };

  return (
    <div className="flex justify-center items-center w-max">
      <div className="bg-background rounded-lg shadow-lg p-8 max-w-md w-full mx-4">
        <div className="flex justify-center mb-6">
          <FiMapPin className="w-20 h-20" />
        </div>
        <h2 className="text-2xl font-semibold text-primary mb-4">
          {gymName ? `Manage Gym: ${gymName}` : "Gym Profile"}
        </h2>
        {!isEditing ? (
          <div className="text-left space-y-3">
            <div>
              <span className="font-bold">Gym Name:</span> {gymName || "Not set"}
            </div>
            <div>
              <span className="font-bold">Location:</span> {location || "Not set"}
            </div>
            <div>
              <span className="font-bold">Contact Info:</span> {contactInfo || "Not set"}
            </div>
            <div>
              <span className="font-bold">Opening Hours:</span> {openingHours || "Not set"}
            </div>
            <button
              className="bg-primary text-white py-2 px-4 rounded mt-4"
              onClick={() => setIsEditing(true)}
            >
              Edit Gym Details
            </button>
          </div>
        ) : (
          <div>
            <div>
              <label className="font-bold">Gym Name:</label>
              <input
                type="text"
                className="border p-2 rounded w-full"
                value={gymName || ""}
                onChange={(e) => setGymName(e.target.value)}
              />
            </div>
            <div>
              <label className="font-bold">Location:</label>
              <input
                type="text"
                className="border p-2 rounded w-full"
                value={location || ""}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>
            <div>
              <label className="font-bold">Contact Info:</label>
              <input
                type="text"
                className="border p-2 rounded w-full"
                value={contactInfo || ""}
                onChange={(e) => setContactInfo(e.target.value)}
              />
            </div>
            <div>
              <label className="font-bold">Opening Hours:</label>
              <input
                type="text"
                className="border p-2 rounded w-full"
                value={openingHours || ""}
                onChange={(e) => setOpeningHours(e.target.value)}
              />
            </div>
            <button
              className="bg-primary text-white py-2 px-4 rounded mt-4"
              onClick={handleSaveGymDetails}
            >
              Save Details
            </button>
            <button
              className="bg-secondary text-white py-2 px-4 rounded mt-4 ml-2"
              onClick={() => setIsEditing(false)}
            >
              Cancel
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default GymProfile;
