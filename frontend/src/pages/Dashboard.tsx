import React from "react";
import Sidebar from "../components/Sidebar";
import { useFetchEvents } from "../hooks/useFetchEvents.ts";
import { useAuth } from "../hooks/useAuth.ts";

const Dashboard: React.FC = () => {
  const auth = useAuth();
  const { events, loading, error } = useFetchEvents();

  if (!auth) return <div>Not authorized</div>;

  // Conditional rendering based on loading/error states
  if (loading) {
    return <div>Loading events...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="app-container">
      <Sidebar userName={auth.user!.username} />
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
    </div>
  );
};

export default Dashboard;
