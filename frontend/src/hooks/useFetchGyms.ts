import { useState } from "react";
import { Gym, GymResponse } from "../types";

// OpenStreetMap-related URLs
const OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";

export const useFetchGyms = () => {
  const [gyms, setGyms] = useState<Gym[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchNearbyGyms = async (location: string) => {
    setLoading(true);
    setError(null);

    // Geocoding request to Nominatim API
    const encodedLocation = encodeURIComponent(location);
    await fetch(
      `https://nominatim.openstreetmap.org/search?q=${encodedLocation}&format=json`,
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch geocoding data");
        }
        return response;
      })
      .then((response) => response.json())
      .then(async (geocodingData) => {
        if (geocodingData.length > 0) {
          const { lat, lon } = geocodingData[0];
          const response = await fetch(
            `${OVERPASS_API_URL}?data=[out:json];node["leisure"="fitness_centre"](around:2000,${lat},${lon});out;`,
          );
          if (!response.ok) {
            throw new Error("Failed to fetch nearby gyms");
          }
          return await response.json().then((gymsData) => {
            if (gymsData.elements && gymsData.elements.length > 0) {
              setGyms(
                gymsData.elements.map((gym: GymResponse) => ({
                  name: gym.tags?.name || "Unnamed Gym",
                  location: {
                    lat: gym.lat,
                    lng: gym.lon,
                  },
                })),
              );
            } else {
              setGyms([]); // Clear gyms if none found
              setError(
                "No nearby gyms found. Try a different location or increase the search radius.",
              );
            }
          });
        } else {
          setError("Geocoding failed: No results found for the location.");
        }
      })
      .catch((err: Error) => {
        setError("Error: " + err.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };
  return { gyms, fetchNearbyGyms, loading, error };
};
