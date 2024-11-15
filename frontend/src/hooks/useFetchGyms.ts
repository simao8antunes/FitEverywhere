import { useState } from "react";
import type { Gym, GymResponse } from "../types";

const API_KEY = "AIzaSyAjEzYhZoH1GHZ_LrBXo7tjKTYzHOB7Cqs"; // Consider keeping sensitive keys in .env file

export const useFetchGyms = () => {
  const [gyms, setGyms] = useState<Gym[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchNearbyGyms = async (location: string) => {
    setLoading(true);
    setError(null);
    try {
      // Geocoding API request
      const geocodingResponse = await fetch(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(location)}&key=${API_KEY}`,
      );
      const geocodingData = await geocodingResponse.json();

      if (geocodingData.status === "OK") {
        const { lat, lng } = geocodingData.results[0].geometry.location;

        // Fetch gyms based on latitude and longitude
        const gymsResponse = await fetch(
          `/api/auth/gyms/nearby?latitude=${lat}&longitude=${lng}&radius=5000`,
        );

        if (gymsResponse.ok) {
          const gymsData = await gymsResponse.json();
          setGyms(
            gymsData.results?.map((gym: GymResponse) => ({
              name: gym.name,
              vicinity: gym.vicinity,
              location: {
                lat: gym.geometry.location.lat,
                lng: gym.geometry.location.lng,
              },
            })) || [],
          );
        } else {
          setError("Failed to fetch nearby gyms");
        }
      } else {
        setError("Geocoding failed");
      }
    } catch (error) {
      setError("Error fetching nearby gyms: " + error);
    } finally {
      setLoading(false);
    }
  };

  return { gyms, fetchNearbyGyms, loading, error };
};
