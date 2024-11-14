import React, { useEffect, useState } from "react";
import GoogleMap from "../components/GoogleMap.tsx";
import { useFetchEvents } from "../hooks/useFetchEvents.ts";

interface Gym {
  name: string;
  vicinity: string;
  location: {
    lng: number;
    lat: number;
  };
}

interface GymResponse {
  name: string;
  vicinity: string;
  geometry: {
    location: {
      lat: number;
      lng: number;
    };
  };
}

const Dashboard: React.FC = () => {
  const { events, loading, error } = useFetchEvents();
  const [nearbyGyms, setNearbyGyms] = useState<Gym[]>([]);

  // Fetch nearby gyms based on event location
  const fetchNearbyGyms = async (location: string) => {
    try {
      // Step 1: Convert location (address) to latitude and longitude using Google Geocoding API
      const geocodingResponse = await fetch(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(location)}&key=AIzaSyAjEzYhZoH1GHZ_LrBXo7tjKTYzHOB7Cqs`,
      );
      const geocodingData = await geocodingResponse.json();

      console.log("Geocoding response:", geocodingData); // Log geocoding data

      if (geocodingData.status === "OK") {
        const latitude = geocodingData.results[0].geometry.location.lat;
        const longitude = geocodingData.results[0].geometry.location.lng;

        console.log("Geocoding successful, coordinates:", latitude, longitude); // Log coordinates

        // Step 2: Use latitude and longitude to fetch nearby gyms
        const gymsResponse = await fetch(
          `/api/auth/gyms/nearby?latitude=${latitude}&longitude=${longitude}&radius=5000`,
        );
        if (gymsResponse.ok) {
          const gymsData = await gymsResponse.json();
          console.log("Nearby gyms data:", gymsData); // Log gyms data
          setNearbyGyms(
            gymsData.results?.map((gym: GymResponse) => ({
              name: gym.name,
              vicinity: gym.vicinity,
              location: {
                lat: gym.geometry.location.lat,
                lng: gym.geometry.location.lng,
              },
            })) || [],
          ); // Transform gyms data
        } else {
          console.error("Failed to fetch nearby gyms");
        }
      } else {
        console.error("Geocoding failed", geocodingData.status);
      }
    } catch (error) {
      console.error("Error fetching nearby gyms:", error);
    }
  };

  useEffect(() => {
    if (events.length > 0 && events[0].location) {
      fetchNearbyGyms(events[0].location);
    }
  }, [events]);

  // Conditional rendering based on loading/error states
  if (loading) {
    return <div>Loading events...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="app-container">
      <div className="events-container">
        <h2>Upcoming Events</h2>
        {events.length === 0 ? (
          <p>No upcoming events.</p>
        ) : (
          <div className="events-list">
            {events.map(({ id, summary, start, end }) => (
              <div key={id} className="event-item">
                <h3 className="event-title">{summary}</h3>
                <p className="event-time">
                  {new Date(start.dateTime).toLocaleString()} -{" "}
                  {new Date(end.dateTime).toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="nearby-gyms-container">
        <h3>Nearby Gyms</h3>
        <div className="gyms-list">
          {nearbyGyms.length > 0 ? (
            <GoogleMap gyms={nearbyGyms} />
          ) : (
            <p>No nearby gyms found.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
