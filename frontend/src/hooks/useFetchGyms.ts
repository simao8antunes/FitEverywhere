import { useState } from "react";
import { Gym, GymResponse } from "../types";

// OpenStreetMap-related URLs
const OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";

// Function to calculate the distance between two coordinates (lat1, lon1) and (lat2, lon2)
const calculateDistance = (lat1: number, lon1: number, lat2: number, lon2: number) => {
  const R = 6371; // Radius of the Earth in kilometers
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const distance = R * c; // Distance in kilometers

  return distance;
};

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
              // Sort gyms by distance to the event location
              const sortedGyms = gymsData.elements
                .map((gym: GymResponse) => {
                  const distance = calculateDistance(lat, lon, gym.lat, gym.lon);
                  return {

                  name: gym.tags?.name || "Unnamed Gym",
                  location: {
                    lat: gym.lat,
                    lng: gym.lon,
                  },
                  distance: distance.toFixed(2),
                };
        })
          .sort((a: { distance: number; }, b: { distance: number; }) => a.distance - b.distance); // Sort by distance


              setGyms(sortedGyms);
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
