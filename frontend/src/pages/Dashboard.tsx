import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Sidebar from "../components/Sidebar/Sidebar";

const Dashboard: React.FC = () => {
  const location = useLocation();
  const userNameFromState = location.state?.userName;
  const userName = userNameFromState || "Guest";
  const [events, setEvents] = useState<any[]>([]);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch("/api/auth/calendar/events", {
          method: "GET",
          credentials: "include",
        });
        if (response.ok) {
          const data = await response.json();
          console.log(data);

          // Filter upcoming events
          const upcomingEvents = data.items.filter((event: any) => {
            // Check if both start and end have dateTime
            const eventStartDate = event.start?.dateTime
              ? new Date(event.start.dateTime)
              : null;
            const eventEndDate = event.end?.dateTime
              ? new Date(event.end.dateTime)
              : null;
            const currentDate = new Date();

            // Only include events with valid start and end times, and those that are in the future
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

  console.log("Received userName in Dashboard:", userName);

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
    </div>
  );
};

export default Dashboard;
