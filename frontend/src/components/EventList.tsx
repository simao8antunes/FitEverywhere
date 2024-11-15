import React from "react";
import type { UseFetchEventsResult } from "../types";

const EventList: React.FC<UseFetchEventsResult> = ({
  events,
  loading,
  error,
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
        {events.map(({ id, summary, start, end }) => (
          <div
            key={id}
            className="bg-gray-100 rounded-lg p-4 border border-gray-200 hover:bg-gray-200 transition-colors"
          >
            <h3 className="text-lg font-semibold">{summary}</h3>
            <p className="text-sm text-gray-600">
              {new Date(start.dateTime).toLocaleString()} -{" "}
              {new Date(end.dateTime).toLocaleString()}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EventList;
