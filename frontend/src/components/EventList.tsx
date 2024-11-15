import React from "react";
import type { UseFetchEventsResult } from "../types";

interface EventListProps extends UseFetchEventsResult {
  onEventClick: (event: any) => void; // Replace `any` with your event type
}

const EventList: React.FC<EventListProps> = ({
  events,
  loading,
  error,
  onEventClick,
}) => {
  // Render loading state
  if (loading) {
    return <div>Loading events...</div>;
  }

  // Render error state
  if (error) {
    return <div>Error: {error}</div>;
  }

  // Render no events message
  if (events.length === 0) {
    return <p>No upcoming events.</p>;
  }

  // Render list of events
  return (
    <div className="bg-white shadow-md rounded-lg p-6">
      <div className="space-y-4">
        {events.map((event) => (
          <div
            key={event.id}
            className="bg-gray-100 rounded-lg p-4 border border-gray-200 hover:bg-gray-200 transition-colors cursor-pointer"
            onClick={() => onEventClick(event)}
          >
            <h3 className="text-lg font-semibold">{event.summary}</h3>
            <p className="text-sm text-gray-600">
              {new Date(event.start.dateTime).toLocaleString()} -{" "}
              {new Date(event.end.dateTime).toLocaleString()}
              <p>{event.location}</p>
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EventList;
