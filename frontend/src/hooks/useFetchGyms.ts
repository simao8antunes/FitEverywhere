import { useState } from "react";
import type { Gym } from "../types";

// OpenStreetMap-related URLs
const OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";

export const useFetchGyms = () => {
  const [gyms, setGyms] = useState<Gym[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchNearbyGyms = async (location: string) => {
    setLoading(true);
    setError(null);
  
    try {
      // Geocoding request to Nominatim API
      const encodedLocation = encodeURIComponent(location);
      const geocodingResponse = await fetch(
        `https://nominatim.openstreetmap.org/search?q=${encodedLocation}&format=json`
      );
  
      if (!geocodingResponse.ok) {
        throw new Error("Failed to fetch geocoding data");
      }
  
      const geocodingData = await geocodingResponse.json();
  
      if (geocodingData.length > 0) {
        const { lat, lon } = geocodingData[0];
  
        // Fetch gyms using Overpass API
        const gymsResponse = await fetch(
          `${OVERPASS_API_URL}?data=[out:json];node["leisure"="fitness_centre"](around:2000,${lat},${lon});out;`
        );
  
        if (!gymsResponse.ok) {
          throw new Error("Failed to fetch nearby gyms");
        }
  
        const gymsData = await gymsResponse.json();
  
        if (gymsData.elements && gymsData.elements.length > 0) {
          setGyms(
            gymsData.elements.map((gym: any) => ({
              name: gym.tags?.name || "Unnamed Gym",
              vicinity: gym.tags?.address || "Unknown address",
              location: {
                lat: gym.lat,
                lng: gym.lon,
              },
            }))
          );
        } else {
          setGyms([]); // Clear gyms if none found
          setError("No nearby gyms found. Try a different location or increase the search radius.");
        }
      } else {
        setError("Geocoding failed: No results found for the location.");
      }
    } catch (err: any) {
      setError("Error: " + err.message);
    } finally {
      setLoading(false);
    }
  };
  

  return { gyms, fetchNearbyGyms, loading, error };
};