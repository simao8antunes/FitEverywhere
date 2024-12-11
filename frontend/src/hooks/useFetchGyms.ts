import { useState } from "react";
import { Gym, GymResponse } from "../types";

// OpenStreetMap-related URLs
const OVERPASS_API_URL = "http://overpass-api.de/api/interpreter";
const API_URL = import.meta.env.VITE_API_BASE_URL as string;

// Function to calculate the distance between two coordinates (lat1, lon1) and (lat2, lon2)
const calculateDistance = (
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number,
) => {
  const R = 6371; // Radius of the Earth in kilometers
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  // Distance in kilometers
  return R * c;
};

export const useFetchGyms = () => {
  const [gyms, setGyms] = useState<Gym[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchOwnGyms = async () => {
    setLoading(true);
    setError(null);
    const response = await fetch(API_URL + "/gym-manager/list-gyms", {
      credentials: "include",
    });
    if (!response.ok) {
      setError("Failed to fetch gyms");
      setLoading(false);
      return;
    }
    if (response.status === 204) {
      setGyms([]);
      setLoading(false);
      return;
    }
    const data = await response.json();
    if (data.gyms && data.gyms.length > 0) {
      setGyms(data.gyms);
    } else {
      setGyms([]);
      setError("No gyms found. Try adding a new gym.");
    }
    setLoading(false);
  };

  //Fetch Gym for Personal Trainer
  /*const fetchPTGym = async () => {
    setLoading(true);
    setError(null);
  };*/

  // api gyms
  const fetchGyms = async (location: string) => {
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
              console.log(gymsData.elements);
              // Sort gyms by distance to the event location
              const sortedGyms = gymsData.elements
                .map((gym: GymResponse) => {
                  const distance = calculateDistance(
                    lat,
                    lon,
                    gym.lat,
                    gym.lon,
                  );

                  return {
                    id: gym.id,
                    name: gym.tags?.name || "Unnamed Gym",
                    latitude: gym.lat,
                    longitude: gym.lon,
                    distance: distance.toFixed(2),
                  };
                })
                .sort(
                  (a: { distance: number }, b: { distance: number }) =>
                    a.distance - b.distance,
                ); // Sort by distance

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

  // api gyms and db gyms
  const fetchNearbyGyms = async (location: string) => {
    setLoading(true);
    setError(null);

    // Geocoding request to Nominatim API
    const encodedLocation = encodeURIComponent(location);
    try {
      const geocodingResponse = await fetch(
        `https://nominatim.openstreetmap.org/search?q=${encodedLocation}&format=json`,
      );

      if (!geocodingResponse.ok) {
        throw new Error("Failed to fetch geocoding data");
      }

      const geocodingData = await geocodingResponse.json();

      if (geocodingData.length === 0) {
        setError("Geocoding failed: No results found for the location.");
        setGyms([]);
        return;
      }

      const { lat, lon } = geocodingData[0];

      // Fetch gyms from database
      const dbResponse = await fetch(API_URL + "/gym/all", {
        credentials: "include",
      });

      if (!dbResponse.ok) {
        throw new Error("Failed to fetch gyms from the database");
      }

      const dbData = await dbResponse.json();
      console.log(dbData);
      const dbGyms = dbData || [];
      console.log("DB GYMS: ", dbGyms);

      // Fetch gyms from OpenStreetMap API
      const osmResponse = await fetch(
        `${OVERPASS_API_URL}?data=[out:json];node["leisure"="fitness_centre"](around:2000,${lat},${lon});out;`,
      );

      if (!osmResponse.ok) {
        throw new Error("Failed to fetch nearby gyms from OpenStreetMap API");
      }

      const osmData = await osmResponse.json();
      const osmGyms = osmData.elements || [];
      console.log("API GYMS: ", osmGyms);

      // Merge gyms from the database and OpenStreetMap
      const mergedGyms = osmGyms.map((osmGym: GymResponse) => {
        const matchingDbGym = dbGyms.find(
          (dbGym: Gym) => dbGym.id === osmGym.id,
        );

        const distance = calculateDistance(lat, lon, osmGym.lat, osmGym.lon);

        return {
          id: osmGym.id,
          name: matchingDbGym?.name || osmGym.tags?.name || "Unnamed Gym",
          dailyFee: matchingDbGym?.dailyFee || "-",
          latitude: osmGym.lat,
          longitude: osmGym.lon,
          distance: distance.toFixed(2),
        };
      });

      setGyms(mergedGyms);
    } catch (err: unknown) {
      const error = err as Error;
      setError("Error: " + error.message);
    } finally {
      setLoading(false);
    }
  };
  return { gyms, fetchNearbyGyms, loading, error, fetchOwnGyms, fetchGyms };
};
