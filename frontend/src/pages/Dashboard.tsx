import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Map from "../components/Map";
import Sidebar from "../components/Sidebar/Sidebar";

interface Event {
  id: string;
  summary: string;
  start: { dateTime: string };
  end: { dateTime: string };
  location?: string;
}

interface Gym {
  name: string;
  location: string;
  latitude: number;
  longitude: number;
}

const Dashboard: React.FC = () => {
  const location = useLocation();
  const userNameFromState = location.state?.userName;
  const userName = userNameFromState || "Guest";
  const [events, setEvents] = useState<Event[]>([]);
  const [nearbyGyms, setNearbyGyms] = useState<Gym[]>([]);

  // Fetch events
  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch("/api/auth/calendar/events", {
          method: "GET",
          credentials: "include",
        });
        if (response.ok) {
          const data = await response.json();
          const upcomingEvents = data.items.filter((event: Event) => {
            const eventStartDate = event.start?.dateTime
              ? new Date(event.start.dateTime)
              : null;
            const eventEndDate = event.end?.dateTime
              ? new Date(event.end.dateTime)
              : null;
            const currentDate = new Date();

            return (
              eventStartDate && eventEndDate && eventStartDate > currentDate
            );
          });

          setEvents(upcomingEvents);
        }
      } catch (error) {
        console.error("Error fetching calendar events:", error);
      }
    };
    fetchEvents();
  }, []);

  // Fetch nearby gyms based on event location
  const fetchNearbyGyms = async (location: string) => {
    try {
      // Step 1: Convert location (address) to latitude and longitude using Google Geocoding API
      const geocodingResponse = await fetch(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(location)}&key=AIzaSyAjEzYhZoH1GHZ_LrBXo7tjKTYzHOB7Cqs`
      );
      const geocodingData = await geocodingResponse.json();
  
      console.log("Geocoding response:", geocodingData); // Log geocoding data
  
      if (geocodingData.status === "OK") {
        const latitude = geocodingData.results[0].geometry.location.lat;
        const longitude = geocodingData.results[0].geometry.location.lng;
  
        console.log("Geocoding successful, coordinates:", latitude, longitude); // Log coordinates
  
        // Step 2: Use latitude and longitude to fetch nearby gyms
        const gymsResponse = await fetch(
          `/api/auth/gyms/nearby?latitude=${latitude}&longitude=${longitude}&radius=5000`
        );
        if (gymsResponse.ok) {
          const gymsData = await gymsResponse.json();
          console.log("Nearby gyms data:", gymsData); // Log gyms data
          setNearbyGyms(
            gymsData.results?.map((gym: any) => ({
              name: gym.name,
              location: gym.vicinity,
              latitude: gym.geometry.location.lat,
              longitude: gym.geometry.location.lng,
            })) || []
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

  return (
    <div className="app-container">
      <Sidebar userName={userName} />
      <div className="events-container">
        <h2>Upcoming Events</h2>
        <div className="events-list">
          {events.map((event) => (
            <div key={event.id} className="event-item">
              <h3 className="event-title">{event.summary}</h3>
              <p className="event-time">
                {new Date(event.start.dateTime).toLocaleString()} -{" "}
                {new Date(event.end.dateTime).toLocaleString()}
              </p>
            </div>
          ))}
        </div>
      </div>

      <div className="nearby-gyms-container">
        <h3>Nearby Gyms</h3>
        <div className="gyms-list">
          {nearbyGyms.length > 0 ? (
            <Map gyms={nearbyGyms} />
          ) : (
            <p>No nearby gyms found.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
